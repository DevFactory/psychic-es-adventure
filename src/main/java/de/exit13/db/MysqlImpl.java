package de.exit13.db;

import de.exit13.utils.Configuration.MySQLConfig;
import de.exit13.utils.FileUtils;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by elshotodore on 12.03.15.
 */
public class MysqlImpl implements DatabaseIntf {
    private Connection connection;
    private MySQLConfig mysqlConfig = new MySQLConfig();
    private PreparedStatement preparedStatement;

    private String dbTable;

    public void initialImport() throws SQLException {
        connection = openConnection(mysqlConfig.getDB_USER(), mysqlConfig.getDB_PASSWORD(), mysqlConfig.getDB_SERVER(), mysqlConfig.getDB());
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet resultSet = null;
        try {
            // INSERT INTO countries values (default, 'AG', 'Argentina');C

            List<String> fileContent;
            FileUtils fileUtils = new FileUtils();
            PreparedStatement preparedStatement;

            try {
                fileContent = fileUtils.fileToList("/data/rawdata/country-id.txt");
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
            String countryCode;
            String countryName;
            for(String line : fileContent) {
                String[] pieces = line.split(";");
                countryCode = pieces[0];
                countryName = pieces[1];

                if(!recordExists("countryCode", countryCode)) {
                    String query = " insert into " + mysqlConfig.getDB() + "." + dbTable + " (countryCode, countryName) values (?, ?)";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, countryCode);
                    preparedStatement.setString(2, countryName);

                    preparedStatement.execute();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<List<String>> resultList = new ArrayList<List<String>>();
        if(resultSet != null) {
            resultList = resultToList(resultSet);

        }

        for(int i = 0; i < resultList.size(); i++) {
            System.out.println(resultList.get(i));
        }

    }

    private boolean recordExists(String fieldName, String fieldValue) throws SQLException {
        boolean answer = true;
        //does it exist already?
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from " + mysqlConfig.getDB() + "." + dbTable + " where " + fieldName + " = '" + fieldValue + "'");
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultToList(resultSet).size() == 0) {
            answer = false;
        }
        return answer;
    }

    @Override
    public Connection openConnection(String user, String password, String server, String db) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + server + "/" + db + "?" + "user=" + user + "&password=" + password);
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
