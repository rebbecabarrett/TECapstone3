package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.controller.Transfer;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserDAO {

    List<User> findAll();

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);

	BigDecimal getAccountBalance(int userId);

	List<Transfer> getListOfTransfers(int userId);

	Transfer getTransferDetails(int transferId);

	void transferFunds(Transfer transferRequest);


}
