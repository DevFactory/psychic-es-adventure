package de.exit13.db;

import de.exit13.utils.Configuration.MySQLConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by elshotodore on 12.03.15.
 */
public class MySQLImpl implements DatabaseIntf {
    private Connection connection = null;
    private MySQLConfig mySQLConfig = new MySQLConfig();
    private PreparedStatement preparedStatement;


    @Override
    public Connection openConnection(String dbUser, String dbPass, String dbServer, String db) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + mySQLConfig.getDB_SERVER() + "/" + mySQLConfig.getDB() + "?" + "user=" + mySQLConfig.getDB_USER() + "&password=" + mySQLConfig.getDB_PASSWORD());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public boolean closeConnection(Connection connection) {
        return false;
    }

    @Override
    public List<List<String>> resultToList(ResultSet resultSet) throws SQLException {
        List<List<String>> resultMap = new ArrayList<List<String>>();
        ResultSetMetaData metadata = null;
        int numberOfColumns = 0;
        int numberOfRows = 0;
        metadata = resultSet.getMetaData();
        numberOfColumns = metadata.getColumnCount();

        while (resultSet.next()) {
            List<String> resultRow = new ArrayList<String>();
            for (int i = 1; i <= numberOfColumns; i++) {
                resultRow.add(resultSet.getString(i));
            }
            resultMap.add(resultRow);
            numberOfRows++;
        }
        return resultMap;
    }
}
