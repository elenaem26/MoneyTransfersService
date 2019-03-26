package revolut.bank.resource;

import org.apache.log4j.Logger;
import revolut.bank.dto.AccountDto;
import revolut.bank.dto.AccountsDto;
import revolut.bank.service.AccountService;
import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import revolut.bank.service.MoneyTransfersService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/accounts")
@RequestScoped
public class AccountController {

    @Inject
    private AccountService accountService;

    private static final Logger log = Logger.getLogger(MoneyTransfersService.class);

    @GET
    @Path("{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDto getAccountById(@PathParam("number") String number) {
        log.info("GET account #" + number);
        return accountService.getById(number);
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountsDto getAllAccounts() {
        log.info("GET all accounts");
        return accountService.getAllAccounts();
    }

    @GET
    @Path("party/{partyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountsDto getAccountsByParty(@PathParam("partyId") Long partyId) {
        log.info("GET accounts for party #" + partyId);
        return accountService.getAccountsForParty(partyId);
    }

}
