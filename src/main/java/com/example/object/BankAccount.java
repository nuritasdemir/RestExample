package com.example.object;

import org.apache.commons.lang3.Validate;

import java.util.Currency;

public class BankAccount {
    private final long userId;
    private final long accountId;
    private final Currency currency;
    private long balance;

    //Max value of javascript integer
    public final static long MAX_BALANCE = 9007199254740991L;

    public BankAccount(long userId, long accountId, Currency currency) {
        Validate.notNull(currency,"Currency must not be %s", null);

        this.userId = userId;
        this.accountId = accountId;
        this.currency = currency;
        this.balance = 0;
    }

    protected void withdraw(long amount) {
        Validate.inclusiveBetween(1L, balance, amount,
                "Amount should be positive and not greater than balance %s.", Long.valueOf(balance));
        balance -= amount;
    }

    protected void deposit(long amount) {
        Validate.isTrue(amount > 0, "Amount should be positive");
        Validate.isTrue(MAX_BALANCE - balance >= amount,
                "Current balance is %s and account cannot hold more than %s",
                Long.valueOf(balance), Long.valueOf(MAX_BALANCE));
        balance += amount;
    }

    protected long getBalance() {
        return balance;
    }

    protected Currency getCurrency() {
        return currency;
    }

    protected long getUserId() {
        return userId;
    }

    protected long getAccountId() {
        return accountId;
    }
}
