package eu.sqlrose.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.1, Oct 17, 2017
 */
@WebServlet(name = "sqlrose-servlet", value = "/*", asyncSupported = true)
public class SqlRoseServlet extends HttpServlet {

    private final Logger log = LoggerFactory.getLogger(SqlRoseServlet.class);

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        String qs = request.getQueryString();
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
}
