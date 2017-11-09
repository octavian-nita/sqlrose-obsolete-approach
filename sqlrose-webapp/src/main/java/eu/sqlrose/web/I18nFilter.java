package eu.sqlrose.web;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Detects whether the current <code>request</code> contains a <em>language tag</em> parameter and if yes, tries to
 * {@link Locale#forLanguageTag(String) retrieve} a corresponding {@link Locale} and set it as attribute on the current
 * {@link javax.servlet.http.HttpSession session} under the same name as the language tag parameter.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.1, Oct 17, 2017
 */
@WebFilter(filterName = "sqlrose-i18n-filter", urlPatterns = {"/*"},
           initParams = @WebInitParam(name = "languageTagParam", value = "lang"))
public class I18nFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        String languageTagParam = getInitParameter("languageTagParam");
        if (languageTagParam == null) {
            languageTagParam = "lang";
        }

        String languageTag = request.getParameter(languageTagParam);
        if (languageTag != null) {
            Locale locale = Locale.forLanguageTag(languageTag.trim());
            if (locale != null) {
                request.getSession(true).setAttribute(languageTagParam, locale);
            }
        }

        chain.doFilter(request, response);
    }
}
