package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests {

    private static final Account ACCOUNT_1 = new Account(new BigDecimal("1000"), 1, 1);
    private static final Account ACCOUNT_2 = new Account(new BigDecimal("500"), 2, 2);
    private static final Account ACCOUNT_3 = new Account(new BigDecimal("100"), 3, 3);

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        sut = new JdbcAccountDao(dataSource);
    }

    @Test
    public void get_balance_returns_correct_balance() {
        BigDecimal balance = sut.getBalance(1);
        Assert.assertEquals("Should return same balance",
                new BigDecimal("1000"), balance);
    }


}
