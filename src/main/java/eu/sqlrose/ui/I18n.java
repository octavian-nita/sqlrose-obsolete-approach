package eu.sqlrose.ui;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 07, 2016
 */
public interface I18n {

    String TEXT_BUNDLE_BASE = "TextBundle";

    String TEXT_BUNDLE =
        StringUtils.appendIfMissing(System.getProperty("sqlrose.l10n.prefix", "locales/").trim(), "/") +
        TEXT_BUNDLE_BASE;

    Locale getLocale();

    default String t(String key, Object... args) { return t(key, null, args); }

    default String t(String key, Locale locale, Object... args) {
        if (key == null) {
            return "!?";
        }

        if (locale == null) {
            locale = getLocale();
            if (locale == null) {
                return "!" + key;
            }
        }

        try {
            ResourceBundle bundle = ResourceBundle.getBundle(TEXT_BUNDLE, locale);

            return args == null || args.length == 0
                   ? bundle.getString(key)
                   : new MessageFormat(bundle.getString(key), locale).format(args);

        } catch (ClassCastException | MissingResourceException ex) {
            LoggerFactory.getLogger(getClass()).warn("Cannot translate text resource for key " + key, ex);

            return "!" + key;

        }
    }
}
