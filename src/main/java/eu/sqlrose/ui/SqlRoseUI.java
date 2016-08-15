package eu.sqlrose.ui;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import eu.sqlrose.core.DataSource;
import eu.sqlrose.core.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 05, 2016
 */
@Theme("sqlrose")
@PreserveOnRefresh
public class SqlRoseUI extends UI implements I18n {

    protected final Logger log = LoggerFactory.getLogger(SqlRoseUI.class);

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        setContent(content);

        Environment env = VaadinSession.getCurrent().getAttribute(Environment.class);
        for (DataSource dataSource : env.getDataSources()) {
            Label l = new Label();

            l.setIcon(FontAwesome.DATABASE);
            l.setCaption(dataSource.toString());

            content.addComponent(l);
        }

        log.info("SqlRose UI initialized");
    }
}
