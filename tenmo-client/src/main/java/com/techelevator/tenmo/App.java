package com.techelevator.tenmo;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TenmoService;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final TenmoService tenmoService = new TenmoService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        //BigDecimal balance = tenmoService.getBalance(currentUser.getUser().getId());
        BigDecimal balance = tenmoService.getBalance(currentUser);
        System.out.println("Current balance: " + balance);
	}

	private void viewTransferHistory() {
		Transfer[] transfers = tenmoService.allTransfers(currentUser);
        for (Transfer transfer : transfers) {
            System.out.println("Transfer ID: " + transfer.getTransferId());
        }

        if(transfers.length != 0) {
            String details = consoleService.promptForString("Would you like to see details for a transfer (Y/N)? ");
            if(details.equals("Y")) {
                int transferId = consoleService.promptForInt("Please enter a transfer id: ");
                for (Transfer transfer : transfers) {
                    if(transfer.getTransferId() == transferId) {
                        int status = transfer.getTransferStatusId();
                        System.out.println("Sent from account: " + transfer.getAccountFrom());
                        System.out.println("Received at account: " +  transfer.getAccountTo());
                        System.out.println("Amount: " + transfer.getAmount());
                        System.out.println(status == 2 ? "Approved" : "Rejected");
                    }
                }
            }
        } else {
            System.out.println("No transfers");
        }
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub

	}

	private void sendBucks() {
        Account[] accounts = tenmoService.allAccounts(currentUser);
        for (Account account : accounts) {
            System.out.println(account.getUserId());
        }
        System.out.println(currentUser.getUser().getId());

        int userId = consoleService.promptForInt("Please pick a user to send to: ");
        BigDecimal amount = consoleService.promptForBigDecimal("How much do you want to send? ");

        Transfer transfer = new Transfer();
        transfer.setAmount(amount);
        transfer.setAccountFrom((int)(long)currentUser.getUser().getId());
        transfer.setAccountTo(userId);

		tenmoService.createTransfer(transfer, currentUser);
	}

	private void requestBucks() {
		// TODO Auto-generated method stub

	}

}
