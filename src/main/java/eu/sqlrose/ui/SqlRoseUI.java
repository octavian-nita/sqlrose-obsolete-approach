package eu.sqlrose.ui;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import eu.sqlrose.core.DataSource;
import eu.sqlrose.env.Environment;
import eu.sqlrose.ui.i18n.I18n;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 05, 2016
 */
@Theme("sqlrose")
@PreserveOnRefresh
public class SqlRoseUI extends UI {

    protected final Logger log = LoggerFactory.getLogger(SqlRoseUI.class);

    protected final I18n i18n = new I18n();

    public SqlRoseUI() {
        setErrorHandler(new SqlRoseErrorHandler());
    }

    public SqlRoseUI(Component content) {
        super(content);
        setErrorHandler(new SqlRoseErrorHandler());
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        setContent(content);

        Environment env = VaadinSession.getCurrent().getAttribute(Environment.class);
        for (DataSource dataSource : env.getDataSources()) {
            DataSourceWidget dsw = new DataSourceWidget(dataSource);
            content.addComponent(dsw);
        }

        log.info("SqlRose UI initialized");
    }
}
