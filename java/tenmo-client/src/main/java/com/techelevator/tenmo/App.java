package com.techelevator.tenmo;

import java.math.BigDecimal;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.view.ConsoleService;

public class App {

	private static final String API_BASE_URL = "http://localhost:8080/";
	private static final String REGISTER = "1";
	private static final String LOGIN = "2";

	private static final String GET_ACCOUNT_BALANCE = "1";
	private static final String SEND_TE_BUCKS = "2";
	private static final String VIEW_PAST_TRANSFERS = "3";
	private static final String LOGIN_AS_DIFFERENT_USER = "4";
	private static final String PROGRAM_EXIT = "5";

	// we only want single instances of these. Be careful to not create multiple
	// instances in your code. We just reuse what we have
	private AuthenticatedUser currentUser;
	private ConsoleService console;
	private AuthenticationService authenticationService;
	private AccountService accountService;
	private TransferService transferService;
	private UserService userService;

	public static void main(String[] args) {
		App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL),
				new AccountService(), new TransferService(), new UserService());
		app.run();
	}

	/**
	 * This constructor is passed the services from main() above
	 * 
	 * @param console               (the console service)
	 * @param authenticationService
	 * @param studentService
	 */
	public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService,
			TransferService transferService, UserService userService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
		this.transferService = transferService;
		this.userService = userService;
	}

	public void run() {

		registerAndLogin();
		mainMenu();

	}

	private void registerAndLogin() {
		while (!isAuthenticated()) {
			String choice = console.printLoginMenu();

			switch (choice) {
			case LOGIN:
				login();
				break;
			case REGISTER:
				register();
				break;
			default:
				System.out.println("Invalid Choice. Please try again!");

			}
		}
	}

	private void mainMenu() {
          
		while (true) {
			String choice = console.printMainMenu(currentUser);

			switch (choice) {

			case GET_ACCOUNT_BALANCE:
				System.out.println("Retrieving account balance ...");
				//call a local/client-side service class to go fetch the list of students
				BigDecimal accountBalance = accountService.getAccountBalance();
				console.printAccountBalance(accountBalance);
				
				break;
			case SEND_TE_BUCKS:
				User[] listOfUsers = userService.getListOfUsers();
				console.printListOfUsers(listOfUsers, currentUser);
				Transfer requestTransfer = console.sendDetailsSubMenuHandler(currentUser);
				Transfer returnedTransfer = transferService.transferFunds(requestTransfer);
				console.printTransferDetails(returnedTransfer);
				break;
				
			case VIEW_PAST_TRANSFERS: 
				System.out.println("Retrieving list of transfers ...");
				Transfer[] listOfTransfers = transferService.getListOfTransfers();
				int transferId = Integer.parseInt(console.printListOfTransfers(listOfTransfers, currentUser));
				boolean finished = false;
				while (!finished) {
				if (transferId == 0) {
					console.printMainMenu(currentUser);
					finished = true;
				}
				for (Transfer t: listOfTransfers) {
					if (t.getTransferId() == transferId) {
					console.printTransferDetails(t);
					finished = true;
					}
			
				}
				}
		
				break;
			case LOGIN_AS_DIFFERENT_USER:
				
				break;
			case PROGRAM_EXIT:
				System.out.println("Exiting... Good Bye!");
				System.exit(1);
			default:
				System.out.println("Invalid Choice. Please try again!");

			}
		}
		
	}

	/*
	 * This method determines if we cab=n break out of the registerAndLogin() loop
	 * above. If we have a currentUser, then we know that a successful login
	 * occurred and we got back a JWT token
	 */
	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {

		boolean isRegistered = false;
		while (!isRegistered) // will keep looping until user is registered
		{
			UserCredentials credentials = registerUserCredentials();
			try {
				authenticationService.register(credentials);
				isRegistered = true;
				console.registrationSuccessful();
			} catch (AuthenticationServiceException e) {
				System.out.println("REGISTRATION ERROR: " + e.getMessage());
				System.out.println("Please attempt to register again.");
			}
		}
	}

	private void login() {

		currentUser = null;
		while (currentUser == null) // will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				// this is what calls the server to retrieve a JWT token (if successful)
				currentUser = authenticationService.login(credentials);
				accountService.setAUTH_TOKEN(currentUser.getToken());
				transferService.setAUTH_TOKEN(currentUser.getToken());
				userService.setAUTH_TOKEN(currentUser.getToken());

			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: " + e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}

	private UserCredentials collectUserCredentials() {
		console.printLoginPrompt();
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}

	private UserCredentials registerUserCredentials() {
		console.printRegisterNewUserPrompt();
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
