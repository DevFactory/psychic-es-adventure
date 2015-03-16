package de.exit13.utils.Configuration;

/**
 * Created by elshotodore on 12.03.15.
 */
public class MySQLConfig {
    public static String DB_USER = "sqluser";
    public static String DB_PASSWORD = "sqluserpw";
    public static String DB_SERVER = "localhost";
    public static String DB = "climatedata";

    public MySQLConfig() {
        new MySQLConfig(DB_USER, DB_PASSWORD, DB_SERVER, DB);
    }

    public MySQLConfig(String DB_USER, String DB_PASSWORD, String DB_SERVER, String DB) {
        this.DB_USER = DB_USER;
        this.DB_PASSWORD = DB_PASSWORD;
        this.DB_SERVER = DB_SERVER;
        this.DB = DB;
    }

    public String getDB_USER() {
        return DB_USER;
    }

    public void setDB_USER(String DB_USER) {
        this.DB_USER = DB_USER;
    }

    public String getDB_PASSWORD() {
        return DB_PASSWORD;
    }

    public void setDB_PASSWORD(String DB_PASSWORD) {
        this.DB_PASSWORD = DB_PASSWORD;
    }

    public String getDB_SERVER() {
        return DB_SERVER;
    }

    public void setDB_SERVER(String DB_SERVER) {
        this.DB_SERVER = DB_SERVER;
    }

    public String getDB() {
        return DB;
    }

    public void setDB(String DB) {
        this.DB = DB;
    }
}
