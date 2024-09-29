package DAO;
import Model.Account;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AccountDAO {
    private Connection connection;

    public AccountDAO(){
        this.connection=ConnectionUtil.getConnection();
    }

    //checking whether the username already exist in the database.
    public boolean usernameExists(String username) {
        
        try {
            String sql = "SELECT COUNT(*) FROM accounts WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if the count is greater than 0
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Account createAccount(Account account) {
        String sql = "INSERT INTO accounts (username, password) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();
    
            // Retrieve generated keys (i.e., the new account_id)
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                account.setAccount_id(generatedKeys.getInt(1)); // Set the new account_id
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Or handle it more gracefully
        }
        return account; // Return the created account
    }
    public boolean userExists(int userId) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE account_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next() && rs.getInt(1) > 0; // Returns true if the count is greater than 0
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of an exception
        }
    }

    public Account validateLogin(String username, String password) {
        String sql = "SELECT * FROM accounts WHERE username = ? AND password = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return new Account(rs.getInt("account_id"),
                                   rs.getString("username"),
                                   rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if login failed
    }



}
