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

}
