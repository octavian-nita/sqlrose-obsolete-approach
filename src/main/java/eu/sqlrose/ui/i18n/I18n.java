package eu.sqlrose.ui.i18n;

import com.vaadin.server.VaadinSession;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 07, 2016
 */
public class I18n {

    protected final String bundlePrefix;

    public I18n() { this(null); }

    public I18n(String bundlePrefix) {
        if (bundlePrefix == null) {
            bundlePrefix = System.getProperty("sqlrose.l10n.prefix", "locales/").trim();
        } else {
            bundlePrefix = bundlePrefix.trim();
        }
        if (bundlePrefix.length() > 0 && !bundlePrefix.endsWith("/")) {
            bundlePrefix += "/";
        }
        this.bundlePrefix = bundlePrefix;
    }

    protected Locale locale;

    public I18n setLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    public Locale getLocale() { return locale == null ? VaadinSession.getCurrent().getLocale() : locale; }

    public String t(String key, Object... args) { return t(key, null, args); }

    public String t(String key, Locale locale, Object... args) {
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
            ResourceBundle bundle = ResourceBundle.getBundle(bundlePrefix + "Text", locale);

            return args == null || args.length == 0
                   ? bundle.getString(key)
                   : new MessageFormat(bundle.getString(key), locale).format(args);

        } catch (ClassCastException | MissingResourceException ex) {
            LoggerFactory.getLogger(getClass()).warn("Cannot translate text resource for key " + key, ex);
            return "!" + key;
        }
    }
}
