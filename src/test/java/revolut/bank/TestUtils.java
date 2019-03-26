package revolut.bank;

import revolut.bank.config.GuiceFactory;
import revolut.bank.h2.H2DbInitializer;
import revolut.bank.service.MoneyTransfersService;
import com.google.inject.Injector;
import org.junit.AfterClass;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class TestUtils {

    static String ACCOUNTS_PATH = "accounts";
    static String MONEY_TRANSFER_PATH = "money/transfer";

    private static Injector injector;
    private static ServerRunner serverRunner;

    static URI getBaseURI() {
        return UriBuilder.fromUri( "http://localhost/" ).port( 8081 ).build();
    }

    static void startServer() throws Exception {
        injector = GuiceFactory.getInjector();
        MoneyTransfersService service = injector.getInstance(MoneyTransfersService.class);
        H2DbInitializer h2DbInitializer = injector.getInstance(H2DbInitializer.class);
        serverRunner = injector.getInstance(ServerRunner.class);
        h2DbInitializer.initInMemoryDb();
        serverRunner.run();
    }

    @AfterClass
    public static void stopServer() throws Exception {
        serverRunner.stopServer();
    }
}
