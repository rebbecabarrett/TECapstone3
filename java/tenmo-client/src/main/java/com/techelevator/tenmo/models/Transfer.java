package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transfer {

	private int transferId;
	private int account_from;
	private int userIdFrom;
	private String usernameFrom;
	private int account_to;
	private int userIdTo;
	private String usernameTo;
	private BigDecimal amount;
	private String transferType;
	private String transferStatus;

	public int getUserIdFrom() {
		return userIdFrom;
	}

	public void setUserIdFrom(int userIdFrom) {
		this.userIdFrom = userIdFrom;
	}

	public int getUserIdTo() {
		return userIdTo;
	}

	public void setUserIdTo(int userIdTo) {
		this.userIdTo = userIdTo;
	}

	public String getUsernameTo() {
		return usernameTo;
	}

	public void setUsernameTo(String usernameTo) {
		this.usernameTo = usernameTo;
	}

	public String getUsernameFrom() {
		return usernameFrom;
	}

	public void setUsernameFrom(String usernameFrom) {
		this.usernameFrom = usernameFrom;
	}

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	public String getTransferStatus() {
		return transferStatus;
	}

	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}

	public int getTransferId() {
		return transferId;
	}

	public void setTransferId(int transferId) {
		this.transferId = transferId;
	}

	public int getAccount_from() {
		return account_from;
	}

	public void setAccount_from(int account_from) {
		this.account_from = account_from;
	}

	public int getAccount_to() {
		return account_to;
	}

	public void setAccount_to(int account_to) {
		this.account_to = account_to;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
