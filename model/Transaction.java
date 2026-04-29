package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    public enum Type { DEPOSIT, WITHDRAW }

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Type          type;
    private final double        amount;
    private final LocalDateTime timestamp;
    private final double        remainingBalance;

    public Transaction(Type type, double amount, double remainingBalance) {
        this.type             = type;
        this.amount           = amount;
        this.remainingBalance = remainingBalance;
        this.timestamp        = LocalDateTime.now();
    }

    public Type          getType()             { return type; }
    public double        getAmount()           { return amount; }
    public LocalDateTime getTimestamp()        { return timestamp; }
    public double        getRemainingBalance() { return remainingBalance; }

    @Override
    public String toString() {
        return String.format("  [%-8s]  Amount: %10.2f  |  Balance After: %10.2f  |  %s",
            type.name(), amount, remainingBalance, timestamp.format(TIMESTAMP_FORMAT));
    }
}
