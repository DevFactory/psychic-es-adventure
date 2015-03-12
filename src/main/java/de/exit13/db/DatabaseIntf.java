package de.exit13.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by elshotodore on 12.03.15.
 */
public interface DatabaseIntf {

    // dealing with the db
    Connection openConnection(String user, String password, String server, String db);
    boolean closeConnection(Connection connection);


    // some useful stuff
    List<List<String>> resultToList(ResultSet resultSet) throws SQLException;

    // some not so useful stuff
    void initialImport() throws SQLException;
}
