package net.lipecki.watson.tools;

import java.sql.ResultSet;

public interface EventsTableRowHandler {

    boolean isApplicable(final ResultSet eventRow) throws Exception;

    void handle(final ResultSet eventRow, final boolean dryRun) throws Exception;

}
