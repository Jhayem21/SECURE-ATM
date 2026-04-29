import facade.ATMFacade;

import java.util.Scanner;

public class Main {

    private static final String RESET  = "\u001B[0m";
    private static final String BOLD   = "\u001B[1m";
    private static final String GREEN  = "\u001B[32m";
    private static final String RED    = "\u001B[31m";
    private static final String CYAN   = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";

    public static void main(String[] args) {
        ATMFacade atm     = new ATMFacade();
        Scanner   scanner = new Scanner(System.in);

        displayBanner();
        displayHelpMenu();

        while (true) {
            System.out.print(CYAN + "\natm> " + RESET);
            if (!scanner.hasNextLine()) break;

            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] tokens  = line.split("\\s+");
            String   command = tokens[0].toLowerCase();

            switch (command) {
                case "login" -> handleLogin(atm, scanner);
                case "deposit" -> {
                    double amount = parseAmountInput(tokens, scanner, "Deposit amount");
                    if (!Double.isNaN(amount)) printColored(atm.deposit(amount));
                }
                case "withdraw" -> {
                    double amount = parseAmountInput(tokens, scanner, "Withdrawal amount");
                    if (!Double.isNaN(amount)) printColored(atm.withdraw(amount));
                }
                case "balance"  -> printColored(atm.checkBalance());
                case "history"  -> {
                    String historyResult = atm.viewTransactionHistory();
                    System.out.println(historyResult.startsWith("ERROR") ? RED + historyResult + RESET : historyResult);
                }
                case "logout"   -> printColored(atm.logout());
                case "exit", "quit" -> {
                    if (atm.isAuthenticated()) System.out.println(atm.logout());
                    printMessage(GREEN, "\nThank you for using SecureATM. Goodbye!\n");
                    scanner.close();
                    return;
                }
                case "help"  -> displayHelpMenu();
                default      -> printMessage(RED, "ERROR: Unknown command '" + command + "'. Type 'help'.");
            }
        }

        scanner.close();
    }

    private static void handleLogin(ATMFacade atm, Scanner scanner) {
        if (atm.isAuthenticated()) {
            printMessage(YELLOW, "INFO: Already logged in. Use 'logout' first.");
            return;
        }

        System.out.print("  Account Number : ");
        String accountNumber = scanner.nextLine().trim();

        System.out.print("  PIN            : ");
        String pin = scanner.nextLine().trim();

        printColored(atm.login(accountNumber, pin));
    }

    private static double parseAmountInput(String[] tokens, Scanner scanner, String label) {
        String rawInput = tokens.length >= 2 ? tokens[1] : readInput(scanner, label);

        if (rawInput.isEmpty()) {
            printMessage(RED, "ERROR: Amount must not be empty.");
            return Double.NaN;
        }

        try {
            double parsedAmount = Double.parseDouble(rawInput);
            if (Double.isNaN(parsedAmount) || Double.isInfinite(parsedAmount))
                throw new NumberFormatException();
            return parsedAmount;
        } catch (NumberFormatException e) {
            printMessage(RED, "ERROR: Invalid amount '" + rawInput + "'. Enter a numeric value.");
            return Double.NaN;
        }
    }

    private static String readInput(Scanner scanner, String label) {
        System.out.print("  " + label + ": ");
        return scanner.nextLine().trim();
    }

    private static void printColored(String message) {
        if (message.startsWith("SUCCESS"))    System.out.println(GREEN  + message + RESET);
        else if (message.startsWith("ERROR")) System.out.println(RED    + message + RESET);
        else                                  System.out.println(YELLOW + message + RESET);
    }

    private static void printMessage(String color, String message) {
        System.out.println(color + message + RESET);
    }

    private static void displayBanner() {
        System.out.println(CYAN + BOLD);
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║          S E C U R E  A T M  v1.0        ║");
        System.out.println("  ║     CodeMatrix Hackathon — ATM Simulation ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.println(RESET);
    }

    private static void displayHelpMenu() {
        System.out.println(BOLD + "  Available Commands:" + RESET);
        System.out.println("  ┌─────────────────────────────────────────────────────┐");
        System.out.println("  │  login              Authenticate to your account     │");
        System.out.println("  │  deposit [amount]   Deposit money                    │");
        System.out.println("  │  withdraw [amount]  Withdraw money (limit: 5000/day) │");
        System.out.println("  │  balance            Check your current balance       │");
        System.out.println("  │  history            View transaction history          │");
        System.out.println("  │  logout             End your session                 │");
        System.out.println("  │  exit               Quit the ATM                     │");
        System.out.println("  └─────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println(YELLOW + "  Demo Accounts:" + RESET);
        System.out.println("  Account: ACC-001  PIN: 1234  Balance: 10,000.00");
        System.out.println("  Account: ACC-002  PIN: 5678  Balance:  2,500.00");
        System.out.println("  Account: ACC-003  PIN: 9999  Balance:      0.00");
    }
}
