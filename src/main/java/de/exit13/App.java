package de.exit13;

/**
 * Created by frank.vogel on 16.02.2015.
 */
public class App {

    public static void main(String args[]) throws Exception {
        Utils utils = new Utils();
        long totalTimer = System.nanoTime();

        IndexUtils iu = new IndexUtils();

        // delete index if exists
        iu.deleteExistingIndex(Config.INDEX_NAME);
        iu.createNewIndex(Config.INDEX_NAME);
//System.exit(13);

        /*

        for(File file : fileList) {
            utils.processFile(file, Config.MAX_LINES_TO_PROCESS, Config.INDEX_NAME, Config.INDEX_TYPE);
        }
        */




        iu.doIndexing(Config.FILTER_EXPRESSION, Config.INDEX_NAME, Config.INDEX_TYPE);

        Thread.sleep(3000L);
        iu.closeClient();


    // querying
/*
*/


        utils.timer(totalTimer);
        System.out.println(Config.ANSI_PURPLE + "Elvis has left the building..." + Config.ANSI_RESET);
    }
}
