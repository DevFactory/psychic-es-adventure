package de.exit13.utils;

import de.exit13.db.MySQLImpl;
import de.exit13.utils.Configuration.Config;
import de.exit13.utils.Configuration.MySQLConfig;

import java.sql.Connection;

/**
 * Created by elshotodore on 12.03.15.
 */
public class Initializer {

    public String bootstrap(boolean initialSetup) throws Exception {
        String status = null;
        System.out.println("Checking " + Config.ANSI_GREEN_FG + "mySql" + Config.ANSI_RESET + " status...");
        status = checkMYSQLConfig();
        if(status != null) { return status; }
        System.out.println(Config.ANSI_GREEN_FG + "OK!" + Config.ANSI_RESET);

        System.out.println("Checking  " + Config.ANSI_GREEN_FG + "elastic" + Config.ANSI_RESET + " status...");
        status = checkESConfig();
        if(status != null) { return status; }
        System.out.println(Config.ANSI_GREEN_FG + "OK!" + Config.ANSI_RESET);


        if(initialSetup == true) {
            //DBImportUtils dbImportUtils = new DBImportUtils();
            //dbImportUtils.mysqlImport();
            ESImportUtils esImportUtils = new ESImportUtils();
            esImportUtils.elasticImport();
        }

        return status;
    }

    private String checkMYSQLConfig() {
        String status = null;
        MySQLConfig mysqlConfig = new MySQLConfig();
        MySQLImpl mysql = new MySQLImpl();
        Connection connection = mysql.openConnection(mysqlConfig.getDB_USER(), mysqlConfig.getDB_PASSWORD(), mysqlConfig.getDB_SERVER(), mysqlConfig.getDB());
        return status;
    }


    private String checkESConfig() throws InterruptedException {
        String status = null;

        return status;
    }
}
