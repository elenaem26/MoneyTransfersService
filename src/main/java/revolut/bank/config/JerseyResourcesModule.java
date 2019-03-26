package revolut.bank.config;

import java.util.Set;

import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class JerseyResourcesModule extends ServletModule {

    @Override
    protected void configureServlets() {
        bindResources();
        serveBoundResources();
    }

    private void bindResources() {
        for (Class<?> resource : lookupResources()) {
            bind(resource);
        }
    }

    private Set<Class<?>> lookupResources() {
        PackagesResourceConfig resourceConfig = new PackagesResourceConfig("revolut.bank.resource");

        return resourceConfig.getClasses();
    }

    private void serveBoundResources() {
        serve("/*").with(GuiceContainer.class);
    }
}