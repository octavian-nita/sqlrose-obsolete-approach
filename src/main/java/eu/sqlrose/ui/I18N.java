package eu.sqlrose.ui;

import com.vaadin.server.VaadinSession;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 07, 2016
 */
public class I18N {

    static {
        String bundlePrefix = System.getProperty("bundlePrefix", "locales").trim();

        TEXT_BUNDLE = bundlePrefix.length() > 0 ? bundlePrefix + "/TextBundle" : "TextBundle";
    }

    public static final String TEXT_BUNDLE;

    public static String t(String key) {
        if (key == null) {
            return "!?";
        }

        VaadinSession session = VaadinSession.getCurrent();
        if (session == null) {
            return "!" + key;
        }

        Locale locale = session.getLocale();
        if (locale == null) {
            return "!" + key;
        }

        try {
            return ResourceBundle.getBundle(TEXT_BUNDLE, locale).getString(key);
        } catch (ClassCastException | MissingResourceException ex) {
            return "!" + key;
        }
    }
}
