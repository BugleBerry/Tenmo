package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;

public class JdbcAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance(int accountId) {
        String query = "SELECT balance FROM account WHERE account_id = ?;";
        BigDecimal balance = null;
        SqlRowSet results = jdbcTemplate.queryForRowSet(query, accountId);

        if(results.next()) {
            balance = results.getBigDecimal("balance");
        }

        return balance;
    }

    @Override
    public Account getAccountById(int accountId) {
        String query = "SELECT * FROM account WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(query, accountId);
        Account account = null;

        if(results.next()) {
            account = mapRowToAccount(results);
        }

        return account;
    }

    public Account mapRowToAccount(SqlRowSet result) {
        Account account = new Account();
        account.setAccountId(result.getInt("account_id"));
        account.setUserId(result.getInt("user_id"));
        account.setBalance(result.getBigDecimal("balance"));
        return account;
    }
}
