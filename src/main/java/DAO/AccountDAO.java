package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.Optional;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

public class AccountDAO {

    private static final Logger LOGGER = System.getLogger(AccountDAO.class.getName());

    /**
     * Finds an account by its username.
     * @param username The username of the account to find.
     * @return An Optional containing the Account if found, or an empty Optional if not.
     */
    public Optional<Account> findByUsername(String username) {
        String sql = "SELECT * FROM Account WHERE username = ?";
        Connection conn = null;
        try {
            conn = ConnectionUtil.getConnection();
            if (conn == null) {
                return Optional.empty();
            }
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Error finding account by username", e);
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

    /**
     * Creates a new account in the database.
     * @param account The Account object to create.
     * @return True if the account was successfully created, false otherwise.
     */
    public boolean createAccount(Account account) {
        String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";
        Connection conn = null;
        try {
            conn = ConnectionUtil.getConnection();
            if (conn == null) {
                return false;
            }
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    account.setAccount_id(generatedKeys.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Error creating account", e);
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
}
