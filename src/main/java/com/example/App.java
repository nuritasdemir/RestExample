package com.example;

import com.example.core.BankAccountService;
import com.example.core.TestService;
import com.example.exception.GenericExceptionMapper;
import com.example.core.MoneyTransferService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class App {
    public static void main(String[] args) throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(8080);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        // Tells the Jersey Servlet which REST core/class to load.
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                BankAccountService.class.getCanonicalName() + ";" +
                        MoneyTransferService.class.getCanonicalName() + ";" +
                        TestService.class.getCanonicalName() + ";" +
                        GenericExceptionMapper.class.getCanonicalName());
        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }
}
