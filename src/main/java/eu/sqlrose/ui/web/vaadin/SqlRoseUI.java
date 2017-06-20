package eu.sqlrose.ui.web.vaadin;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
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
@Title("SQLrose")
@PreserveOnRefresh
public class SqlRoseUI extends UI {

    protected final Logger log = LoggerFactory.getLogger(SqlRoseUI.class);

    protected final I18n i18n = new I18n();

    public SqlRoseUI() {}

    public SqlRoseUI(Component content) { super(content); }

    @Override
    protected void init(VaadinRequest request) {

        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        // Do we need something similar?
        // Page.getCurrent().addBrowserWindowResizeListener((Page.BrowserWindowResizeListener) event -> {
        //     DashboardEventBus.post(new BrowserResizeEvent());
        // });

        updateContent();

        log.debug("SQLrose UI initialized");
    }

    private void updateContent() {
        final HorizontalLayout content = new HorizontalLayout();
        setContent(content);

        content.setSizeFull();

        Environment env = VaadinSession.getCurrent().getAttribute(Environment.class);
        for (DataSource dataSource : env.getDataSources()) {
            DataSourceWidget dsw = new DataSourceWidget(dataSource);
            content.addComponent(dsw);
        }
    }
}
