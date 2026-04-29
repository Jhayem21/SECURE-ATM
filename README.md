# SecureATM CLI — CodeMatrix Hackathon

## How to Run
```bash
javac -d out -sourcepath . Main.java
java -cp out Main
```

## Demo Accounts
| Account  | PIN  | Balance     |
|----------|------|-------------|
| ACC-001  | 1234 | ₱10,000.00  |
| ACC-002  | 5678 |  ₱2,500.00  |
| ACC-003  | 9999 |      ₱0.00  |

## Architecture
```
Main → ATMFacade → AuthService / AccountService → AccountRepository → Account
```

## Design Patterns
| Pattern    | File                    |
|------------|-------------------------|
| Facade     | ATMFacade.java          |
| Factory    | AccountFactory.java      |
| Repository | AccountRepository.java   |

## Naming Conventions Applied
- Methods   : camelCase, verb + noun  (depositAmount, withdrawAmount, getBalance)
- Variables : camelCase, descriptive  (accountStore, transactionList, rawInput)
- Booleans  : is / has / can prefix   (isLocked, isAuthenticated, hasAccount)
- Classes   : PascalCase              (AccountService, AuthService, ATMFacade)
- Constants : UPPER_CASE              (MAX_FAILED_ATTEMPTS, DAILY_WITHDRAWAL_LIMIT)
