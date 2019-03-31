package revolut.bank.config;

import revolut.bank.ServerRunner;
import revolut.bank.exception.MissingConfigurationFileException;
import revolut.bank.h2.DSLContextFactory;
import revolut.bank.h2.DataSource;
import revolut.bank.h2.H2DbInitializer;
import revolut.bank.service.AccountService;
import revolut.bank.service.MoneyTransferJob;
import revolut.bank.service.MoneyTransfersService;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ServicesModule extends AbstractModule {

    private static final String PROPERTIES_FILE_NAME = "application.properties";

    @Override
    protected void configure() {
        bind(ServerRunner.class).in(Singleton.class);
        bind(DataSource.class).in(Singleton.class);
        bind(DSLContextFactory.class).in(Singleton.class);
        bind(H2DbInitializer.class).in(Singleton.class);
        bind(MoneyTransfersService.class).in(Singleton.class);
        bind(AccountService.class).in(Singleton.class);
        bind(MoneyTransferJob.class).in(Singleton.class);

        try {
            Properties properties = new Properties();
            properties.load(new FileReader(PROPERTIES_FILE_NAME));
            Names.bindProperties(binder(), properties);
        } catch (IOException e) {
            throw new MissingConfigurationFileException("Configuration file application.properties is missing", e);
        }
    }

}
