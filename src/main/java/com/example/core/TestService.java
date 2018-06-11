package com.example.core;

import com.example.api.TestApi;

public class TestService implements TestApi {
    public boolean deleteAll() {
        return BankAccountStore.getBankAccountStore().deleteAll();
    }
}
