package eu.sqlrose.ui;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static org.apache.commons.lang3.StringUtils.appendIfMissing;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 07, 2016
 */
public interface I18n {

    String TEXT_BUNDLE_BASE = "TextBundle";

    String TEXT_BUNDLE = appendIfMissing(System.getProperty("bundlePrefix", "locales/").trim(), "/") + TEXT_BUNDLE_BASE;

    Locale getLocale();

    default String t(String key) {
        if (key == null) {
            return "!?";
        }

        Locale locale = getLocale();
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
