package revolut.bank.resource;

import org.apache.log4j.Logger;
import revolut.bank.dto.TransferHistoriesDto;
import revolut.bank.dto.TransferInfoDto;
import revolut.bank.exception.MoneyTransferringException;
import revolut.bank.service.MoneyTransfersService;
import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/money/transfer")
@RequestScoped
public class MoneyTransferController {

    @Inject
    private MoneyTransfersService moneyTransfersService;

    private static final Logger log = Logger.getLogger(MoneyTransfersService.class);

    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transfer(TransferInfoDto transferInfoDto) throws MoneyTransferringException, InterruptedException {
        log.info("POST transfer " + transferInfoDto);
        moneyTransfersService.transfer(transferInfoDto);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("history/{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public TransferHistoriesDto getHistoryForAccount(@PathParam("number") String number,
                                                     @QueryParam("from") String fromDate,
                                                     @QueryParam("to") String toDate) {
        log.info(String.format("Get history for account #%s for perid %s-%s", number, fromDate, toDate));
        return moneyTransfersService.getHistoryForAccount(number, fromDate, toDate);
    }
}
