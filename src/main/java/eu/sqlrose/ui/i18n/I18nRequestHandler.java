package eu.sqlrose.ui.i18n;

import com.vaadin.server.RequestHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;

import java.util.Locale;

/**
 * {@link RequestHandler} that detects whether the current <code>request</code> contains a parameter named
 * {@link #LANGUAGE_TAG_PARAM}; if yes, it tries to {@link Locale#forLanguageTag(String) retrieve} a corresponding
 * {@link Locale} and set it as the current <code>session</code>'s {@link VaadinSession#setLocale(Locale) locale}.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 07, 2016
 */
public class I18nRequestHandler implements RequestHandler {

    public static final String LANGUAGE_TAG_PARAM = "lang";

    @Override
    public boolean handleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response) {

        String languageTag = request.getParameter(LANGUAGE_TAG_PARAM);
        if (languageTag != null) {
            Locale locale = Locale.forLanguageTag(languageTag.trim());
            if (locale != null) {
                session.setLocale(locale);
            }
        }

        return false;
    }
}
