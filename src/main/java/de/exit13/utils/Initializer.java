package de.exit13.utils;

import de.exit13.Config;

import static de.exit13.utils.enums.Utils.done;

/**
 * Created by elshotodore on 12.03.15.
 */
public class Initializer {

    public boolean bootstrap(boolean doImport) throws Exception {
        boolean success = true;
        System.out.println("Checking " + Config.ANSI_RED_FG + "mySql" + Config.ANSI_RESET);
        success = checkMYSQLConfig();
        done();

        System.out.println("Checking  " + Config.ANSI_RED_FG + "ElasticSearch" + Config.ANSI_RESET);
        success = checkESConfig();
        done();


        if(doImport == true) {
            ImportUtils iu = new ImportUtils();
            iu.mySqlImport();
            iu.elasticImport();
        }
        return success;
    }

    private boolean checkMYSQLConfig() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            System.out.print(".");
            Thread.sleep(50);
        }
        System.out.println("");        return true;
    }


    private boolean checkESConfig() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            System.out.print(".");
            Thread.sleep(50);
        }
        System.out.println("");
        return true;
    }
}
