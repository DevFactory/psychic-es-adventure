package de.exit13;

import de.exit13.utils.Configuration.Config;
import de.exit13.utils.Initializer;

/**
 * Created by elshotodore on 16.02.2015.
 */
public class App {

    public static void main(String args[]) {
        Initializer i = new Initializer();
        try {
            boolean initialSetup = true;
            String status = i.bootstrap(initialSetup);
            if(null != status) {
                System.out.println(Config.ANSI_RED_FG + "ERROR initializing the app!" + Config.ANSI_RESET);

                System.out.println("Status:" + status);
                System.exit(13);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        //Utils utils = new Utils();
        //IndexUtils iu = new IndexUtils();

        // delete index if exists
        //iu.deleteExistingIndex(Config.INDEX_NAME);
        //iu.createNewIndex(Config.INDEX_NAME);

/*

        DatabaseIntf mysql = new MySqlImpl();
        try {
            mysql.initialImport();
        } catch (SQLException e) {
            e.printStackTrace();
        }

*/
        System.out.println(Config.ANSI_PURPLE_FG + "Elvis has left the building..." + Config.ANSI_RESET);
    }
}
