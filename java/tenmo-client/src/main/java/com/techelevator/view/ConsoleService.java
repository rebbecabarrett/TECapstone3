package com.techelevator.view;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Scanner;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;


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
		System.out.println("4.  Login as different user");
		System.out.println("5.  Exit");
		
		return in.nextLine();
	
	}
	
	public void printLoginPrompt() {
		System.out.println("Please log in");
	}
	
	public String getUserInput(String userInputField) {
		System.out.println("Please enter a " + userInputField );
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

	public void printListOfTransfers(Transfer[] listOfTransfers) {
		for (int i=0; i<listOfTransfers.length; i++ ) {
			if(listOfTransfers[i].getTransferType().contains("Send")) {
				System.out.println(listOfTransfers[i].getTransferId() + " " + listOfTransfers[i].getUsernameTo() + " $" + listOfTransfers[i].getAmount());
			}
			if(listOfTransfers[i].getTransferType().contains("Request"))
				System.out.println(listOfTransfers[i].getTransferId() + " " + listOfTransfers[i].getUsernameFrom() + " $" + listOfTransfers[i].getAmount());
		
	}
	

}

	
}


