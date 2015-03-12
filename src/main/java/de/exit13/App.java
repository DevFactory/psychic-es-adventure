package de.exit13;

import de.exit13.db.DatabaseIntf;
import de.exit13.db.MySqlImpl;
import de.exit13.utils.Initializer;

import java.sql.SQLException;

/**
 * Created by elshotodore on 16.02.2015.
 */
public class App {

    public static void main(String args[]) {
        Initializer i = new Initializer();
        try {
            boolean doImport = true;
            if(!i.bootstrap(doImport)) {
                System.out.println("Sorry!");
            };
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
