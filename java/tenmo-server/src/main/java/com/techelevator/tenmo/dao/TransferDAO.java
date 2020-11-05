package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.controller.Transfer;

public interface TransferDAO {
	
	List<Transfer> getListOfTransfers(int userId);

	Transfer getTransferDetails(int transferId);

	void transferFunds(Transfer transferRequest);

}
