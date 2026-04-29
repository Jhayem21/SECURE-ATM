package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLog {

    public enum Event {
        LOGIN_SUCCESS, LOGIN_FAILED, ACCOUNT_LOCKED, LOGOUT,
        DEPOSIT, WITHDRAW, BALANCE_CHECK, HISTORY_CHECK
    }

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Event         event;
    private final String        accountNumber;
    private final String        detail;
    private final LocalDateTime timestamp;

    public AuditLog(Event event, String accountNumber, String detail) {
        this.event         = event;
        this.accountNumber = accountNumber;
        this.detail        = detail;
        this.timestamp     = LocalDateTime.now();
    }

    public Event         getEvent()         { return event; }
    public String        getAccountNumber() { return accountNumber; }
    public String        getDetail()        { return detail; }
    public LocalDateTime getTimestamp()     { return timestamp; }

    @Override
    public String toString() {
        return String.format("[AUDIT] %s | Account: %s | %s | %s",
            timestamp.format(TIMESTAMP_FORMAT), accountNumber, event.name(), detail);
    }
}
