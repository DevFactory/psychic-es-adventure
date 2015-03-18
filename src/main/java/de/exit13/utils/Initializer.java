package de.exit13.utils;

import de.exit13.db.MySQLImpl;
import de.exit13.search.ElasticImpl;
import de.exit13.utils.configuration.Config;
import de.exit13.utils.configuration.ElasticConfig;
import de.exit13.utils.configuration.MySQLConfig;
import org.elasticsearch.common.xcontent.XContentBuilder;

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
            // DBImportUtils dbImportUtils = new DBImportUtils();
            // dbImportUtils.mysqlImport();
            ElasticImpl elastic = new ElasticImpl();

            XContentBuilder mapping = ElasticConfig.createMapping (ElasticConfig.getMappingFieldsDefinition());
            elastic.deleteIndex("testindex2");
            elastic.createIndex("testindex2", mapping);

            ElasticImportUtils elasticImportUtils = new ElasticImportUtils();
            //elasticImportUtils.elasticImport();


        }

        return status;
    }

    private String checkMYSQLConfig() {
        String status = null;
        MySQLConfig mysqlConfig = new MySQLConfig();
        MySQLImpl mysql = new MySQLImpl();
        //Connection connection = mysql.openConnection(mysqlConfig.getDB_USER(), mysqlConfig.getDB_PASSWORD(), mysqlConfig.getDB_SERVER(), mysqlConfig.getDB());
        return status;
    }


    private String checkESConfig() throws InterruptedException {
        String status = null;

        return status;
    }
}
