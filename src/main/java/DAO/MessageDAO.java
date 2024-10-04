package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

public class MessageDAO {

    private static final Logger LOGGER = System.getLogger(MessageDAO.class.getName());

    public boolean createMessage(Message message) {
        String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        Connection conn = null;
        try {
            conn = ConnectionUtil.getConnection();
            if (conn == null) {
                return false;
            }
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, message.getPosted_by());
            stmt.setString(2, message.getMessage_text());
            stmt.setLong(3, message.getTime_posted_epoch());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    message.setMessage_id(generatedKeys.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Error creating message", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.ERROR, "Error closing connection", e);
                }
            }
        }
        return false;
    }

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message";
        Connection conn = null;
        try {
            conn = ConnectionUtil.getConnection();

            if (conn == null) {
                return messages;
            }

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                messages.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Error finding all messages", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.ERROR, "Error closing connection", e);
                }
            }
        }
        return messages;
    }

    public Optional<Message> getMessageById(int messageId) {
        String sql = "SELECT * FROM Message WHERE message_id = ?";
        Connection conn = null;
        try {
            conn = ConnectionUtil.getConnection();
            if (conn == null) {
                return Optional.empty();
            }
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, messageId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Error finding message by ID", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.ERROR, "Error closing connection", e);
                }
            }
        }
        return Optional.empty();
    }

    public boolean deleteMessageById(int messageId) {
        String sql = "DELETE FROM Message WHERE message_id = ?";
        Connection conn = null;
        try {
            conn = ConnectionUtil.getConnection();
            if (conn == null) {
                return false;
            }
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, messageId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Error deleting message by ID", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.ERROR, "Error closing connection", e);
                }
            }
        }
        return false;
    }

    public Optional<Message> updateMessageText(int messageId, String newText) {
        String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
        Connection conn = null;
        try {
            conn = ConnectionUtil.getConnection();
            if (conn == null) {
                return Optional.empty();
            }
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newText);
            stmt.setInt(2, messageId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return getMessageById(messageId);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Error updating message text", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.ERROR, "Error closing connection", e);
                }
            }
        }
        return Optional.empty();
    }

    public List<Message> getMessagesByUserId(int userId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message WHERE posted_by = ?";
        Connection conn = null;
        try {
            conn = ConnectionUtil.getConnection();
            if (conn == null) {
                return messages;
            }
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Error finding messages by user ID", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.ERROR, "Error closing connection", e);
                }
            }
        }
        return messages;
    }
}
