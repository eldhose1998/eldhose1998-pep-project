package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;
import java.util.Optional;

public class MessageService {

    private final MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    /**
     * Creates a new message.
     * @param postedBy The ID of the user posting the message.
     * @param messageText The text of the message.
     * @param timePostedEpoch The epoch time when the message was posted.
     * @return An Optional containing the created Message if successful, or an empty Optional if not.
     */
    public Optional<Message> createMessage(int postedBy, String messageText, long timePostedEpoch) {
        if (messageText == null || messageText.isBlank() || messageText.length() > 255) {
            return Optional.empty();
        }

        Message newMessage = new Message(postedBy, messageText, timePostedEpoch);
        boolean isCreated = messageDAO.createMessage(newMessage);
        return isCreated ? Optional.of(newMessage) : Optional.empty();
    }

    /**
     * Retrieves all messages.
     * @return A list of all messages.
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * Retrieves a message by its ID.
     * @param messageId The ID of the message to retrieve.
     * @return An Optional containing the Message if found, or an empty Optional if not.
     */
    public Optional<Message> getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    /**
     * Deletes a message by its ID.
     * @param messageId The ID of the message to delete.
     * @return True if the message was successfully deleted, false otherwise.
     */
    public boolean deleteMessageById(int messageId) {
        return messageDAO.deleteMessageById(messageId);
    }

    /**
     * Updates the text of a message identified by its ID.
     * @param messageId The ID of the message to update.
     * @param newText The new text for the message.
     * @return An Optional containing the updated Message if successful, or an empty Optional if not.
     */
    public Optional<Message> updateMessageText(int messageId, String newText) {
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            return Optional.empty();
        }
        return messageDAO.updateMessageText(messageId, newText);
    }

    /**
     * Retrieves all messages posted by a specific user.
     * @param userId The ID of the user whose messages to retrieve.
     * @return A list of messages posted by the specified user.
     */
    public List<Message> getMessagesByUserId(int userId) {
        return messageDAO.getMessagesByUserId(userId);
    }
}
