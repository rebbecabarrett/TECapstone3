package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.controller.Transfer;

public class TransferSqlDAO implements TransferDAO {
	
	private JdbcTemplate jdbcTemplate;

	public TransferSqlDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Transfer> getListOfTransfers(int userId) {
		List<Transfer> listAllTransfers = new ArrayList<Transfer>();
		Transfer transfer = null;

		SqlRowSet transferList = jdbcTemplate.queryForRowSet("SELECT transfer_id, account_from, account_to, transfer_types.transfer_type_desc, transfer_statuses.transfer_status_desc, amount "
				+ "FROM transfers "
				+ "JOIN transfer_types ON transfer_types.transfer_type_id = transfers.transfer_type_id "
				+ "JOIN transfer_statuses ON transfer_statuses.transfer_status_id = transfers.transfer_status_id "
				+ "JOIN accounts ON transfers.account_to = accounts.account_id OR transfers.account_from = accounts.account_id "
				+ "WHERE accounts.user_id = ?;", userId);
				

		while (transferList.next()) {

			transfer = mapRowToTransfer(transferList);
			listAllTransfers.add(transfer);
		}

		return listAllTransfers;
	}

	@Override
	public Transfer getTransferDetails(int transferId) {
		
		Transfer transferDetails = null;
	
		SqlRowSet response = jdbcTemplate.queryForRowSet(
			"SELECT transfer_id, account_from, account_to, transfer_types.transfer_type_desc, transfer_statuses.transfer_status_desc, amount"
					+ " FROM transfers" + " JOIN accounts ON accounts.account_id = transfers.account_from"
					+ " JOIN users ON users.user_id = accounts.user_id"
					+ " JOIN transfer_types ON transfer_types.transfer_type_id = transfers.transfer_type_id"
					+ " JOIN transfer_statuses ON transfer_statuses.transfer_status_id = transfers.transfer_status_id"
					+ " WHERE transfers.transfer_id = ?",
			transferId);
		if (response.next()) {
			transferDetails = mapRowToTransfer(response);

	}
	return transferDetails;}

	@Override
	public void transferFunds(Transfer transferRequest) {
		// TODO Auto-generated method stub

	}
	
	private Transfer mapRowToTransfer(SqlRowSet rs) {
		Transfer transfer = new Transfer();
		transfer.setTransferId(rs.getInt("transfer_id"));
		transfer.setAccount_from(rs.getInt("account_from"));
		transfer.setAccount_to(rs.getInt("account_to"));
		transfer.setTransferType(rs.getString("transfer_type_desc"));
		transfer.setTransferStatus(rs.getString("transfer_status_desc"));
		transfer.setAmount(rs.getBigDecimal("amount"));
		return transfer;
	}

}
