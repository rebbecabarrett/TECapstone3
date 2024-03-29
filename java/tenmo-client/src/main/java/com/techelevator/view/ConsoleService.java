package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Scanner;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;
	public String AUTH_TOKEN = "";

	public ConsoleService(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
	}

	public String printLoginMenu() {

		System.out.println("**********************************************");
		System.out.println("*            Welcome to Tenmo                *");
		System.out.println("**********************************************");
		System.out.println("\n");
		System.out.println("Please select from the following choices to get started:\n");
		System.out.println("1.  Register");
		System.out.println("2.  Login");

		return in.nextLine();

	}

	public String printMainMenu(AuthenticatedUser currentUser) {

		System.out.println("\n");
		System.out.println("Please select from the following choices to get started:\n");
		System.out.println("1.  View your current balance");
		System.out.println("2.  Send TE bucks");
		System.out.println("3.  View your past transfers");
		System.out.println("4.  Exit");

		return in.nextLine();

	}

	public void printLoginPrompt() {
		System.out.println("Please log in");
	}

	public String getUserInput(String userInputField) {
		System.out.println("Please enter a " + userInputField);
		return in.nextLine();
	}

	public void printRegisterNewUserPrompt() {
		System.out.println("Please register a new user:");
	}

	public void registrationSuccessful() {
		System.out.println("Registration successful. You can now login.");
	}

	public void printAccountBalance(BigDecimal accountBalance) {
		System.out.println("Your current account balance is: $" + accountBalance);

	}

	public String printListOfTransfers(Transfer[] listOfTransfers, AuthenticatedUser currentUser) {
		String currentUsername = currentUser.getUser().getUsername();
		System.out.println("-------------------------------------------------------------------");
		System.out.printf("%-20s %-20s      %-20s\n", "Transfer ID", "From/To", "Amount");
		System.out.println("-------------------------------------------------------------------");
		for (int i = 0; i < listOfTransfers.length; i++) {
			if (listOfTransfers[i].getUsernameFrom().contains(currentUsername))
				System.out.printf("%-20s To:   %-20s $%-5s\n", listOfTransfers[i].getTransferId(),
						listOfTransfers[i].getUsernameTo(), listOfTransfers[i].getAmount());
			else if (listOfTransfers[i].getUsernameTo().contains(currentUsername)) {
				System.out.printf("%-20s From: %-20s $%-5s\n", listOfTransfers[i].getTransferId(),
						listOfTransfers[i].getUsernameFrom(), listOfTransfers[i].getAmount());
			}

		}
		boolean valid = false;
		String transferId = "";
		while (!valid) {
			System.out.println("\n");
			System.out.println("Please enter transfer id to view details (or press 0 to cancel):\n");
			transferId = in.nextLine();

			if (validateTransferIdInput(transferId, listOfTransfers)) {
				valid = true;

			} else {

			}
		}
		return transferId;
	}

	private boolean validateTransferIdInput(String transferId, Transfer[] listOfTransfers) {
		int input;

		try {
			input = Integer.parseInt(transferId);
		} catch (Exception e) {
			return false;
		}

		if (input == 0) {
			return true;
		}

		for (Transfer t : listOfTransfers) {
			if (t.getTransferId() == input) {

				return true;
			}

		}
		return false;
	}

	private boolean validateIntegerInput(String stringInput) {
		int input;
		try {
			input = Integer.parseInt(stringInput);
		} catch (Exception e) {
			return false;
		}
		if (input > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void printTransferDetails(Transfer transferDetails) {
		System.out.println("Id: " + transferDetails.getTransferId());
		System.out.println("Account From: " + transferDetails.getUsernameFrom());
		System.out.println("Account To: " + transferDetails.getUsernameTo());
		System.out.println("Type: " + transferDetails.getTransferType());
		System.out.println("Status: " + transferDetails.getTransferStatus());
		System.out.println("Amount: $" + transferDetails.getAmount());

	}

	public Transfer sendDetailsSubMenuHandler(AuthenticatedUser currentUser, int response) {
		Transfer requestTransfer = new Transfer();
		
		requestTransfer.setUserIdTo(response);
		
		boolean validDollarAmount = false;

		while (!validDollarAmount) {
			System.out.println("Enter Amount:\n");
			String amountAsString = in.nextLine();
			if (validateIntegerInput(amountAsString)) {
				BigDecimal amount = new BigDecimal(amountAsString);
				requestTransfer.setAmount(amount);
				validDollarAmount = true;
			} else {
			}
		}

		return requestTransfer;
	}

	private boolean validateUserIdInput(String userInput, User[] listOfUsers) {
		int input;

		try {
			input = Integer.parseInt(userInput);
		} catch (Exception e) {
			return false;
		}

		if (input == 0) {
			return true;
		}

		for (User u : listOfUsers) {
			if (u.getId() == input) {

				return true;
			}

		}
		return false;

	}

	public void printListOfUsers(User[] listOfUsers, AuthenticatedUser currentUser) {
		String currentUsername = currentUser.getUser().getUsername();
		System.out.println("------------------------------------------");
		System.out.printf("%-20s %-20s\n", "Users ID", "Name");
		System.out.println("------------------------------------------");
		for (int i = 0; i < listOfUsers.length; i++) {
			if (listOfUsers[i].getUsername().contains(currentUsername)) {

			}

			else {
				System.out.printf("%-20s %-20s\n", listOfUsers[i].getId(), listOfUsers[i].getUsername());
			}

		}

	}

	public int getUserIdChoice(AuthenticatedUser currentUser, User[] listOfUsers) {
		boolean valid = false;

		while (!valid) {
			System.out.println("\n");
			System.out.println("Enter ID of user you are sending to (Press 0 to cancel):\n");
			String userIdAsString = in.nextLine();
			
			if (Integer.parseInt(userIdAsString)==0) {
				valid = true;
				return Integer.parseInt(userIdAsString);
			}
			else if (validateUserIdInput(userIdAsString, listOfUsers)) {
				int userIdTo = Integer.parseInt(userIdAsString);
				valid = true;
				return userIdTo;

			} 
			 else {

			}
		}
		return 0;
	}

}
