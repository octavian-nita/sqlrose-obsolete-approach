package eu.sqlrose.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Sep 13, 2016
 */
public class SqlRoseComponent extends CustomComponent {

    protected void style(Style style, Style... otherStyles) { style(this, style, otherStyles); }

    protected <T extends Component> T style(T component, Style style, Style... otherStyles) {
        if (component != null) {
            if (style != null) {
                component.addStyleName(style.styleName());
            }
            if (otherStyles != null) {
                for (Style s : otherStyles) {
                    if (s != null) {
                        component.addStyleName(s.styleName());
                    }
                }
            }
        }

        return component;
    }
}
