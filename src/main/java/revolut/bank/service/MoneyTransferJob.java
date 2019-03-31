package revolut.bank.service;

import com.google.inject.Inject;
import revolut.bank.model.generated.tables.records.TransferRecord;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * MoneyTransferJob.
 * Scheduled job for updating transfers which state is inprogress
 *
 */
public class MoneyTransferJob {

    private static final int DELAY_IN_MILLIS = 2 * 1000;

    @Inject
    private MoneyTransfersService moneyTransfersService;

    public MoneyTransferJob() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleWithFixedDelay(this::makeTransfer, 0, DELAY_IN_MILLIS, TimeUnit.MILLISECONDS);
    }

    public void makeTransfer() {
        Collection<TransferRecord> recordsInProgress = moneyTransfersService.getInprogressTransfers();
        for (TransferRecord record : recordsInProgress) {
            moneyTransfersService.transferInJob(record);
        }
    }
}
