package de.exit13;

/**
 * Created by frank.vogel on 16.02.2015.
 */
public class ElasticsearchEvaluation {

    public static void main(String args[]) throws Exception {
        long totalTimer = System.nanoTime();

        IndexUtils iu = new IndexUtils();
        String indexName;
        String indexType;
        String filter;
/*
        //iuX.deleteExistingIndex("awi_1d");
        //iuX.deleteExistingIndex("awi_7d");
        //iuX.deleteExistingIndex("awi_14d");
        //iuX.deleteExistingIndex("awi_1m");
        //iuX.deleteExistingIndex("awi_3m");
*/



//        System.exit(13);

/*
        indexName = "awi_1d";
        indexType = "impression";
        iu.createNewIndex(indexName);
        filter = "^impressionlog20140101.*";
        iu.doIndexing(filter, indexName, indexType);

        indexName = "awi_7d";
        indexType = "impression";
        iu.createNewIndex(indexName);
        filter = "^impressionlog2014010[1-7].*";
        iu.doIndexing(filter, indexName, indexType);

        indexName = "awi_14d";
        indexType = "impression";
        iu.createNewIndex(indexName);
        filter = "^impressionlog2014010[1-9].*";
        iu.doIndexing(filter, indexName, indexType);
        filter = "^impressionlog2014011[0-4].*";
        iu.doIndexing(filter, indexName, indexType);

        long oneMonth = System.nanoTime();
        indexName = "awi_1m";
        indexType = "impression";
        iu.createNewIndex(indexName);
        filter = "^impressionlog201401.*";
        //iu.doIndexing(filter, indexName, indexType);
        Utils.timer(oneMonth);

        long threeMonth = System.nanoTime();
        indexName = "awi_3m";
        indexType = "impression";
        iu.createNewIndex(indexName);
        filter = "^impressionlog20140[1-3].*";
        //iu.doIndexing(filter, indexName, indexType);
        Utils.timer(oneMonth);

        Thread.sleep(1000L);
        iu.closeClient();
*/


    // querying tasks
/*

        final String QUERY_INDEX = "awi";
        final String QUERY_TYPE = "impression";

        QueryUtils qu = new QueryUtils();
        long merchantId = 2891;


        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("Querying for merchant_id - " + merchantId + "...");
        qu.getDataByMerchantIdUnsorted(QUERY_INDEX, QUERY_TYPE, merchantId);

        // sorting gives exception
        merchantId = 1221;
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("Querying for merchant_id - " + merchantId);
        qu.getDataByMerchantIdSortedByDate(QUERY_INDEX, QUERY_TYPE, merchantId);


        merchantId = 2891;
        Date dateFrom = new GregorianCalendar(2014,0,1,0,0).getTime();
        Date dateTo = new GregorianCalendar(2014,0,3,0,0).getTime();
        String from = "2014-01-01 00:00:00";
        String to = "2014-01-03 00:00:00";
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("Querying by DateRange [ " + dateFrom + ", " + dateTo + " ] for merchantId - " + merchantId);
        qu.getDataByMerchantIdByDateRange(QUERY_INDEX, QUERY_TYPE, merchantId, dateFrom, dateTo);


        merchantId = -1;
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("Querying all the data...");
        qu.getAllDataUnsorted(QUERY_INDEX, QUERY_TYPE);


        // clean up
        qu.closeClient();
*/


        Utils.timer(totalTimer);
        System.out.println(Config.ANSI_PURPLE + "Elvis has left the building..." + Config.ANSI_RESET);
    }
}
