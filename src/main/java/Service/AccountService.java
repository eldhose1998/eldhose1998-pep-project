package Service;

import DAO.AccountDAO;
import Model.Account;

import java.util.Optional;

public class AccountService {

    private final AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    /**
     * Registers a new account.
     * @param username The username for the new account.
     * @param password The password for the new account.
     * @return An Optional containing the created Account if successful, or an empty Optional if not.
     */
    public Optional<Account> registerAccount(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.length() < 4) {
            return Optional.empty();
        }

        Optional<Account> existingAccount = accountDAO.findByUsername(username);
        if (existingAccount.isPresent()) {
            return Optional.empty();
        }

        Account newAccount = new Account(username, password);
        boolean isCreated = accountDAO.createAccount(newAccount);
        return isCreated ? Optional.of(newAccount) : Optional.empty();
    }

    /**
     * Logs in a user.
     * @param username The username of the account.
     * @param password The password of the account.
     * @return An Optional containing the Account if login is successful, or an empty Optional if not.
     */
    public Optional<Account> login(String username, String password) {
        Optional<Account> account = accountDAO.findByUsername(username);
        if (account.isPresent() && account.get().getPassword().equals(password)) {
            return account;
        }
        return Optional.empty();
    }

    // Additional methods for updating, deleting accounts, etc.
}
