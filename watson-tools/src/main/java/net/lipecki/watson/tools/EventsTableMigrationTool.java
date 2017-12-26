package net.lipecki.watson.tools;

import ch.qos.logback.classic.Level;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class EventsTableMigrationTool {

    private List<EventsTableRowHandler> handlers = new LinkedList<>();

    public EventsTableMigrationTool(final String[] args) {
        log.info("EventsTableMigrationTool [args={}]", args);
        this.handlers.add(new ChangeEventTypeNameHandler());
        this.handlers.add(new ChangePayloadClassHandler());
    }

    public static void main(String[] args) {
        try {
            ((ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
            new EventsTableMigrationTool(args).run(true);
        } catch (final Exception ex) {
            log.error("Tool failed with exception [ex={}]", ex.getMessage(), ex);
            System.exit(1);
        }
    }

    public void run(final boolean dryRun) throws Exception {
        log.info("Starting tool");

        final String url = "jdbc:h2:file:./watson";
        final String user = "sa";
        final String pass = "";

        log.debug("Connecting to H2 [url={}, user={}, pass={}]", url, user, pass);
        final Connection conn = DriverManager.getConnection(url, user, pass);
        conn.setAutoCommit(false);
        log.trace("JDBC connected [connection={}]", conn);

        try {
            final Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            final ResultSet eventRow = statement.executeQuery("select * from events");
            while (eventRow.next()) {
                boolean changes = false;
                for (final EventsTableRowHandler handler : this.handlers) {
                    final boolean applicable = handler.isApplicable(eventRow);
                    final String handlerClass = handler.getClass().getSimpleName();
                    log.trace("Checking handler [handler={}, applicable={}]", handlerClass, applicable);
                    if (applicable) {
                        log.debug("Running event row handler [handler={}]", handlerClass);
                        handler.handle(eventRow, dryRun);
                    }
                    changes = true;
                }
                if (changes) {
                    eventRow.updateRow();
                }
            }
        } catch (final Exception ex) {
            conn.rollback();
            throw ex;
        }
        if (!dryRun) {
            conn.commit();
        }
    }

}
