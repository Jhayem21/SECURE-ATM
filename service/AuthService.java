package service;

import model.Account;
import model.AuditLog;
import model.AuditLog.Event;
import repository.AccountRepository;

import java.util.Optional;

public class AuthService {

    private final AccountRepository accountRepository;
    private Account currentAccount;

    public AuthService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public String login(String accountNumber, String pin) {
        String inputError = validateLoginInput(accountNumber, pin);
        if (inputError != null) return inputError;

        Optional<Account> accountResult = accountRepository.getAccountByNumber(accountNumber);
        if (accountResult.isEmpty()) return "ERROR: Account not found.";

        Account account = accountResult.get();

        if (account.isLocked()) {
            writeAuditLog(account, Event.ACCOUNT_LOCKED, "Login attempt on locked account.");
            return "ERROR: Account is locked. Please contact your bank.";
        }

        if (!account.getPin().equals(pin)) {
            account.addFailedAttempt();
            writeAuditLog(account, Event.LOGIN_FAILED, "Failed attempt #" + account.getFailedAttemptCount());

            if (account.isLocked()) {
                writeAuditLog(account, Event.ACCOUNT_LOCKED, "Locked after 3 failed attempts.");
                return "ERROR: Too many failed attempts. Account is now LOCKED.";
            }

            int remainingAttempts = Account.MAX_FAILED_ATTEMPTS - account.getFailedAttemptCount();
            return String.format("ERROR: Incorrect PIN. %d attempt(s) remaining.", remainingAttempts);
        }

        account.clearFailedAttempts();
        currentAccount = account;
        writeAuditLog(account, Event.LOGIN_SUCCESS, "Authenticated successfully.");
        return "SUCCESS: Welcome, Account " + accountNumber + "!";
    }

    public String logout() {
        if (!isAuthenticated()) return "INFO: No active session.";

        String accountNumber = currentAccount.getAccountNumber();
        writeAuditLog(currentAccount, Event.LOGOUT, "User logged out.");
        currentAccount = null;
        return "SUCCESS: Account " + accountNumber + " logged out. Goodbye!";
    }

    public boolean isAuthenticated()   { return currentAccount != null; }
    public Account getCurrentAccount() { return currentAccount; }

    private String validateLoginInput(String accountNumber, String pin) {
        if (accountNumber == null || accountNumber.isBlank())
            return "ERROR: Account number must not be empty.";
        if (pin == null || pin.isBlank())
            return "ERROR: PIN must not be empty.";
        if (!pin.matches("\\d+"))
            return "ERROR: PIN must contain digits only.";
        return null;
    }

    private void writeAuditLog(Account account, Event event, String detail) {
        account.addAuditLog(new AuditLog(event, account.getAccountNumber(), detail));
    }
}
