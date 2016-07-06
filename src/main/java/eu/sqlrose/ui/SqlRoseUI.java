package eu.sqlrose.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 05, 2016
 */
@Theme("valo")
public class SqlRoseUI extends UI {

    private static final long serialVersionUID = 1020160705L;

    private final Logger LOG = LoggerFactory.getLogger(SqlRoseUI.class);

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        setContent(content);

        content.addComponent(new Label("Hello SqlRose!"));

        LOG.info("SqlRose UI initialized");
    }
}
