package eu.sqlrose.web;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * &quot;Backported&quot; from Servlet 4.0...
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Nov 9, 2017
 */
public class HttpFilter implements Filter {

    private transient FilterConfig config;

    protected FilterConfig getConfig() { return config; }

    @Override
    public void init(FilterConfig config) throws ServletException { this.config = config; }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest && response instanceof HttpServletResponse)) {
            throw new ServletException("non-HTTP request or response");
        }

        this.doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException { chain.doFilter(request, response); }

    protected String getInitParameter(String name) {
        final FilterConfig config = getConfig();
        if (config == null) {
            throw new IllegalStateException("E_FILTER_CONFIG_NOT_INITIALIZED");
        }

        return config.getInitParameter(name);
    }

    @Override
    public void destroy() {}
}
