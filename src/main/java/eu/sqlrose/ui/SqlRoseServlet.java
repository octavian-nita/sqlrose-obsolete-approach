package eu.sqlrose.ui;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
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
@WebServlet(value = "/*", asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = SqlRoseUI.class)
public class SqlRoseServlet extends VaadinServlet implements SessionInitListener, SessionDestroyListener {

    static {
        SLF4JBridgeHandler.install();
    }

    private static final long serialVersionUID = 1020160705L;

    private final Logger LOG = LoggerFactory.getLogger(SqlRoseServlet.class);

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

            LOG.error(message, throwable);
        }
    }

    @Override
    public void sessionInit(SessionInitEvent sessionInitEvent) throws ServiceException {
        LOG.info("SqlRose session created");

        // TODO: load server configuration...
        // TODO: set up i18n...
        // TODO: ensure local storage...
    }

    @Override
    public void sessionDestroy(SessionDestroyEvent sessionDestroyEvent) {
        LOG.info("SqlRose session destroyed");

        // TODO: close / log remaining open DB connections...
    }
}
