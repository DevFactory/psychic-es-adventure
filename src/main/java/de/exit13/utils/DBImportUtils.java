package de.exit13.utils;

import de.exit13.db.MySQLImpl;
import de.exit13.utils.configuration.Config;
import de.exit13.utils.configuration.MySQLConfig;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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
        String dbTable = "weatherstations";
        String fileName = "/data/rawdata/CLIMAT/_stations_list_CLIMAT_data.txt";

        importStations(dbTable, fileName);

/*

        dbTable = "climatedata";
        fileName = "/data/rawdata/CLIMAT/CLIMAT_RAW_200303.txt";

        String directory = "/data/rawdata/CLIMAT";
        String filter = "CLIMAT_RAW_20.*.txt";
        FileUtils fu = new FileUtils();
        ArrayList<String> fileList = null;
        try {
            fileList = fu.readDirectory(directory, filter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Read " + fileList.size() + " files from " + directory + ".");

        for(String file : fileList) {
            System.out.println("File: " + file);
            importData(dbTable, file);

        }
  */
    return "";
    }


    private void importStations(String dbTable, String fileName) {
        System.out.println(Config.ANSI_RED_FG + "mysql" + Config.ANSI_RESET +" importing data...");
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

                String[] pieces = line.split(";",-1);
                int piecesNumber = pieces.length;

                station_id = pieces[0].trim();
                if(station_id.equals("")) { station_id = "-9999";}

                station_name = pieces[1].trim();
                if(station_name.equals("")) { station_name = "N/A";}

                latitude = pieces[2].trim().replace(" ", "");
                if(latitude.equals("")) { latitude = "-9999";}

                longitude = pieces[3].trim().replace(" ","");
                if(longitude.equals("")) { longitude = "-9999";}

                elevation = pieces[4].trim();
                if(elevation.equals("")) { elevation = "-9999";}

                // 5 is some duplicate of 4, so skip it???

                country_name = pieces[6].trim();
                if(country_name.equals("")) { country_name = "N/A";}

                String fields[] = new String[] {"station_id", "station_name", "latitude", "longitude", "elevation", "country_name" };
                String values[] = new String[] {station_id, station_name, latitude, longitude, elevation, country_name };
                if( !dbRecordExists(dbTable, fields, values) ) {
                    String query = "insert into " + mySQLConfig.getDB() + "." + dbTable + " (station_id, station_name, latitude, longitude, elevation, country_name) values (?, ?, ?, ?, ?, ?)";
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


    private void importData(String dbTable, String fileName) {
        System.out.println(Config.ANSI_RED_FG + "mysql" + Config.ANSI_RESET +" importing stations...");
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

            int i = 0;
            for(String line : fileContent) {
                //WMO-Station ID; StationName;                               Latitude;  Longitude;       ;        ;Country

                String[] pieces = line.split(";", -1);
                int piecesNumber = pieces.length;

                Map<String, Object> mapping = new HashMap<String, Object>();
                int sign = +1;
                for (int j = 0; j < pieces.length; j++) {
                    // set all the zero value pieces to -9999
                    if (null == pieces[j] || pieces[j].isEmpty()) {
                        pieces[j] = "-9999";
                    }
                    //pieces[i] = pieces[i].replace(" ", "");
                }

                // for a reference where the values come from -> see /resources/datafile_format.txt
                try {
                    int year = parseInt(pieces[0]);
                    int month = parseInt(pieces[1]);
                    String station_id = pieces[2];
                    int mean_monthly_station_level_pressure = parseInt(pieces[4]);
                    sign = (parseInt(pieces[8]) > 0) ? -1 : +1;
                    int mean_monthly_air_temp = sign * parseInt(pieces[9]);
                    int total_monthly_precipitation = parseInt(pieces[19]);
                    int number_of_days_with_precipitation = parseInt(pieces[21]);
                    int total_monthly_sunshine = parseInt(pieces[23]);
                    int days_with_max_temp_gt_25 = parseInt(pieces[66]);
                    int days_with_max_temp_gt_30 = parseInt(pieces[67]);
                    int days_with_max_temp_gt_35 = parseInt(pieces[69]);
                    int days_with_max_temp_gt_40 = parseInt(pieces[70]);
                    int days_with_min_temp_lt_0 = parseInt(pieces[72]);
                    int days_with_max_temp_lt_0 = parseInt(pieces[73]);
                    sign = (parseInt(pieces[106]) > 0) ? -1 : +1;
                    int max_temp_per_month = sign * parseInt(pieces[107]);
                    sign = (parseInt(pieces[111]) > 0) ? -1 : +1;
                    int min_temp_per_month = sign * parseInt(pieces[112]);
                    int max_gust_wind_speed_per_month = parseInt(pieces[118]);
                    int number_of_days_with_thunderstorms = parseInt(pieces[121]);
                    int number_of_days_with_hail = parseInt(pieces[122]);
                    String fields[] = new String[]{"year", "month", "station_id", "mean_monthly_station_level_pressure", "mean_monthly_air_temp", "total_monthly_precipitation", "number_of_days_with_precipitation", "total_monthly_sunshine", "days_with_max_temp_gt_25", "days_with_max_temp_gt_30", "days_with_max_temp_gt_35", "days_with_max_temp_gt_40", "days_with_min_temp_lt_0", "days_with_max_temp_lt_0", "max_temp_per_month", "min_temp_per_month", "max_gust_wind_speed_per_month", "number_of_days_with_thunderstorms", "number_of_days_with_hail"};

                    Object values[] = new Object[]{year, month, station_id, mean_monthly_station_level_pressure, mean_monthly_air_temp, total_monthly_precipitation, number_of_days_with_precipitation, total_monthly_sunshine, days_with_max_temp_gt_25, days_with_max_temp_gt_30, days_with_max_temp_gt_35, days_with_max_temp_gt_40, days_with_min_temp_lt_0, days_with_max_temp_lt_0, max_temp_per_month, min_temp_per_month, max_gust_wind_speed_per_month, number_of_days_with_thunderstorms, number_of_days_with_hail};

                    try {
                        String query = "insert into " + mySQLConfig.getDB() + "." + dbTable + " (year,month,station_id,mean_monthly_station_level_pressure,mean_monthly_air_temp,total_monthly_precipitation,number_of_days_with_precipitation,total_monthly_sunshine,days_with_max_temp_gt_25,days_with_max_temp_gt_30,days_with_max_temp_gt_35,days_with_max_temp_gt_40,days_with_min_temp_lt_0,days_with_max_temp_lt_0,max_temp_per_month,min_temp_per_month,max_gust_wind_speed_per_month,number_of_days_with_thunderstorms,number_of_days_with_hail) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setInt(1, year);
                        preparedStatement.setInt(2, month);
                        preparedStatement.setString(3, station_id);
                        preparedStatement.setInt(4, mean_monthly_station_level_pressure);
                        preparedStatement.setInt(5, mean_monthly_air_temp);
                        preparedStatement.setInt(6, total_monthly_precipitation);
                        preparedStatement.setInt(7, number_of_days_with_precipitation);
                        preparedStatement.setInt(8, total_monthly_sunshine);
                        preparedStatement.setInt(9, days_with_max_temp_gt_25);
                        preparedStatement.setInt(10, days_with_max_temp_gt_30);
                        preparedStatement.setInt(11, days_with_max_temp_gt_35);
                        preparedStatement.setInt(12, days_with_max_temp_gt_40);
                        preparedStatement.setInt(13, days_with_min_temp_lt_0);
                        preparedStatement.setInt(14, days_with_max_temp_lt_0);
                        preparedStatement.setInt(15, max_temp_per_month);
                        preparedStatement.setInt(16, min_temp_per_month);
                        preparedStatement.setInt(17, max_gust_wind_speed_per_month);
                        preparedStatement.setInt(18, number_of_days_with_thunderstorms);
                        preparedStatement.setInt(19, number_of_days_with_hail);

                        preparedStatement.execute();
                    } catch (SQLException sqe) {
                    }

                    System.out.print("\r" + i + " lines of " + lineCount + " processed.");
                    i++;
                }
                catch(NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
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
