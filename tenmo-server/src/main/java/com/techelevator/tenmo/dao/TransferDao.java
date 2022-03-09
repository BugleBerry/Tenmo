package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    List<Transfer> getTransfersByAccountId(int accountId);

    Transfer getTransferById(Integer transferId);

    void sendTransfer(Transfer transferToSent);

    Transfer createTransfer(Transfer newTransfer);
}
