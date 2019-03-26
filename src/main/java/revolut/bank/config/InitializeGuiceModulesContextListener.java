package revolut.bank.config;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class InitializeGuiceModulesContextListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return GuiceFactory.getInjector();
    }
}
