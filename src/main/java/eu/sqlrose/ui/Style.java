package eu.sqlrose.ui;

/**
 * Wraps {@link com.vaadin.ui.Component#addStyleName(String) style names}, managing common prefixes.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Sep 13, 2016
 */
public enum Style {

    //
    // General, transversal styles:
    //

    SELECTABLE,

    //
    // (Custom) Widgets:
    //

    W_DATA_SOURCE("w-ds");

    public static final String PREFIX = "sr-";

    Style() { this(null); }

    Style(String name) { this(PREFIX, name); }

    Style(String prefix, String name) {
        prefix = prefix == null ? "" : prefix.trim();

        if (name == null || (name = name.trim()).length() == 0) {
            name = prefix + String.join("-", name().toLowerCase().split("_+"));
        }
        if (prefix.length() != 0 && !name.startsWith(prefix)) {
            name = prefix + name;
        }

        this.name = name;
    }

    private final String name;

    public String styleName() { return name; }
}
