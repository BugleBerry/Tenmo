package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDao implements TransferDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getTransfersByAccountId(int accountId) {
        String query = "SELECT * FROM transfer WHERE account_from = ? " +
                "OR account_to = ?;";
        List<Transfer> transferList = new ArrayList<>();
        SqlRowSet result = jdbcTemplate.queryForRowSet(query, accountId);
        while (result.next()) {
            transferList.add(mapRowToTransfer(result));
        }
        return transferList;
    }

    @Override
    public Transfer getTransferById(Integer transferId) {
        Transfer transfer = null;
        String query = "SELECT * FROM transfer WHERE transfer_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(query, transferId);
        if (result.next()) {
            transfer = mapRowToTransfer(result);
        }
        return transfer;
    }

    @Override
    public void sendTransfer(Transfer transferToSend) {
//        BigDecimal amountToTransfer = transferToSend.getAmount();
//        BigDecimal fromAccountBalance = new BigDecimal("0.00");
//        String query = "SELECT * FROM account WHERE account_id = ?;";
//        SqlRowSet result = jdbcTemplate.queryForRowSet(query, transferToSend.getAccountFrom());
//        if (result.next()) {
//            fromAccountBalance = result.getBigDecimal("balance");
//        }
//        assert fromAccountBalance != null;
//        fromAccountBalance = fromAccountBalance.subtract(amountToTransfer);
//
//        BigDecimal toAccountBalance = new BigDecimal("0.00");
//        query = "SELECT * FROM account WHERE account_id = ?;";
//        result = jdbcTemplate.queryForRowSet(query, transferToSend.getAccountTo());
//        if (result.next()) {
//            toAccountBalance = result.getBigDecimal("balance");
//        }
//        toAccountBalance = toAccountBalance.add(amountToTransfer);
//
//        query = "UPDATE account SET balance = ? WHERE account_id = ?;";
//        jdbcTemplate.update(query, fromAccountBalance, transferToSend.getAccountFrom());
//
//        query = "UPDATE account SET balance = ? WHERE account_id = ?;";
//        jdbcTemplate.update(query, toAccountBalance, transferToSend.getAccountTo());


    }

    @Override
    public Transfer createTransfer(Transfer newTransfer) {
        String query = "INSERT INTO transfer (transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount) VALUES (2, 2, ?, ?, ?) RETURNING transfer_id;";
        Integer newId = jdbcTemplate.queryForObject(query, Integer.class,
                newTransfer.getAccountFrom(), newTransfer.getAccountTo(), newTransfer.getAmount());
        return getTransferById(newId);
    }

    public Transfer mapRowToTransfer(SqlRowSet result) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(result.getInt("transfer_id"));
        transfer.setTransferTypeId(result.getInt("transfer_type_id"));
        transfer.setTranferStatusId(result.getInt("transfer_status_id"));
        transfer.setAccountFrom(result.getInt("account_from"));
        transfer.setAccountTo(result.getInt("account_to"));
        transfer.setAmount(result.getBigDecimal("amount"));
        return transfer;
    }
}
