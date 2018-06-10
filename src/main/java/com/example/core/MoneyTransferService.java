package com.example.core;

import com.example.api.MoneyTransferApi;

public class MoneyTransferService implements MoneyTransferApi {
    public long transfer(long sourceAccountId, long destinationAccountId, long amount) {
        return BankAccountStore.getBankAccountStore()
                .transfer(sourceAccountId, destinationAccountId, amount);
    }
}
