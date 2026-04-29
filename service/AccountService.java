package service;

import model.Account;
import model.AuditLog;
import model.AuditLog.Event;
import model.Transaction;
import model.Transaction.Type;

import java.util.List;

public class AccountService {

    public String depositAmount(Account account, double amount) {
        String validationError = validatePositiveAmount(amount, "Deposit");
        if (validationError != null) return validationError;

        account.addBalance(amount);
        account.addTransaction(new Transaction(Type.DEPOSIT, amount, account.getBalance()));
        writeAuditLog(account, Event.DEPOSIT,
            String.format("Deposited %.2f | Balance: %.2f", amount, account.getBalance()));

        return String.format("SUCCESS: Deposited %.2f | New Balance: %.2f",
            amount, account.getBalance());
    }

    public String withdrawAmount(Account account, double amount) {
        String validationError = validatePositiveAmount(amount, "Withdrawal");
        if (validationError != null) return validationError;

        if (amount > account.getBalance())
            return String.format("ERROR: Insufficient balance. Available: %.2f", account.getBalance());

        double remainingDailyLimit = account.getRemainingDailyLimit();
        if (amount > remainingDailyLimit)
            return String.format("ERROR: Exceeds daily withdrawal limit. Remaining: %.2f", remainingDailyLimit);

        account.deductBalance(amount);
        account.trackDailyWithdrawal(amount);
        account.addTransaction(new Transaction(Type.WITHDRAW, amount, account.getBalance()));
        writeAuditLog(account, Event.WITHDRAW,
            String.format("Withdrew %.2f | Balance: %.2f | Daily Remaining: %.2f",
                amount, account.getBalance(), account.getRemainingDailyLimit()));

        return String.format("SUCCESS: Withdrew %.2f | New Balance: %.2f | Daily Limit Remaining: %.2f",
            amount, account.getBalance(), account.getRemainingDailyLimit());
    }

    public String getBalance(Account account) {
        writeAuditLog(account, Event.BALANCE_CHECK, "Balance checked.");
        return String.format("Current Balance: %.2f", account.getBalance());
    }

    public String getTransactionHistory(Account account) {
        writeAuditLog(account, Event.HISTORY_CHECK, "Transaction history viewed.");

        List<Transaction> transactionList = account.getTransactionList();
        if (transactionList.isEmpty()) return "INFO: No transactions on record.";

        StringBuilder historyBuilder = new StringBuilder();
        historyBuilder.append(String.format("%n  %-8s  %-14s  %-16s  %-20s%n",
            "TYPE", "AMOUNT", "BALANCE AFTER", "TIMESTAMP"));
        historyBuilder.append("  ").append("-".repeat(68)).append("\n");
        transactionList.forEach(transaction -> historyBuilder.append(transaction).append("\n"));
        historyBuilder.append("  ").append("-".repeat(68));
        historyBuilder.append(String.format("%n  Total transactions: %d", transactionList.size()));

        return historyBuilder.toString();
    }

    private String validatePositiveAmount(double amount, String label) {
        if (amount <= 0)
            return String.format("ERROR: %s amount must be greater than 0.", label);
        if (Double.isNaN(amount) || Double.isInfinite(amount))
            return String.format("ERROR: %s amount is invalid.", label);
        return null;
    }

    private void writeAuditLog(Account account, Event event, String detail) {
        account.addAuditLog(new AuditLog(event, account.getAccountNumber(), detail));
    }
}
