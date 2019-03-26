package revolut.bank;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import revolut.bank.config.InitializeGuiceModulesContextListener;
import com.google.inject.servlet.GuiceFilter;

public class ServerRunner {
    
    private static Server server;

    @Inject(optional = true)
    @Named("server.port")
    private int port = 8080;
    
    public void run() throws Exception {
        createServer();
        bindGuiceContextToServer();
        startServer();
    }

    private void bindGuiceContextToServer() {
        ServletContextHandler context = createRootContext();
        serveGuiceContext(context);
    }

    private void serveGuiceContext(ServletContextHandler context) {
        bindGuiceContextAndFilter(context);
        addDefaultServletToContext(context);
    }

    private void addDefaultServletToContext(ServletContextHandler context) {
        /*
         * Jetty requires some servlet to be bound to the path, otherwise request is just skipped. This prevents Guice
         * from handling the request, because it is done through filter.
         */
        context.addServlet(DefaultServlet.class, "/");
    }

    private void bindGuiceContextAndFilter(ServletContextHandler context) {
        context.addEventListener(new InitializeGuiceModulesContextListener());
        context.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
    }

    private ServletContextHandler createRootContext() {
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("");
        server.setHandler(context);
        return context;
    }

    public void waitForServerToFinish() throws InterruptedException {
        server.join();
    }

    private void startServer() throws Exception {
        server.start();
    }

    public void stopServer() throws Exception {
        server.stop();
    }

    private void createServer() {
        server = new Server(port);
    }
}
