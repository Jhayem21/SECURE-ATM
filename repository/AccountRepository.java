package repository;

import factory.AccountFactory;
import model.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AccountRepository {

    private final Map<String, Account> accountStore = new HashMap<>();

    public AccountRepository() {
        loadDefaultAccounts();
    }

    public void saveAccount(Account account) {
        accountStore.put(account.getAccountNumber(), account);
    }

    public Optional<Account> getAccountByNumber(String accountNumber) {
        return Optional.ofNullable(accountStore.get(accountNumber));
    }

    public boolean hasAccount(String accountNumber) {
        return accountStore.containsKey(accountNumber);
    }

    private void loadDefaultAccounts() {
        saveAccount(AccountFactory.createAccount("ACC-001", "1234", 10_000.00));
        saveAccount(AccountFactory.createAccount("ACC-002", "5678",  2_500.00));
        saveAccount(AccountFactory.createAccount("ACC-003", "9999",      0.00));
    }
}
