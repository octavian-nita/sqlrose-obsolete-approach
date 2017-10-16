package eu.sqlrose.ui.web.vaadin;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import eu.sqlrose.core.CompositeException;
import eu.sqlrose.env.Environment;
import eu.sqlrose.ui.i18n.I18nRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.net.URL;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 05, 2016
 */
@WebServlet(name = "sqlrose", value = "/*", asyncSupported = true)
@VaadinServletConfiguration(productionMode = true, ui = SqlRoseUI.class)
public class SqlRoseServlet extends VaadinServlet implements SessionInitListener, SessionDestroyListener {

    private final Logger log = LoggerFactory.getLogger(SqlRoseServlet.class);

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();

        final VaadinServletService service = getService();
        service.addSessionInitListener(this);
        service.addSessionDestroyListener(this);
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = (HttpServletRequest) request; // web...

        String uri = httpRequest.getRequestURI();
        String qs = httpRequest.getQueryString();
        if (qs != null) {
            uri += "?" + qs;
        }

        try {

            log.debug("*** REQ {} ***", uri);
            super.service(request, response);

        } catch (Throwable throwable) {
            log.error("An error has occurred while servicing request " + uri, throwable);
        }
    }

    @Override
    public void sessionInit(SessionInitEvent sessionInitEvent) throws ServiceException {
        final VaadinSession session = sessionInitEvent.getSession();

        Environment env = loadEnvironment();
        // TODO perform eventual initial configuration based on the loaded environment
        session.setAttribute(Environment.class, env);

        session.addRequestHandler(new I18nRequestHandler());
        session.setErrorHandler(new SqlRoseErrorHandler());

        log.info("SQLrose session initialized");
    }

    /**
     * Loading an environment (server and/or local) does not throw any exceptions (eventual errors are at least logged).
     */
    protected Environment loadEnvironment() {
        final Environment env = new Environment();

        ClassLoader ldr = VaadinService.getCurrent().getClassLoader();
        for (String res : new String[]{//@fmt:off

            "src/main/resources/config.yaml",
            "src/main/resources/config-private.yaml",
            "src/main/resources/data-sources.yaml",
            "src/main/resources/data-sources-private.yaml" //@fmt:on

        }) {
            URL url = ldr.getResource(res);
            if (url == null) {
                log.info("Configuration resource " + res + " not found; ignoring...");
            } else {
                log.info("Loading the environment from configuration resource " + res + "...");
                env.load(url);
            }
        }

        return env;
    }

    @Override
    public void sessionDestroy(SessionDestroyEvent sessionDestroyEvent) {
        final VaadinSession session = sessionDestroyEvent.getSession();

        Environment env = session.getAttribute(Environment.class);
        if (env != null) {
            try {
                env.cleanup();
            } catch (CompositeException e) {
                session.getErrorHandler().error(new ErrorEvent(e));
            }
        }

        log.info("SQLrose session destroyed");
    }
}
