package de.exit13;

import de.exit13.db.DatabaseIntf;
import de.exit13.db.MySqlImpl;

import java.sql.SQLException;

/**
 * Created by el shotodore on 16.02.2015.
 */
public class App {

    public static void main(String args[]) {
        //Utils utils = new Utils();
        //IndexUtils iu = new IndexUtils();

        // delete index if exists
        //iu.deleteExistingIndex(Config.INDEX_NAME);
        //iu.createNewIndex(Config.INDEX_NAME);



        DatabaseIntf mysql = new MySqlImpl();
        try {
            mysql.initialImport();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        System.out.println(Config.ANSI_PURPLE + "Elvis has left the building..." + Config.ANSI_RESET);
    }
}
