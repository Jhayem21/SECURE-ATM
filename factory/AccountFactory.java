package factory;

import model.Account;

public class AccountFactory {

    private AccountFactory() {}

    public static Account createAccount(String accountNumber, String pin, double initialBalance) {
        validateAccountNumber(accountNumber);
        validatePin(pin);
        validateInitialBalance(initialBalance);
        return new Account(accountNumber, pin, initialBalance);
    }

    public static Account createAccount(String accountNumber, String pin) {
        return createAccount(accountNumber, pin, 0.0);
    }

    private static void validateAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank())
            throw new IllegalArgumentException("Account number must not be blank.");
    }

    private static void validatePin(String pin) {
        if (pin == null || pin.length() < 4)
            throw new IllegalArgumentException("PIN must be at least 4 digits.");
        if (!pin.matches("\\d+"))
            throw new IllegalArgumentException("PIN must contain digits only.");
    }

    private static void validateInitialBalance(double initialBalance) {
        if (initialBalance < 0)
            throw new IllegalArgumentException("Initial balance cannot be negative.");
    }
}
