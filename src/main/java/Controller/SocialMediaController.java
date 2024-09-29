package Controller;

import java.util.Optional;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public SocialMediaController(){
        this.messageDAO=new MessageDAO();
        this.accountDAO=new AccountDAO();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
       //app.get("example-endpoint", this::exampleHandler);

        app.post("/register", this::registerAccountHandler);
        app.post("/login", this::loginAccountHandler);
        app.post("/messages", this::messageCreateHandler);
        app.get("/messages", this::getMessageHandler);
        app.get("/messages", this::getMessageByIDHandler);
        app.delete("/message/{message_id}", this::messageDeleteByIDHandler);
        app.patch("/messages/{message_id}", this::updateMsgByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getMessageByUserHandler);

        app.start(8080);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerAccountHandler(Context context) {
        Account account=context.bodyAsClass(Account.class);
        
        if (account.getUsername() == null || account.getUsername().isBlank() || 
        account.getPassword() == null || account.getPassword().length() < 4 || 
        accountDAO.usernameExists(account.getUsername())) {
        context.status(400);  // Bad request
        return;
        }

        Account registeredAccount = accountDAO.createAccount(account);
        context.status(200).json(registeredAccount);
    }


    private void loginAccountHandler(Context context){
        Account account=context.bodyAsClass(Account.class);

        Account existingAccount=accountDAO.validateLogin(account.getUsername(),account.getPassword());
        if(existingAccount!=null){
            context.status(200).json(existingAccount);
        }else{
            context.status(401);
        }
    }

    private void messageCreateHandler(Context context) {
        // Parse the incoming JSON to a Message object
        Message message = context.bodyAsClass(Message.class);
        
        // Validate the message content
        if (message.getMessage_text() == null || message.getMessage_text().isBlank() || 
            message.getMessage_text().length() > 255 || 
            !accountDAO.userExists(message.getPosted_by())) {
            context.status(400);  // Bad request
            return;
        }
    
        // Create the message in the database
        Message createdMessage = messageDAO.createMessage(message);
        
        // Return the created message, including message_id
        context.json(createdMessage);
    }

    private void getMessageHandler(Context context){
    }

    private void getMessageByIDHandler(Context context){
    }

    private void messageDeleteByIDHandler(Context context){
    }

    private void updateMsgByIDHandler(Context context){
    }

    private void getMessageByUserHandler(Context context){
    }
}