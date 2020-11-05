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
public class AccountSqlDAO implements AccountDAO {
	
	private JdbcTemplate jdbcTemplate;

	public AccountSqlDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public BigDecimal getAccountBalance(int userId) {
		BigDecimal response = jdbcTemplate.queryForObject(
				"select accounts.balance from accounts where accounts.user_id = ?", BigDecimal.class, userId);
		return response;
	}
	
	@Override
	public void addMoneyToAccount(int userIdTo, BigDecimal amountRequest) {
		String sql = "update accounts set balance = balance  + ? WHERE user_id = ?";
		jdbcTemplate.update(sql, amountRequest, userIdTo);
	}
	
	@Override
	public void withdrawMoneyFromAccount(int userIdFrom, BigDecimal amountRequest) {
		String sql = "update accounts set balance = balance  - ? WHERE user_id = ?";
		jdbcTemplate.update(sql, amountRequest, userIdFrom);
	}

	@Override
	public int getAccountIdFromUserId(int userId) {
		int response = jdbcTemplate.queryForObject("SELECT accounts.account_id FROM accounts JOIN transfers ON transfers.account_to = accounts.account_id OR transfers.account_from = accounts.account_id WHERE accounts.user_id = ?", int.class, userId);
	return response;
	}
	
}
