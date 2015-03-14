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
public class DBImportUtils {

    MySQLConfig mySQLConfig = new MySQLConfig();
    MySQLImpl mysql = new MySQLImpl();
    Connection connection = null;

    public String mysqlImport( ) {
        connection = mysql.openConnection(mySQLConfig.getDB_USER(), mySQLConfig.getDB_PASSWORD(), mySQLConfig.getDB_SERVER(), mySQLConfig.getDB());
        String dbTable = "weatherstations_WMO";
        String fileName = "/data/rawdata/CLIMAT/aaa_stations_list_CLIMAT_data.txt";

        importStations(dbTable, fileName);
        return "";
    }


    private void importStations(String dbTable, String fileName) {
        System.out.println(Config.ANSI_RED_FG + "mysql" + Config.ANSI_RESET +" importing stations...");
        try {
            List<String> fileContent = new ArrayList<String>();
            int lineCount = 0;
            FileUtils fileUtils = new FileUtils();
            PreparedStatement preparedStatement;
            try {
                fileContent = fileUtils.readFileContent(fileName);
                lineCount = fileContent.size()-1; // skip first line containing header info
            } catch (IOException e) {
                e.printStackTrace();
            }
            String station_id = "-9999";
            String station_name = "n/a";
            String latitude = "n/a";
            String longitude = "n/a";
            String elevation = "-9999";
            String country_name = "n/a";
            int i = 0;
            for(String line : fileContent) {
                //WMO-Station ID; StationName;                               Latitude;  Longitude;       ;        ;Country

                String[] pieces = line.split(";");
                int piecesNumber = pieces.length;
                station_id = pieces[0].trim();
                station_name = pieces[1].trim();
                latitude = pieces[2].trim().replace(" ","");
                longitude = pieces[3].trim().replace(" ","");
                elevation = pieces[4].trim();
                if(elevation.equals("")) { elevation = "-9999";}
                // 5 is some strange duplicate of 4
                country_name = pieces[6].trim();


                String fields[] = new String[] {"station_id", "station_name", "latitude", "longitude", "elevation", "country_name" };
                String values[] = new String[] {station_id, station_name, latitude, longitude, elevation, country_name };
                if( !dbRecordExists(dbTable, fields, values) ) {
                    String query = "insert into " + mySQLConfig.getDB() + "." + dbTable + " (station_id, station_name, latitude, longitude, elevation, country_name) values (?, ?, ?, ?, ?, ?)";
                    preparedStatement = connection.prepareStatement(query);

                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, station_id);
                    preparedStatement.setString(2, station_name);
                    preparedStatement.setString(3, latitude);
                    preparedStatement.setString(4, longitude);
                    preparedStatement.setString(5, elevation);
                    preparedStatement.setString(6, country_name);
                    preparedStatement.execute();

                    System.out.print("\r" + i + " lines of " + lineCount + " processed.");
                    //if(i>2) { break;}
                    i++;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("\n" +Config.ANSI_GREEN_FG + "Done!" + Config.ANSI_RESET);
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
        String whereClause = " WHERE ";
        //does it exist already?
        selectClause = "SELECT * FROM " + mySQLConfig.getDB() + "." + dbTable + "";

        int i = 0;
        for(String field : fields) {
            whereClause +="'" + field + "' =  ?";
            if(i<fields.length-1) { whereClause += " AND ";}
            i++;
        }
        PreparedStatement preparedStatement = connection.prepareStatement(selectClause + whereClause);
        i = 1;
        for(String field : fields) {
            preparedStatement.setString(i, values[i-1]);
            i++;
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        if(mysql.resultToList(resultSet).size() == 0) {
            answer = false;
        }
        return answer;
    }
}
