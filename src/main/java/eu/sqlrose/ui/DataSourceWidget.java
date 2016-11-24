package eu.sqlrose.ui;

import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import eu.sqlrose.core.DataSource;

import static com.vaadin.server.FontAwesome.DATABASE;
import static com.vaadin.ui.themes.ValoTheme.BUTTON_LINK;
import static eu.sqlrose.ui.Style.SELECTABLE;
import static eu.sqlrose.ui.Style.W_DATA_SOURCE;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Sep 13, 2016
 */
public class DataSourceWidget extends SqlRoseComponent {

    private DataSource dataSource;

    public DataSourceWidget() { this(null); }

    public DataSourceWidget(DataSource dataSource) {
        this.dataSource = dataSource;

        final String dsName = dataSource == null ? "" : dataSource.getName();
        final String dsDesc =
            dataSource == null ? "" : dataSource.getDescription() == null ? "" : dataSource.getDescription();

        Button bt = new Button(dsName, DATABASE);
        bt.setDescription(dsDesc);
        bt.addStyleName(BUTTON_LINK);
        bt.addClickListener((ClickListener) event -> Page.getCurrent().setUriFragment(dsName));

        setCompositionRoot(style(new HorizontalLayout(bt), SELECTABLE, W_DATA_SOURCE));
    }
}
