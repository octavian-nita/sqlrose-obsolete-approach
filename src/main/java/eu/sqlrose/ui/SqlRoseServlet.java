package eu.sqlrose.ui;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import eu.sqlrose.core.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 05, 2016
 */
@WebServlet(name = "sqlrose", value = "/*", asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = SqlRoseUI.class)
public class SqlRoseServlet extends VaadinServlet implements SessionInitListener, SessionDestroyListener {

    static {
        SLF4JBridgeHandler.install();
    }

    protected final Logger log = LoggerFactory.getLogger(SqlRoseServlet.class);

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();

        VaadinServletService vaadinService = getService();
        vaadinService.addSessionInitListener(this);
        vaadinService.addSessionDestroyListener(this);
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) {
        try {

            super.service(request, response);

        } catch (Throwable throwable) {
            String message = "An error has occurred";

            if (request instanceof HttpServletRequest) {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                message += " while servicing request " + httpRequest.getRequestURI();

                String queryString = httpRequest.getQueryString();
                if (queryString != null && (queryString = queryString.trim()).length() > 0) {
                    message += "?" + queryString;
                }
            }

            log.error(message, throwable);

            // TODO: redirect to an error page with app restart option
        }
    }

    @Override
    public void sessionInit(SessionInitEvent sessionInitEvent) throws ServiceException {
        VaadinSession session = sessionInitEvent.getSession();
        VaadinService service = session.getService();

        session.addRequestHandler(new I18nRequestHandler());

        // Load server configuration
        try {
            session.setAttribute(Environment.class,
                                 new Environment().load(service.getClassLoader().getResource("connections.yml")));
        } catch (IOException ioe) {
            throw new ServiceException("cannot load the (server) environment", ioe);
        }

        // Load local storage configuration

        log.info("SqlRose session created");
    }

    @Override
    public void sessionDestroy(SessionDestroyEvent sessionDestroyEvent) {

        // TODO: close remaining open DB connections

        log.info("SqlRose session destroyed");
    }
}
