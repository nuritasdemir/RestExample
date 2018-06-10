package com.example.core;

import com.example.api.BankAccountApi;

public class BankAccountService implements BankAccountApi {
    public long balance(long accountId) {
        return BankAccountStore.getBankAccountStore().getBalance(accountId);
    }

    public long open(long userId) {
        return BankAccountStore.getBankAccountStore().open(userId);
    }

    public long withdraw(long accountId, long amount) {
        return BankAccountStore.getBankAccountStore().withdraw(accountId, amount);
    }

    public long deposit(long accountId, long amount) {
        return BankAccountStore.getBankAccountStore().deposit(accountId, amount);
    }
}
