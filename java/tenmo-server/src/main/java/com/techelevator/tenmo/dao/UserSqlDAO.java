package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.controller.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserSqlDAO implements UserDAO {

    private static final double STARTING_BALANCE = 1000;
    private JdbcTemplate jdbcTemplate;

    public UserSqlDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int findIdByUsername(String username) {
        return jdbcTemplate.queryForObject("select user_id from users where username = ?", int.class, username);
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "select * from users";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            User user = mapRowToUser(results);
            users.add(user);
        }

        return users;
    }

    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        for (User user : this.findAll()) {
            if( user.getUsername().toLowerCase().equals(username.toLowerCase())) {
                return user;
            }
        }
        throw new UsernameNotFoundException("User " + username + " was not found.");
    }

    @Override
    public boolean create(String username, String password) {
        boolean userCreated = false;
        boolean accountCreated = false;

        // create user
        String insertUser = "insert into users (username,password_hash) values(?,?)";
        String password_hash = new BCryptPasswordEncoder().encode(password);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String id_column = "user_id";
        userCreated = jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(insertUser, new String[]{id_column});
                    ps.setString(1, username);
                    ps.setString(2,password_hash);
                    return ps;
                }
                , keyHolder) == 1;
        int newUserId = (int) keyHolder.getKeys().get(id_column);

        // create account
        String insertAccount = "insert into accounts (user_id,balance) values(?,?)";
        accountCreated = jdbcTemplate.update(insertAccount,newUserId,STARTING_BALANCE) == 1;

        return userCreated && accountCreated;
    }

    

	@Override
	public BigDecimal getAccountBalance(int userId) {
		BigDecimal response = jdbcTemplate.queryForObject("select accounts.balance from accounts where accounts.user_id = ?", BigDecimal.class, userId);
		return response;
	}

	@Override
	public List<Transfer> getListOfTransfers(int userId) {
		List<Transfer> listAllTransfers = new ArrayList<Transfer>();
		Transfer request = null;
		SqlRowSet requestList = jdbcTemplate.queryForRowSet("SELECT transfer_id, account_from, account_to, transfer_types.transfer_type_desc, transfer_statuses.transfer_status_desc, amount, users.username"
					+ " FROM transfers"
					+ " JOIN accounts ON accounts.account_id = transfers.account_from"
					+ " JOIN users ON users.user_id = accounts.user_id"
					+ " JOIN transfer_types ON transfer_types.transfer_type_id = transfers.transfer_type_id"
					+ " JOIN transfer_statuses ON transfer_statuses.transfer_status_id = transfers.transfer_status_id"
					+ " WHERE users.user_id = ? AND transfer_types.transfer_type_desc = 'Request'");
		
		while(requestList.next()) {
			request = mapRowToTransfer(requestList);
			listAllTransfers.add(request);
		}
		Transfer send = null;
		SqlRowSet sendList = jdbcTemplate.queryForRowSet("SELECT transfer_id, account_from, account_to, transfer_types.transfer_type_desc, transfer_statuses.transfer_status_desc, amount, users.username"
				+ " FROM transfers"
				+ " JOIN accounts ON accounts.account_id = transfers.account_to"
				+ " JOIN users ON users.user_id = accounts.user_id"
				+ " JOIN transfer_types ON transfer_types.transfer_type_id = transfers.transfer_type_id"
				+ " JOIN transfer_statuses ON transfer_statuses.transfer_status_id = transfers.transfer_status_id"
				+ " WHERE users.user_id = ? AND transfer_types.transfer_type_desc = 'Send'");
		
		return listAllTransfers;
		
		
	}
	
	@Override
	public Transfer getTransferDetails(int transferId) {
		Transfer transferDetails = null;
		SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT transfer_id, account_from, account_to, transfer_types.transfer_type_desc, transfer_statuses.transfer_status_desc, amount"
				+ " FROM transfers"
				+ " JOIN accounts ON accounts.account_id = transfers.account_from"
				+ " JOIN users ON users.user_id = accounts.user_id"
				+ " JOIN transfer_types ON transfer_types.transfer_type_id = transfers.transfer_type_id"
				+ " JOIN transfer_statuses ON transfer_statuses.transfer_status_id = transfers.transfer_status_id"
				+ " WHERE transfers.transfer_id = ?", transferId);
		if(response.next()) {
			transferDetails = mapRowToTransfer(response);
	
		}
	return transferDetails;
	}	
	
	@Override
	public void transferFunds(Transfer transferRequest) {
		// TODO Auto-generated method stub
		
	}
	
	public String getUsernameFromAccountId(int accountId) {
		SqlRowSet response = jdbcTemplate.queryForRowSet("select users.username JOIN accounts ON accounts.user_id = users.user_id where accounts.account_id = ?", accountId);
		if (response.next()) {
			
			String username = response.getString(0);
		
			return username;
		
	}
		return null;
	}
	
	
	private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities("ROLE_USER");
        return user;
    }

	private Transfer mapRowToTransfer(SqlRowSet rs) {
		Transfer transfer = new Transfer();
		transfer.setTransferId(rs.getInt("transfer_id"));
		transfer.setAccount_from(rs.getInt("account_from"));
		transfer.setAccount_to(rs.getInt("account_to"));
		transfer.setTransferType(rs.getString("transfer_type_desc"));
		transfer.setTransferStatus(rs.getString("transfer_status_desc"));
		transfer.setAmount(rs.getBigDecimal("amount"));
		transfer.setUsername(rs.getString("username"));
		return transfer;
	}
	



}
