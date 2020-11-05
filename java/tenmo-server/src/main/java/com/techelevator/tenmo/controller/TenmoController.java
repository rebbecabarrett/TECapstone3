package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.User;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping(path = "/api")

public class TenmoController {
	
	private UserDAO userDAO;
	private TransferDAO transferDAO;
	private AccountDAO accountDAO;
	
	public TenmoController (UserDAO userDAO, TransferDAO transferDAO, AccountDAO accountDAO) {
		this.userDAO = userDAO;
		this.transferDAO = transferDAO;
		this.accountDAO = accountDAO;
	}
	
	@RequestMapping(path = "/accounts", method = RequestMethod.GET)
	public BigDecimal getUserAccountBalance(@Valid Principal principal) {
		int userId = userDAO.findIdByUsername(principal.getName());
		return accountDAO.getAccountBalance(userId);
	
	}
	
	@RequestMapping (path = "/transfers", method = RequestMethod.GET)
	public List<Transfer> getListOfTransfers(@Valid Principal principal) {
		
		int userId = userDAO.findIdByUsername(principal.getName());
		List<Transfer> listOfTransfers = null;
		
		listOfTransfers = transferDAO.getListOfTransfers(userId);
	
		for (Transfer t: listOfTransfers) {
			t.setUsernameFrom(userDAO.getUsernameFromAccountId(t.getAccount_from()));
			t.setUsernameTo(userDAO.getUsernameFromAccountId(t.getAccount_to()));
		}
		
		return listOfTransfers;
	}
	
	@RequestMapping (path = "/users", method = RequestMethod.GET)
	public List<User> getAllUsers() {
		return userDAO.findAll();
	}
	
	@RequestMapping (path = "/transfers/{transferId}", method = RequestMethod.GET)
	public Transfer getTransferDetails(@PathVariable int transferId) {
		Transfer transferDetails = transferDAO.getTransferDetails(transferId);
		return transferDetails;
	}
	
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping (path = "/transfers", method = RequestMethod.POST)
	public void transferFunds(@RequestBody Transfer transferRequest, Principal principal) {
		int userId = userDAO.findIdByUsername(principal.getName());
		transferRequest.setUserIdFrom(userId);
		BigDecimal accountBalanceOfSender = accountDAO.getAccountBalance(userId);
		if (accountBalanceOfSender.compareTo(transferRequest.getAmount()) ==1) {
			accountDAO.addMoneyToAccount(transferRequest.getUserIdTo(), transferRequest.getAmount());
			accountDAO.withdrawMoneyFromAccount(transferRequest.getUserIdFrom(), transferRequest.getAmount());
		}
		transferDAO.transferFunds(transferRequest);
	}
	
	
	
	

}
