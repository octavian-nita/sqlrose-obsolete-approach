package eu.sqlrose.ui.web;

import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static java.util.logging.Level.CONFIG;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Mar 30, 2017
 */
@WebListener
public class SqlRoseContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent contextEvent) { setupLogging(contextEvent); }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {}

    protected void setupLogging(ServletContextEvent contextEvent) {
        // Route any j.u.l logging request to SLF4J.
        // See:
        //  * http://blog.cn-consult.dk/2009/03/bridging-javautillogging-to-slf4j.html
        //  * http://stackoverflow.com/questions/9117030/jul-to-slf4j-bridge
        //  * https://logback.qos.ch/manual/configuration.html#LevelChangePropagator

        // We might not need this though if we're installing a LevelChangePropagator
        LogManager.getLogManager().reset();

        SLF4JBridgeHandler.removeHandlersForRootLogger(); // avoid having everything logged twice
        SLF4JBridgeHandler.install();

        Logger.getGlobal().setLevel(CONFIG /* -> INFO in SLF4J API */);
    }
}
