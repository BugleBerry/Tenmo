package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
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
        BigDecimal amountToTransfer = transferToSend.getAmount();

        String query = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";
        jdbcTemplate.update(query, amountToTransfer, transferToSend.getAccountFrom());

        query = "UPDATE account SET balance = balance + ? WHERE account_id = ?;";
        jdbcTemplate.update(query, amountToTransfer, transferToSend.getAccountTo());
    }

    @Override
    public Transfer createTransfer(Transfer newTransfer) {
        String query = "INSERT INTO transfer (transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount) VALUES (2, ?, ?, ?, ?) RETURNING transfer_id;";
        Integer newId = jdbcTemplate.queryForObject(query, Integer.class, newTransfer.getTransferStatusId(),
                newTransfer.getAccountFrom(), newTransfer.getAccountTo(), newTransfer.getAmount());
        return getTransferById(newId);
    }

    public Transfer mapRowToTransfer(SqlRowSet result) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(result.getInt("transfer_id"));
        transfer.setTransferTypeId(result.getInt("transfer_type_id"));
        transfer.setTransferStatusId(result.getInt("transfer_status_id"));
        transfer.setAccountFrom(result.getInt("account_from"));
        transfer.setAccountTo(result.getInt("account_to"));
        transfer.setAmount(result.getBigDecimal("amount"));
        return transfer;
    }
}
