package eu.sqlrose.ui;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import eu.sqlrose.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

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

    /**
     * Loading an environment (server and/or local) does not throw any exceptions (eventual errors are at least logged).
     */
    protected Environment loadEnvironment() {
        final Environment env = new Environment();

        ClassLoader ldr = VaadinService.getCurrent().getClassLoader();
        env.load(ldr.getResource("config.yaml"), ldr.getResource("config-private.yaml"),
                 ldr.getResource("data-sources.yaml"), ldr.getResource("data-sources-private.yaml"));

        return env;
    }

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

                String qs = httpRequest.getQueryString();
                if (qs != null && (qs = qs.trim()).length() > 0) {
                    message += "?" + qs;
                }
            }

            log.error(message, throwable);

            // TODO: redirect to an error page with app restart option
        }
    }

    @Override
    public void sessionInit(SessionInitEvent sessionInitEvent) throws ServiceException {
        VaadinSession session = sessionInitEvent.getSession();

        session.addRequestHandler(new I18nRequestHandler());
        session.setAttribute(Environment.class, loadEnvironment());

        log.info("SqlRose session created");
    }

    @Override
    public void sessionDestroy(SessionDestroyEvent sessionDestroyEvent) {

        // TODO: close remaining open DB connections

        log.info("SqlRose session destroyed");
    }
}
