package com.example.core;

import org.apache.commons.lang3.Validate;

import java.util.Currency;

class BankAccount {
    private final long userId;
    private final long accountId;
    private final Currency currency;
    private long balance;

    //Max value of javascript integer
    protected final static long MAX_BALANCE = 9007199254740991L;

    protected BankAccount(long userId, long accountId) {
        this.userId = userId;
        this.accountId = accountId;
        this.currency = null;
        this.balance = 0;
    }

    protected BankAccount(long userId, long accountId, Currency currency) {
        Validate.notNull(currency,"Currency must not be %s", (Currency)null);

        this.userId = userId;
        this.accountId = accountId;
        this.currency = currency;
        this.balance = 0;
    }

    protected long withdraw(long amount) {
        Validate.inclusiveBetween(1L, balance, amount,
                "Amount should be positive and not greater than balance %s.", Long.valueOf(balance));

        balance -= amount;

        return balance;
    }

    protected long deposit(long amount) {
        Validate.isTrue(amount > 0, "Amount should be positive");
        Validate.isTrue(MAX_BALANCE - balance >= amount,
                "Current balance is %s and account cannot hold more than %s",
                Long.valueOf(balance), Long.valueOf(MAX_BALANCE));

        balance += amount;

        return balance;
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
