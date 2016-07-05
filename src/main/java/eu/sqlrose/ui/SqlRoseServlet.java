package eu.sqlrose.ui;

import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;

import javax.servlet.ServletException;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 05, 2016
 */
public class SqlRoseServlet extends VaadinServlet implements SessionInitListener, SessionDestroyListener {

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();

        VaadinServletService vaadinService = getService();
        vaadinService.addSessionInitListener(this);
        vaadinService.addSessionDestroyListener(this);
    }

    @Override
    public void sessionInit(SessionInitEvent sessionInitEvent) throws ServiceException {
        // TODO: 1. decide upon a configuration and load it
        // TODO: 2. set up Vaadin and application logging
    }

    @Override
    public void sessionDestroy(SessionDestroyEvent sessionDestroyEvent) {
        // TODO: close / log remaining open DB connections...
    }
}
