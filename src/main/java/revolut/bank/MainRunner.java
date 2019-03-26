package revolut.bank;

import revolut.bank.config.GuiceFactory;
import revolut.bank.h2.H2DbInitializer;
import revolut.bank.service.MoneyTransfersService;
import com.google.inject.Injector;

public class MainRunner {

    public static void main(String[] args) throws Exception {
        Injector injector = GuiceFactory.getInjector();
        H2DbInitializer h2DbInitializer = injector.getInstance(H2DbInitializer.class);
        ServerRunner serverRunner = injector.getInstance(ServerRunner.class);
        h2DbInitializer.initInMemoryDb();
        serverRunner.run();
        serverRunner.waitForServerToFinish();
    }
}
