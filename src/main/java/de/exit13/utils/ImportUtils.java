package de.exit13.utils;

import de.exit13.db.MysqlImpl;
import de.exit13.utils.Configuration.Config;
import de.exit13.utils.Configuration.MySQLConfig;

import java.sql.Connection;

/**
 * Created by elshotodore on 12.03.15.
 */
public class ImportUtils {


    public String mysqlImport( ) {

        System.out.println(Config.ANSI_RED_FG + "mysql" + Config.ANSI_RESET +" import...");
        Connection connection;

        MySQLConfig mySQLConfig = new MySQLConfig();
        MysqlImpl mysql = new MysqlImpl();

        String user = "sqluser";
        String password= "sqluserpw";
        String server = "localhost";
        String db = "climatedata";
        String table = "countries";

        System.out.println(Config.ANSI_GREEN_FG + "Done!" + Config.ANSI_RESET);
        return "";
    }

    public void elasticImport() {
        System.out.println(Config.ANSI_RED_FG + "elastic" + Config.ANSI_RESET +" import...");

        System.out.println(Config.ANSI_GREEN_FG + "Done!" + Config.ANSI_RESET);
    }
}
