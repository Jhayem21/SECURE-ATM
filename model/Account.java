package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Account {

    public static final int    MAX_FAILED_ATTEMPTS    = 3;
    public static final double DAILY_WITHDRAWAL_LIMIT = 5000.0;

    private final String accountNumber;
    private final String pin;

    private double    balance;
    private boolean   isLocked;
    private int       failedAttemptCount;
    private double    dailyWithdrawnAmount;
    private LocalDate lastWithdrawalDate;

    private final List<Transaction> transactionList = new ArrayList<>();
    private final List<AuditLog>    auditLogList    = new ArrayList<>();

    public Account(String accountNumber, String pin, double initialBalance) {
        this.accountNumber = accountNumber;
        this.pin           = pin;
        this.balance       = initialBalance;
    }

    public void addBalance(double amount) {
        balance += amount;
    }

    public void deductBalance(double amount) {
        balance -= amount;
    }

    public void addFailedAttempt() {
        failedAttemptCount++;
        if (failedAttemptCount >= MAX_FAILED_ATTEMPTS) isLocked = true;
    }

    public void clearFailedAttempts() {
        failedAttemptCount = 0;
    }

    public double getRemainingDailyLimit() {
        refreshDailyLimitIfExpired();
        return DAILY_WITHDRAWAL_LIMIT - dailyWithdrawnAmount;
    }

    public void trackDailyWithdrawal(double amount) {
        refreshDailyLimitIfExpired();
        dailyWithdrawnAmount += amount;
        lastWithdrawalDate    = LocalDate.now();
    }

    private void refreshDailyLimitIfExpired() {
        if (lastWithdrawalDate == null || !lastWithdrawalDate.equals(LocalDate.now())) {
            dailyWithdrawnAmount = 0.0;
            lastWithdrawalDate   = LocalDate.now();
        }
    }

    public void addTransaction(Transaction transaction) { transactionList.add(transaction); }
    public void addAuditLog(AuditLog auditLog)          { auditLogList.add(auditLog); }

    public String            getAccountNumber()    { return accountNumber; }
    public String            getPin()              { return pin; }
    public double            getBalance()          { return balance; }
    public boolean           isLocked()            { return isLocked; }
    public int               getFailedAttemptCount(){ return failedAttemptCount; }
    public List<Transaction> getTransactionList()  { return transactionList; }
    public List<AuditLog>    getAuditLogList()     { return auditLogList; }
}
