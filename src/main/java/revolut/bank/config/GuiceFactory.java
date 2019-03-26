package revolut.bank.config;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class GuiceFactory
{
    private static final Injector inj = Guice.createInjector(new ServicesModule(), new JerseyResourcesModule());

    public static Injector getInjector()
    {
        return inj;
    }
}
