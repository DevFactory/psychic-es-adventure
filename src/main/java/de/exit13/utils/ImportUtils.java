package de.exit13.utils;

import de.exit13.db.MySQLImpl;
import de.exit13.utils.Configuration.Config;
import de.exit13.utils.Configuration.MySQLConfig;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by elshotodore on 12.03.15.
 */
public class ImportUtils {

    MySQLConfig mySQLConfig = new MySQLConfig();
    MySQLImpl mysql = new MySQLImpl();
    Connection connection = null;

    public String mysqlImport( ) {
        connection = mysql.openConnection(mySQLConfig.getDB_USER(), mySQLConfig.getDB_PASSWORD(), mySQLConfig.getDB_SERVER(), mySQLConfig.getDB());
        importCountries();
        importWeatherStations();
        return "";
    }


    private void importCountries() {
        System.out.println(Config.ANSI_RED_FG + "mysql" + Config.ANSI_RESET +" importing countries...");
        String dbTable = "countries";
        try {
            List<String> fileContent = new ArrayList<String>();;
            FileUtils fileUtils = new FileUtils();
            PreparedStatement preparedStatement;
            try {
                fileContent = fileUtils.fileToList("/data/rawdata/country-id.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String countryCode;
            String countryName;
            for(String line : fileContent) {
                String[] pieces = line.split(";");
                countryCode = pieces[0];
                countryName = pieces[1];
                String fields[] = new String[] {"countryCode", "countryName"};
                String values[] = new String[] {countryCode,countryName};
                if(!dbRecordExists(dbTable, fields, values)) {
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
        System.out.println(Config.ANSI_GREEN_FG + "Done!" + Config.ANSI_RESET);

    }

    private void importWeatherStations() {
        System.out.println(Config.ANSI_RED_FG + "mysql" + Config.ANSI_RESET +" importing weather stations...");
        String dbTable = "weatherstations";
        try {
            List<String> fileContent = new ArrayList<String>();;
            FileUtils fileUtils = new FileUtils();
            PreparedStatement preparedStatement;
            try {
                fileContent = fileUtils.fileToList("/data/rawdata/station-list.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String wban;
            String name;
            String countryCode;
            String latitude;
            String longitude;
            String elevation;

            for(String line : fileContent) {
                String[] pieces = line.split(";");

                wban = pieces[0].trim();
                name = pieces[1].trim();
                countryCode = pieces[2].trim();
                latitude = pieces[3].trim();
                longitude = pieces[4].trim();
                elevation = pieces[5].trim();

                String fields[] = new String[] {"wban", "name", "countryCode", "latitude", "longitude", "elevation" };
                String values[] = new String[] {wban, name, countryCode, latitude, longitude, elevation };
                if(!dbRecordExists(dbTable, fields, values)) {
                    String query = " insert into " + mySQLConfig.getDB() + "." + dbTable + " (wban, name, countryCode, latitude, longitude, elevation) values (?, ?, ?, ?, ?, ?)";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, wban);
                    preparedStatement.setString(2, name);
                    preparedStatement.setString(3, countryCode);
                    preparedStatement.setString(4, latitude);
                    preparedStatement.setString(5, longitude);
                    preparedStatement.setInt(6, parseInt(elevation));
                    preparedStatement.execute();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(Config.ANSI_GREEN_FG + "Done!" + Config.ANSI_RESET);
        System.out.println(Config.ANSI_GREEN_FG + "Done!" + Config.ANSI_RESET);
    }

    /**
     * Check if a record exists in the db by selecting everything where all fields = values match.
     * */
    private boolean dbRecordExists(String dbTable, String[] fields, String[] values) throws SQLException {
        boolean answer = true;
        if(fields.length != values.length) {
            System.out.println("dbRecordExists -> fields[] and values[] have to have the same size!");
            System.exit(13);
        }
        String selectClause = "";
        String whereClause = " where ";
        //does it exist already?
        selectClause = "SELECT * from " + mySQLConfig.getDB() + "." + dbTable + "";

        int i = 0;
        for(String field : fields) {
            whereClause += field +" = '" + values[i] + "' ";
            if(i<fields.length-1) { whereClause += " AND ";}
            i++;
        }
        PreparedStatement preparedStatement = connection.prepareStatement(selectClause + whereClause);
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
