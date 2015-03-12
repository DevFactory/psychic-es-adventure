package de.exit13.utils;

import de.exit13.Config;

import java.sql.Connection;

import static de.exit13.utils.enums.Utils.done;

/**
 * Created by elshotodore on 12.03.15.
 */
public class ImportUtils {

    public void mySqlImport( ) {
        System.out.println(Config.ANSI_RED_FG + "mysql " + Config.ANSI_RESET +" import...");
        Connection connection;
        String user = "sqluser";
        String password= "sqluserpw";
        String server = "localhost";
        String db = "climatedata";
        String table = "countries";
        done();
    }

    public void elasticImport() {
        System.out.println(Config.ANSI_RED_FG + "elastic " + Config.ANSI_RESET +" import...");
        done();
    }
}
