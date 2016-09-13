package eu.sqlrose.ui;

/**
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

    W_DATA_SOURCE("sr-w-ds");

    Style() { this(null); }

    Style(String name) {
        if (name == null || (name = name.trim()).length() == 0) {
            name = "sr-" + String.join("-", name().toLowerCase().split("_+"));
        }
        this.name = name;
    }

    private final String name;

    public String styleName() { return name; }
}
