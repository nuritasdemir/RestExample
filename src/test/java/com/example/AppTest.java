package com.example;

import com.example.utils.RetrieveUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    @Test
    public void createTwoAccounts() throws IOException {
        resetBankAccounts();
        long userId = 1;

        long firstAccountId = createAccount(userId);
        long secondAccountId = createAccount(userId);

        assertEquals(1, firstAccountId);
        assertEquals(2, secondAccountId);
    }

    @Test
    public void depositMoney() throws IOException {
        resetBankAccounts();
        long userId = 1;
        long firstAmount = 100;
        long secondAmount = 200;

        long accountId = createAccount(userId);
        assertEquals(1, accountId);

        long resultingAmount = depositMoney(accountId, firstAmount);
        checkBalance(accountId, firstAmount, resultingAmount);

        resultingAmount = depositMoney(accountId, secondAmount);
        checkBalance(accountId, firstAmount + secondAmount, resultingAmount);
    }

    @Test
    public void depositTooMuchMoney() throws IOException {
        resetBankAccounts();
        long userId = 1;
        long amount = 9007199254740992L;

        long accountId = createAccount(userId);
        assertEquals(1, accountId);

        String response = depositMoneyResponse(accountId, amount);

        assertEquals("{\"status\":500,\"message\":\"Current balance is 0 and account cannot hold more than 9007199254740991\"}", response);

        checkBalance(accountId, 0);
    }

    @Test
    public void depositNegative() throws IOException {
        resetBankAccounts();
        long userId = 1;
        long amount = -1;

        long accountId = createAccount(userId);
        assertEquals(1, accountId);

        String response = depositMoneyResponse(accountId, amount);

        assertEquals("{\"status\":500,\"message\":\"Amount should be positive\"}", response);

        checkBalance(accountId, 0);
    }

    @Test
    public void depositZero() throws IOException {
        resetBankAccounts();
        long userId = 1;
        long amount = 0;

        long accountId = createAccount(userId);
        assertEquals(1, accountId);

        String response = depositMoneyResponse(accountId, amount);

        assertEquals("{\"status\":500,\"message\":\"Amount should be positive\"}", response);

        checkBalance(accountId, 0);
    }

    @Test
    public void depositNotExistingAccount() throws IOException {
        resetBankAccounts();
        long accountId = 1;
        long amount = 100;

        String response = depositMoneyResponse(accountId, amount);

        assertEquals("{\"status\":500,\"message\":\"Account with ID " +
                accountId + " does not exist\"}", response);
    }

    @Test
    public void withdrawMoney() throws IOException {
        resetBankAccounts();
        long userId = 1;
        long initialBalance = 100;
        long firstWithdrawAmount = 30;
        long secondWithdrawAmount = 60;

        long accountId = createAccount(userId);
        assertEquals(1, accountId);

        long resultingAmount = depositMoney(accountId, initialBalance);
        checkBalance(accountId, initialBalance, resultingAmount);

        resultingAmount = withdrawMoney(accountId, firstWithdrawAmount);
        checkBalance(accountId, initialBalance - firstWithdrawAmount, resultingAmount);

        resultingAmount = withdrawMoney(accountId, secondWithdrawAmount);
        checkBalance(accountId, initialBalance - firstWithdrawAmount - secondWithdrawAmount, resultingAmount);
    }

    @Test
    public void withdrawTooMuchMoney() throws IOException {
        resetBankAccounts();
        long userId = 1;
        long initialBalance = 100;
        long withdrawAmount = 130;

        long accountId = createAccount(userId);
        assertEquals(1, accountId);

        long resultingAmount = depositMoney(accountId, initialBalance);
        checkBalance(accountId, initialBalance, resultingAmount);

        String response = withdrawMoneyResponse(accountId, withdrawAmount);

        assertEquals(
                "{\"status\":500,\"message\":\"Amount should be positive and not greater than balance " +
                        initialBalance + ".\"}", response);

        checkBalance(accountId, initialBalance);
    }

    @Test
    public void withdrawZero() throws IOException {
        resetBankAccounts();
        long userId = 1;
        long initialBalance = 100;
        long withdrawAmount = 0;

        long accountId = createAccount(userId);
        assertEquals(1, accountId);

        long resultingAmount = depositMoney(accountId, initialBalance);
        checkBalance(accountId, initialBalance, resultingAmount);

        String response = withdrawMoneyResponse(accountId, withdrawAmount);

        assertEquals(
                "{\"status\":500,\"message\":\"Amount should be positive and not greater than balance " +
                        initialBalance + ".\"}", response);

        checkBalance(accountId, initialBalance);
    }

    @Test
    public void withdrawNegative() throws IOException {
        resetBankAccounts();
        long userId = 1;
        long initialBalance = 100;
        long withdrawAmount = -1;

        long accountId = createAccount(userId);
        assertEquals(1, accountId);

        long resultingAmount = depositMoney(accountId, initialBalance);
        checkBalance(accountId, initialBalance, resultingAmount);

        String response = withdrawMoneyResponse(accountId, withdrawAmount);

        assertEquals(
                "{\"status\":500,\"message\":\"Amount should be positive and not greater than balance " +
                        initialBalance + ".\"}", response);

        checkBalance(accountId, initialBalance);
    }

    @Test
    public void withdrawNotExistingAccount() throws IOException {
        resetBankAccounts();
        long accountId = 1;
        long amount = 100;

        String response = withdrawMoneyResponse(accountId, amount);

        assertEquals("{\"status\":500,\"message\":\"Account with ID " +
                accountId + " does not exist\"}", response);
    }

    @Test
    public void transferMoney() throws IOException {
        resetBankAccounts();
        long userId = 1;

        long sourceAccountId = createAccount(userId);
        long destinationAccountId = createAccount(userId);

        long sourceInitialBalance = 100;
        long destinationInitialBalance = 50;
        long transferAmount = 30;

        assertEquals(1, sourceAccountId);
        assertEquals(2, destinationAccountId);

        long resultingAmount = depositMoney(sourceAccountId, sourceInitialBalance);
        checkBalance(sourceAccountId, sourceInitialBalance, resultingAmount);

        resultingAmount = depositMoney(destinationAccountId, destinationInitialBalance);
        checkBalance(destinationAccountId, destinationInitialBalance, resultingAmount);

        resultingAmount = transferMoney(sourceAccountId, destinationAccountId, transferAmount);
        checkBalance(sourceAccountId, sourceInitialBalance - transferAmount, resultingAmount);

        checkBalance(destinationAccountId, destinationInitialBalance + transferAmount);
    }

    @Test
    public void transferMoneySourceAccountNotExist() throws IOException {
        resetBankAccounts();
        long userId = 1;

        long sourceAccountId = 10;
        long destinationAccountId = createAccount(userId);

        long transferAmount = 30;

        String response = transferMoneyResponse(sourceAccountId, destinationAccountId, transferAmount);

        assertEquals("{\"status\":500,\"message\":\"Account with ID " +
                sourceAccountId + " does not exist\"}", response);

        checkBalance(destinationAccountId, 0);
    }

    @Test
    public void transferMoneyDestinationAccountNotExist() throws IOException {
        resetBankAccounts();
        long userId = 1;

        long sourceAccountId = createAccount(userId);
        long destinationAccountId = 10;

        long transferAmount = 30;

        String response = transferMoneyResponse(sourceAccountId, destinationAccountId, transferAmount);

        assertEquals("{\"status\":500,\"message\":\"Account with ID " +
                destinationAccountId + " does not exist\"}", response);

        checkBalance(sourceAccountId, 0);
    }

    @Test
    public void transferMoneyBothAccountNotExist() throws IOException {
        resetBankAccounts();

        long sourceAccountId = 10;
        long destinationAccountId = 20;

        long transferAmount = 30;

        String response = transferMoneyResponse(sourceAccountId, destinationAccountId, transferAmount);

        assertEquals("{\"status\":500,\"message\":\"Account with ID " +
                sourceAccountId + " does not exist\"}", response);
    }

    @Test
    public void transferMoneyDestinationFull() throws IOException {
        resetBankAccounts();
        long userId = 1;

        long sourceAccountId = createAccount(userId);
        long destinationAccountId = createAccount(userId);

        long sourceInitialBalance = 100;
        long destinationInitialBalance = 9007199254740990L;
        long maxAmount = 9007199254740991L;
        long transferAmount = 30;

        assertEquals(1, sourceAccountId);
        assertEquals(2, destinationAccountId);

        long resultingAmount = depositMoney(sourceAccountId, sourceInitialBalance);
        checkBalance(sourceAccountId, sourceInitialBalance, resultingAmount);

        resultingAmount = depositMoney(destinationAccountId, destinationInitialBalance);
        checkBalance(destinationAccountId, destinationInitialBalance, resultingAmount);

        String response = transferMoneyResponse(sourceAccountId, destinationAccountId, transferAmount);

        assertEquals("{\"status\":500,\"message\":\"Current balance is " + destinationInitialBalance +
                " and account cannot hold more than " + maxAmount +"\"}", response);

        checkBalance(sourceAccountId, sourceInitialBalance);
        checkBalance(destinationAccountId, destinationInitialBalance);
    }


    private void checkBalance(long accountId, long expectedAmount) throws IOException {
        long amount = getBalance(accountId);
        assertEquals(expectedAmount, amount);
    }

    private void checkBalance(long accountId, long expectedAmount, long resultingAmount) throws IOException {
        checkBalance(accountId, expectedAmount);
        assertEquals(expectedAmount, resultingAmount);
    }

    private long getBalance(long accountId) throws IOException {
        HttpUriRequest request = new HttpGet( "http://localhost:8080/bank-account/balance?account-id=" + accountId);
        long amount = Long.parseLong(getResponse(request));
        return amount;
    }

    private long depositMoney(long accountId, long amount) throws IOException {
        long newAmount = Long.parseLong(depositMoneyResponse(accountId, amount));
        return newAmount;
    }

    private String depositMoneyResponse(long accountId, long amount) throws IOException {
        HttpUriRequest request = new HttpPost( "http://localhost:8080/bank-account/deposit?account-id=" +
                accountId + "&amount=" + amount);
        String response = getResponse(request);
        return response;
    }


    private long withdrawMoney(long accountId, long amount) throws IOException {
        String response = withdrawMoneyResponse(accountId, amount);
        return Long.parseLong(response);
    }

    private String withdrawMoneyResponse(long accountId, long amount) throws IOException {
        HttpUriRequest request = new HttpPost( "http://localhost:8080/bank-account/withdraw?account-id=" +
                accountId + "&amount=" + amount);
        return getResponse(request);
    }

    private long transferMoney(long sourceAccountId, long destinationAccountId, long amount) throws IOException {
        String response = transferMoneyResponse(sourceAccountId, destinationAccountId, amount);
        return Long.parseLong(response);
    }

    private String transferMoneyResponse(long sourceAccountId, long destinationAccountId, long amount) throws IOException {
        HttpUriRequest request = new HttpPost( "http://localhost:8080/money-transfer/transfer?source-account-id=" +
                sourceAccountId + "&destination-account-id=" + destinationAccountId + "&amount=" + amount);

        return getResponse(request);
    }

    private String getResponse(HttpUriRequest request) throws IOException {
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
        String response = RetrieveUtils.retrieveResourceFromResponse(httpResponse);
        return response;
    }

    private long createAccount(long userId) throws IOException {
        HttpUriRequest request = new HttpPost( "http://localhost:8080/bank-account/open?user-id=" + userId );
        long accountId = Long.parseLong(getResponse(request));
        return accountId;
    }

    private void resetBankAccounts() throws IOException {
        HttpUriRequest request = new HttpDelete( "http://localhost:8080/test/all" );
        getResponse(request);
    }
}
