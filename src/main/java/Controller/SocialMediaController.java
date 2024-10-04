package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
import java.util.Optional;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     *
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::registerAccountHandler);
        app.post("/login", this::loginAccountHandler);
        app.post("/messages", this::messageCreateHandler);
        app.get("/messages", this::getMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::messageDeleteByIDHandler);
        app.patch("/messages/{message_id}", this::updateMsgByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getMessageByUserHandler);

        return app;
    }

    /**
     * Handles the registration of a new account.
     * @param context The Javalin Context object containing the HTTP request and response.
     */
    private void registerAccountHandler(Context context) {
        Account account = context.bodyAsClass(Account.class);
        Optional<Account> registeredAccount = accountService.registerAccount(account.getUsername(), account.getPassword());

        if (registeredAccount.isPresent()) {
            context.status(200).json(registeredAccount.get()); // 200 OK
        } else {
            context.status(400); // 400 Bad Request
        }
    }

    /**
     * Handles the login of an account.
     * @param context The Javalin Context object containing the HTTP request and response.
     */
    private void loginAccountHandler(Context context) {
        Account account = context.bodyAsClass(Account.class);
        Optional<Account> loggedInAccount = accountService.login(account.getUsername(), account.getPassword());

        if (loggedInAccount.isPresent()) {
            context.status(200).json(loggedInAccount.get()); // 200 OK
        } else {
            context.status(401); // 401 Unauthorized
        }
    }

    /**
     * Handles the creation of a new message.
     * @param context The Javalin Context object containing the HTTP request and response.
     */
    private void messageCreateHandler(Context context) {
        Message message = context.bodyAsClass(Message.class);
        Optional<Message> createdMessage = messageService.createMessage(message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());

        if (createdMessage.isPresent()) {
            context.status(200).json(createdMessage.get());
        } else {
            context.status(400);
        }
    }

    /**
     * Retrieves all messages.
     * @param context The Javalin Context object containing the HTTP request and response.
     */
    private void getMessageHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.status(200).json(messages);
    }

    /**
     * Retrieves a message by its ID.
     * @param context The Javalin Context object containing the HTTP request and response.
     */
    private void getMessageByIDHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Optional<Message> message = messageService.getMessageById(messageId);

        if (message.isPresent()) {
            context.status(200).json(message.get());
        } else {
            context.status(200).result("");
        }
    }

    /**
     * Deletes a message by its ID.
     * @param context The Javalin Context object containing the HTTP request and response.
     */
    private void messageDeleteByIDHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Optional<Message> message = messageService.getMessageById(messageId);

        if (message.isPresent() && messageService.deleteMessageById(messageId)) {
            context.status(200).json(message.get());
        } else {
            context.status(200).result("");
        }
    }

    /**
     * Updates the text of a message identified by its ID.
     * @param context The Javalin Context object containing the HTTP request and response.
     */
    private void updateMsgByIDHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        String newText = context.bodyAsClass(Message.class).getMessage_text();
        Optional<Message> updatedMessage = messageService.updateMessageText(messageId, newText);

        if (updatedMessage.isPresent()) {
            context.status(200).json(updatedMessage.get());
        } else {
            context.status(400);
        }
    }

    /**
     * Retrieves all messages posted by a specific user.
     * @param context The Javalin Context object containing the HTTP request and response.
     */
    private void getMessageByUserHandler(Context context) {
        int userId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByUserId(userId);
        context.status(200).json(messages);
    }
}