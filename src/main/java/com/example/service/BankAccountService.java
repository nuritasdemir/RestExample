package com.example.service;

import com.example.api.BankAccountApi;

public class BankAccountService implements BankAccountApi {
    public long balance(long accountId) {
        return 5;
    }

    public long withdraw(long userId) {
        return 0;
    }

    public long withdraw(long accountId, long amount) {
        return 0;
    }

    public long deposit(long accountId, long amount) {
        return 0;
    }
}
