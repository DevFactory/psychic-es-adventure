package de.exit13.db;

import de.exit13.utils.FileUtils;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by elshotodore on 12.03.15.
 */
public class MySqlImpl implements DatabaseIntf {
    private Connection connection;
    String user ="sqluser";
    String password="sqluserpw";
    String server = "localhost";
    String db = "climatedata";
    String table = "countries";

    public void initialImport() throws SQLException {
        System.out.println("initialImport");
        connection = openConnection(user, password, server, db);

        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet resultSet = null;
        try {
            // INSERT INTO countries values (default, 'AG', 'Argentina');C

            String countryCode = "XX";
            String countryName = "ABC 123";
            List<String> fileContent;
            FileUtils fileUtils = new FileUtils();
            PreparedStatement preparedStatement;

            try {
                fileContent = fileUtils.fileToList("/data/rawdata/country-id.txt");
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
            String country_id;
            String country_name;
            for(String line : fileContent) {
                String[] pieces = line.split(";");
                country_id = pieces[0];
                country_name = pieces[1];

                if(!recordExists("country_id", country_id)) {
                    String query = " insert into " + db + "." + table + " (country_id, country_name) values (?, ?)";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, country_id);
                    preparedStatement.setString(2, country_name);

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
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from " + db + "." + table + " where " + fieldName + " = '" + fieldValue + "'");
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
