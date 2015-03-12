package de.exit13.utils;

import de.exit13.db.MySQLImpl;
import de.exit13.utils.Configuration.Config;
import de.exit13.utils.Configuration.MySQLConfig;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by elshotodore on 12.03.15.
 */
public class ImportUtils {

    MySQLConfig mySQLConfig = new MySQLConfig();
    MySQLImpl mysql = new MySQLImpl();
    Connection connection = null;

    public String mysqlImport( ) {
        connection = mysql.openConnection(mySQLConfig.getDB_USER(), mySQLConfig.getDB_PASSWORD(), mySQLConfig.getDB_SERVER(), mySQLConfig.getDB());
        System.out.println(Config.ANSI_RED_FG + "mysql" + Config.ANSI_RESET +" import...");
        Connection connection = null;
        Statement statement = null;
        MySQLConfig mySQLConfig = new MySQLConfig();
        MySQLImpl mysql = new MySQLImpl();

        String dbTable = "countries";

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

                if(!dbRecordExists(dbTable, "countryCode", countryCode)) {
                    String query = " insert into " + mySQLConfig.getDB() + "." + dbTable + " (countryCode, countryName) values (?, ?)";
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
            try {
                resultList = mysql.resultToList(resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        for(int i = 0; i < resultList.size(); i++) {
            System.out.println(resultList.get(i));
        }
        System.out.println(Config.ANSI_GREEN_FG + "Done!" + Config.ANSI_RESET);
        return "";
    }
    private boolean dbRecordExists(String dbTable, String fieldName, String fieldValue) throws SQLException {
        boolean answer = true;
        //does it exist already?
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from " + mySQLConfig.getDB() + "." + dbTable + " where " + fieldName + " = '" + fieldValue + "'");
        ResultSet resultSet = preparedStatement.executeQuery();
        if(mysql.resultToList(resultSet).size() == 0) {
            answer = false;
        }
        return answer;
    }



    public void elasticImport() {
        System.out.println(Config.ANSI_RED_FG + "elastic" + Config.ANSI_RESET +" import...");

        System.out.println(Config.ANSI_GREEN_FG + "Done!" + Config.ANSI_RESET);
    }
}
