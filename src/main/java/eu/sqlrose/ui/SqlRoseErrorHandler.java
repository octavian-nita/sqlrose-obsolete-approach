package eu.sqlrose.ui;

import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.ErrorEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import eu.sqlrose.core.ErrorCode;
import eu.sqlrose.core.SqlRoseException;
import eu.sqlrose.ui.eventlog.EventLog;

import static com.vaadin.shared.Position.BOTTOM_RIGHT;
import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;
import static com.vaadin.ui.Notification.Type.WARNING_MESSAGE;

/**
 * Does not, by default, set the component error for the component where the initial exception was thrown.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Sep 26, 2016
 */
public class SqlRoseErrorHandler extends DefaultErrorHandler {

    protected final EventLog eventLog;

    public SqlRoseErrorHandler() { this(null); }

    public SqlRoseErrorHandler(EventLog eventLog) { this.eventLog = eventLog; }

    @Override
    public final void error(ErrorEvent event) {
        Throwable throwable = event.getThrowable();

        if (throwable != null) {
            throwable = findRelevantThrowable(throwable);
        }

        if (throwable != null && throwable instanceof SqlRoseException) {
            handle((SqlRoseException) throwable);
        } else {
            handle(throwable);
        }
    }

    protected void handle(SqlRoseException exception) {
        // Obtain a message for the user:
        ErrorCode errCode = exception.getCode();
        if (errCode != null) {
            errCode = ErrorCode.E_GENERIC;
        } else {

            Object[] details = exception.getDetails();
        }

        final Notification notification = new Notification("", WARNING_MESSAGE);

        if (exception.isInternal()) {
            notification.setDelayMsec(2000);
        } else {
            notification.setDelayMsec(4000);
            notification.setStyleName(ERROR_MESSAGE.getStyle());
        }
        notification.setPosition(BOTTOM_RIGHT);
        notification.setHtmlContentAllowed(true);

        notification.show(Page.getCurrent());
    }

    /**
     * @param throwable can be <code>null</code>
     */
    protected void handle(Throwable throwable) {
        if (throwable == null) {
        } else {

        }
    }
}
