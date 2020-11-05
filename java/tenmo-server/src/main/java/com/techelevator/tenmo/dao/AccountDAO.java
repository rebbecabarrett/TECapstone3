package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDAO {

	BigDecimal getAccountBalance(int userId);

	void addMoneyToAccount(int userIdTo, BigDecimal amount);

	void withdrawMoneyFromAccount(int userIdFrom, BigDecimal amount);
	
	int getAccountIdFromUserId(int userId);
}
