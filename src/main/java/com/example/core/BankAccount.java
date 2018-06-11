package com.example.core;

import org.apache.commons.lang3.Validate;

class BankAccount {
    private final long userId;
    private final long accountId;
    //Balance is kept as cents in long format.
    private long balance;

    //Max value of javascript integer
    protected final static long MAX_BALANCE = 9007199254740991L;

    protected BankAccount(long userId, long accountId) {
        this.userId = userId;
        this.accountId = accountId;
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

    protected long getUserId() {
        return userId;
    }

    protected long getAccountId() {
        return accountId;
    }
}
