package eu.sqlrose.ui;

import com.vaadin.server.RequestHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;

import java.util.Locale;

/**
 * {@link RequestHandler Request handler} that detects whether the current <code>request</code> contains a parameter
 * named {@link #LANGUAGE_TAG_NAME}; if yes it tries to interpret the value as a {@link Locale#forLanguageTag(String)
 * language tag} and set the current <code>session</code>'s {@link Locale} accordingly.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 07, 2016
 */
public class LocaleSensitiveRequestHandler implements RequestHandler {

    public static final String LANGUAGE_TAG_NAME = "lang";

    @Override
    public boolean handleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response) {

        String languageTag = request.getParameter(LANGUAGE_TAG_NAME);
        if (languageTag != null) {
            Locale locale = Locale.forLanguageTag(languageTag.trim());
            if (locale != null) {
                session.setLocale(locale);
            }
        }

        return false;
    }
}
