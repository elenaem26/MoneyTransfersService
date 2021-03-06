package revolut.bank;

import revolut.bank.dto.*;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import revolut.bank.model.TransferStatus;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static revolut.bank.TestUtils.ACCOUNTS_PATH;
import static revolut.bank.TestUtils.MONEY_TRANSFER_PATH;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Testing the application REST API.
 * I use Thread.sleep in order to give time to the {@link revolut.bank.service.MoneyTransferJob} to work out
 */
public class ApplicationTest {

    @BeforeClass
    public static void startServer() throws Exception {
        TestUtils.startServer();
    }

    @AfterClass
    public static void stopServer() throws Exception {
        TestUtils.stopServer();
    }

    /**
     * Test one money transfer between accounts
     * @throws IOException
     */
    @Test
    public void testOneTransfer() throws IOException, InterruptedException {
        Client client = Client.create( new DefaultClientConfig() );
        WebResource service = client.resource(TestUtils.getBaseURI());
        String from = "456";
        String to = "567";
        int amount = 19999;

        ClientResponse resp = service.path(ACCOUNTS_PATH).path(from)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        AccountDto accountFromBefore = resp.getEntity(AccountDto.class);

        resp = service.path(ACCOUNTS_PATH).path(to)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        AccountDto accountToBefore = resp.getEntity(AccountDto.class);

        makeOneTransfer(service, from, to, amount);

        Thread.sleep(2300);

        resp = service.path(ACCOUNTS_PATH).path(from)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        AccountDto accountFromAfter = resp.getEntity(AccountDto.class);

        resp = service.path(ACCOUNTS_PATH).path(to)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        AccountDto accountToAfter= resp.getEntity(AccountDto.class);

        assertEquals(accountFromBefore.getBalance() - amount, (long)accountFromAfter.getBalance());
        assertEquals(accountToBefore.getBalance() + amount, (long)accountToAfter.getBalance());
    }

    /**
     * Test 1000 serial money transfers between accounts
     * @throws IOException
     */
    @Test
    public void test1000Transfers() throws IOException, InterruptedException {
        Client client = Client.create( new DefaultClientConfig() );
        WebResource service = client.resource(TestUtils.getBaseURI());
        String fromNumber = "3465";
        String toNumber = "123";
        testMultipleTransfers(service, fromNumber, toNumber, 12, 1000);
    }

    /**
     * Test concurrent transfers
     * account1  156->  account2
     * account1  1126->  account2
     * account2  546->  account1
     * account2  541->  account3
     * account1  5416->  account3
     * @throws IOException
     */
    @Test
    public void testConcurrentTransfers() throws IOException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(20);
        Client client = Client.create( new DefaultClientConfig() );
        WebResource service = client.resource(TestUtils.getBaseURI());

        String account1 = "3465";
        String account2 = "345";
        String account3 = "123";

        ClientResponse resp = service.path(ACCOUNTS_PATH).path(account1)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        AccountDto account1Before = resp.getEntity(AccountDto.class);

        resp = service.path(ACCOUNTS_PATH).path(account2)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        AccountDto account2Before = resp.getEntity(AccountDto.class);

        resp = service.path(ACCOUNTS_PATH).path(account3)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        AccountDto account3Before = resp.getEntity(AccountDto.class);

        executor.execute(() -> service.path(MONEY_TRANSFER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, TransferInfoDto.builder()
                        .fromAccountNumber(account1)
                        .toAccountNumber(account2)
                        .amount(156L)
                        .build()));

        executor.execute(() -> service.path(MONEY_TRANSFER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, TransferInfoDto.builder()
                        .fromAccountNumber(account1)
                        .toAccountNumber(account2)
                        .amount(1126L)
                        .build()));


        executor.execute(() -> service.path(MONEY_TRANSFER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, TransferInfoDto.builder()
                        .fromAccountNumber(account2)
                        .toAccountNumber(account1)
                        .amount(546L)
                        .build()));

        executor.execute(() -> service.path(MONEY_TRANSFER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, TransferInfoDto.builder()
                        .fromAccountNumber(account2)
                        .toAccountNumber(account3)
                        .amount(541L)
                        .build()));

        executor.execute(() -> service.path(MONEY_TRANSFER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, TransferInfoDto.builder()
                        .fromAccountNumber(account1)
                        .toAccountNumber(account3)
                        .amount(5416L)
                        .build()));

        executor.shutdown();
        final boolean done = executor.awaitTermination(1, TimeUnit.SECONDS);
        assertTrue(done);

        Thread.sleep(4000);

        resp = service.path(ACCOUNTS_PATH).path(account1)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        AccountDto account1After = resp.getEntity(AccountDto.class);

        resp = service.path(ACCOUNTS_PATH).path(account2)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        AccountDto account2After = resp.getEntity(AccountDto.class);

        resp = service.path(ACCOUNTS_PATH).path(account3)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        AccountDto account3After = resp.getEntity(AccountDto.class);

        assertEquals(account1Before.getBalance() - 156 - 1126 + 546 - 5416, (long)account1After.getBalance());
        assertEquals(account2Before.getBalance() + 156 + 1126 - 546 - 541, (long)account2After.getBalance());
        assertEquals(account3Before.getBalance() + 541 + 5416 , (long)account3After.getBalance());
    }

    @Test
    public void testFailedTransfer() {
        Client client = Client.create( new DefaultClientConfig() );
        WebResource service = client.resource(TestUtils.getBaseURI());
        ClientResponse resp = service.path(MONEY_TRANSFER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, TransferInfoDto.builder()
                        .fromAccountNumber("123")
                        .toAccountNumber("234")
                        .amount(120000L)
                        .build());
        ErrorMessage errorMessage = resp.getEntity(ErrorMessage.class);
        assertEquals(400, errorMessage.getStatus());
        assertEquals("Not enough money", errorMessage.getMessage());
    }

    @Test
    public void testTransferHistory() throws IOException, InterruptedException {
        Client client = Client.create( new DefaultClientConfig() );
        WebResource service = client.resource(TestUtils.getBaseURI());
        makeOneTransfer(service, "2345", "678", 1);
        makeOneTransfer(service, "2345", "567", 2);
        makeOneTransfer(service, "678", "2345", 3);
        makeOneTransfer(service, "678", "567", 3);

        Thread.sleep(3000);

        ClientResponse resp = service.path(MONEY_TRANSFER_PATH).path("history").path("2345")
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        TransferHistoriesDto history = resp.getEntity(TransferHistoriesDto.class);
        assertEquals(3, history.getTotal());

        assertEquals("2345", history.getHistory().get(0).getUserAccountNumber());
        assertEquals("678", history.getHistory().get(0).getInteractionAccountNumber());
        assertEquals(3, (long)history.getHistory().get(0).getAmount());
        assertEquals(TransferStatus.DONE.name(), history.getHistory().get(0).getState());
        assertEquals(OperationType.DEPOSIT.name(), history.getHistory().get(0).getOperation());

        assertEquals("2345", history.getHistory().get(1).getUserAccountNumber());
        assertEquals("567", history.getHistory().get(1).getInteractionAccountNumber());
        assertEquals(2, (long)history.getHistory().get(1).getAmount());
        assertEquals(TransferStatus.DONE.name(), history.getHistory().get(1).getState());
        assertEquals(OperationType.WITHDRAW.name(), history.getHistory().get(1).getOperation());

        assertEquals("2345", history.getHistory().get(2).getUserAccountNumber());
        assertEquals("678", history.getHistory().get(2).getInteractionAccountNumber());
        assertEquals(1, (long)history.getHistory().get(2).getAmount());
        assertEquals(TransferStatus.DONE.name(), history.getHistory().get(2).getState());
        assertEquals(OperationType.WITHDRAW.name(), history.getHistory().get(2).getOperation());
    }

    private void testMultipleTransfers(WebResource service, String from, String to, long amount, int times) throws InterruptedException {
        ClientResponse resp = service.path(ACCOUNTS_PATH).path(from)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        AccountDto accountFromBefore = resp.getEntity(AccountDto.class);

        resp = service.path(ACCOUNTS_PATH).path(to)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        AccountDto accountToBefore = resp.getEntity(AccountDto.class);

        for (int i = 0; i < times; i ++) {
            makeOneTransfer(service, from, to, amount);
        }

        Thread.sleep(4000);

        resp = service.path(ACCOUNTS_PATH).path(from)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        AccountDto accountFromAfter = resp.getEntity(AccountDto.class);

        resp = service.path(ACCOUNTS_PATH).path(to)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        AccountDto accountToAfter= resp.getEntity(AccountDto.class);

        assertEquals(accountFromBefore.getBalance() - amount * times, (long)accountFromAfter.getBalance());
        assertEquals(accountToBefore.getBalance() + amount * times, (long)accountToAfter.getBalance());
    }

    private void makeOneTransfer(WebResource service, String from , String to, long amount) throws InterruptedException {
        ClientResponse resp = service.path(MONEY_TRANSFER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, TransferInfoDto.builder()
                        .fromAccountNumber(from)
                        .toAccountNumber(to)
                        .amount(amount)
                        .build());
        assertEquals(resp.getStatus(), 200);
    }

    //Accounts

    @Test
    public void testGetAllAccounts() throws IOException {
        Client client = Client.create( new DefaultClientConfig() );
        WebResource service = client.resource(TestUtils.getBaseURI());

        ClientResponse resp = service.path(ACCOUNTS_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        AccountsDto accounts = resp.getEntity(AccountsDto.class);
        assertEquals(10, accounts.getTotal());
        assertEquals(10, accounts.getAccounts().size());
        assertTrue(accounts.getAccounts().stream()
                .map(AccountDto::getNumber)
                .allMatch(Objects::nonNull));
    }

    @Test
    public void testGetAccountsByParty() throws IOException {
        Client client = Client.create( new DefaultClientConfig() );
        WebResource service = client.resource(TestUtils.getBaseURI());

        ClientResponse resp = service.path(ACCOUNTS_PATH).path("party").path("1")
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        AccountsDto accounts = resp.getEntity(AccountsDto.class);
        assertEquals(2, accounts.getTotal());
        assertEquals(2, accounts.getAccounts().size());
        assertTrue(accounts.getAccounts().stream()
                .map(AccountDto::getNumber)
                .allMatch(Objects::nonNull));

        resp = service.path(ACCOUNTS_PATH).path("party").path("2")
                .accept(MediaType.APPLICATION_JSON )
                .get(ClientResponse.class);
        accounts = resp.getEntity(AccountsDto.class);
        assertEquals(1, accounts.getTotal());
        assertEquals(1, accounts.getAccounts().size());
        assertTrue(accounts.getAccounts().stream()
                .map(AccountDto::getNumber)
                .allMatch(Objects::nonNull));


        resp = service.path(ACCOUNTS_PATH).path("party").path("10")
                .accept(MediaType.APPLICATION_JSON )
                .get(ClientResponse.class);
        accounts = resp.getEntity(AccountsDto.class);
        assertEquals(0, accounts.getTotal());
        assertEquals(0, accounts.getAccounts().size());
        assertTrue(accounts.getAccounts().stream()
                .map(AccountDto::getNumber)
                .allMatch(Objects::nonNull));
    }

    @Test
    public void testGetAccountByNumber() throws IOException {
        Client client = Client.create( new DefaultClientConfig() );
        WebResource service = client.resource(TestUtils.getBaseURI());

        ClientResponse resp = service.path(ACCOUNTS_PATH).path("123")
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        AccountDto account = resp.getEntity(AccountDto.class);
        assertEquals("123", account.getNumber());
    }

    @Test
    public void testGetNotFoundAccountByNumber() throws IOException {
        Client client = Client.create( new DefaultClientConfig() );
        WebResource service = client.resource(TestUtils.getBaseURI());

        ClientResponse resp = service.path(ACCOUNTS_PATH).path("aa123")
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        ErrorMessage errorMessage = resp.getEntity(ErrorMessage.class);
        assertEquals(404, errorMessage.getStatus());
        assertEquals("Account is not found", errorMessage.getMessage());
    }
}
