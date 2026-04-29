package facade;

import repository.AccountRepository;
import service.AccountService;
import service.AuthService;

public class ATMFacade {

    private final AuthService    authService;
    private final AccountService accountService;

    public ATMFacade() {
        AccountRepository accountRepository = new AccountRepository();
        this.authService    = new AuthService(accountRepository);
        this.accountService = new AccountService();
    }

    public String login(String accountNumber, String pin) {
        return authService.login(accountNumber, pin);
    }

    public String logout() {
        return authService.logout();
    }

    public String deposit(double amount) {
        if (!isAuthenticated()) return buildUnauthenticatedError();
        return accountService.depositAmount(authService.getCurrentAccount(), amount);
    }

    public String withdraw(double amount) {
        if (!isAuthenticated()) return buildUnauthenticatedError();
        return accountService.withdrawAmount(authService.getCurrentAccount(), amount);
    }

    public String checkBalance() {
        if (!isAuthenticated()) return buildUnauthenticatedError();
        return accountService.getBalance(authService.getCurrentAccount());
    }

    public String viewTransactionHistory() {
        if (!isAuthenticated()) return buildUnauthenticatedError();
        return accountService.getTransactionHistory(authService.getCurrentAccount());
    }

    public boolean isAuthenticated() {
        return authService.isAuthenticated();
    }

    private String buildUnauthenticatedError() {
        return "ERROR: You must be logged in. Use: login";
    }
}
