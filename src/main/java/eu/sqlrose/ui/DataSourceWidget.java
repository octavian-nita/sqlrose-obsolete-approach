package eu.sqlrose.ui;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import eu.sqlrose.core.DataSource;

import static com.vaadin.server.FontAwesome.DATABASE;
import static com.vaadin.server.Sizeable.Unit.PERCENTAGE;
import static com.vaadin.shared.ui.label.ContentMode.HTML;
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

        Label label = new Label(DATABASE.getHtml() + " " + dsName, HTML);
        label.setDescription(dsDesc);

        setWidth(100, PERCENTAGE);
        style(SELECTABLE);
        setCompositionRoot(style(new HorizontalLayout(label), SELECTABLE, W_DATA_SOURCE));
    }
}
