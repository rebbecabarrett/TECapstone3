package com.techelevator.tenmo;


import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.view.ConsoleService;

public class App {

	private static final String API_BASE_URL = "http://localhost:8080/";
	private static final String REGISTER = "1";
	private static final String LOGIN = "2";


	// we only want single instances of these. Be careful to not create multiple instances in your code. We just reuse what we have
	private AuthenticatedUser currentUser;
	private ConsoleService console;
	private AuthenticationService authenticationService;

	public static void main(String[] args) {
		App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
		app.run();
	}

/**
 *  This constructor is passed the services from main() above
 * @param console  (the console service)
 * @param authenticationService
 * @param studentService
 */
	public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
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
          
		String choice = console.printMainMenu(currentUser);

		
	}


    /*
     * This method determines if we cab=n break out of the registerAndLogin() loop above. If we
     * have a currentUser, then we know that a successful login occurred and we got back a JWT token
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
