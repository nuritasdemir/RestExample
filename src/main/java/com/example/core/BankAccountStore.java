package com.example.core;

import org.apache.commons.lang3.Validate;

import java.util.Hashtable;

class BankAccountStore {
    private final static BankAccountStore bankAccountStore = new BankAccountStore();

    private Hashtable<Long, BankAccount> accounts;
    private long nextAccountId;
    private Object nextAccountIdLock;

    private BankAccountStore(){
        accounts = new Hashtable<Long, BankAccount>();
        nextAccountId = 1;
        nextAccountIdLock = new Object();
    }

    protected static BankAccountStore getBankAccountStore () {
        return bankAccountStore;
    }

    private BankAccount getAccount(long accountId) {
        BankAccount account = accounts.get(accountId);
        Validate.notNull(account, "Account with ID %s does not exist", Long.valueOf(accountId));
        return account;
    }

    protected long transfer(long sourceAccountId, long destinationAccountId, long amount){
        BankAccount sourceAccount = getAccount(sourceAccountId);
        BankAccount destinationAccount = getAccount(destinationAccountId);

        if (sourceAccountId == destinationAccountId)
            return sourceAccount.getBalance();

        BankAccount firstLock, secondLock;

        if (sourceAccountId < destinationAccountId) {
            firstLock = sourceAccount;
            secondLock = destinationAccount;
        } else {
            firstLock = destinationAccount;
            secondLock = sourceAccount;
        }

        synchronized (firstLock) {
            synchronized (secondLock) {
                return transfer(sourceAccount, destinationAccount, amount);
            }
        }
    }

    private long transfer(BankAccount sourceAccount, BankAccount destinationAccount, long amount) {
        sourceAccount.withdraw(amount);

        try {
            destinationAccount.deposit(amount);
        } catch (IllegalArgumentException ex) {
            sourceAccount.deposit(amount);
            throw ex;
        }

        return sourceAccount.getBalance();
    }

    protected long getBalance(long accountId) {
        return getAccount(accountId).getBalance();
    }


    protected long open(long userId) {
        long accountId;
        synchronized (nextAccountIdLock) {
            accountId = nextAccountId++;
        }

        accounts.put(accountId, new BankAccount(userId, accountId));

        return accountId;
    }

    protected long withdraw(long accountId, long amount) {
        BankAccount account = getAccount(accountId);

        synchronized (account) {
            return account.withdraw(amount);
        }
    }

    protected long deposit(long accountId, long amount) {
        BankAccount account = getAccount(accountId);

        synchronized (account) {
            return account.deposit(amount);
        }
    }

    public boolean deleteAll() {
        synchronized (nextAccountIdLock) {
            nextAccountId = 1;
            accounts.clear();
        }
        return true;
    }
}
