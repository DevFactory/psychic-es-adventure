package de.exit13;

/**
 * Created by frank.vogel on 16.02.2015.
 */
public class App {

    public static void main(String args[]) {
        Utils utils = new Utils();
        IndexUtils iu = new IndexUtils();

        // delete index if exists
        iu.deleteExistingIndex(Config.INDEX_NAME);
        iu.createNewIndex(Config.INDEX_NAME);





        System.out.println(Config.ANSI_PURPLE + "Elvis has left the building..." + Config.ANSI_RESET);
    }
}
