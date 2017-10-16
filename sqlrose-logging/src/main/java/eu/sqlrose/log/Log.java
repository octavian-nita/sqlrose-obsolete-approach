package eu.sqlrose.log;

import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.LogManager;
import java.util.logging.Logger;

import static java.util.logging.Level.CONFIG;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Oct 16, 2017
 */
public class Log {

    public static void init() { routeJulToSlf4j(); }

    /**
     * Route any j.u.l logging request to SLF4J.
     *
     * @see <a href="http://blog.cn-consult.dk/2009/03/bridging-javautillogging-to-slf4j.html">
     * http://blog.cn-consult.dk/2009/03/bridging-javautillogging-to-slf4j.html</a>
     * @see <a href="http://stackoverflow.com/questions/9117030/jul-to-slf4j-bridge">
     * http://stackoverflow.com/questions/9117030/jul-to-slf4j-bridge</a>
     * @see <a href="https://logback.qos.ch/manual/configuration.html#LevelChangePropagator">
     * https://logback.qos.ch/manual/configuration.html#LevelChangePropagator</a>
     */
    private static void routeJulToSlf4j() {
        // We might not need this though if we're installing a LevelChangePropagator
        LogManager.getLogManager().reset();

        SLF4JBridgeHandler.removeHandlersForRootLogger(); // avoid having everything logged twice
        SLF4JBridgeHandler.install();

        Logger.getGlobal().setLevel(CONFIG /* -> INFO in SLF4J API */);
    }
}
