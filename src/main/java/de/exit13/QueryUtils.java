package de.exit13;


import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.SortOrder;

import java.util.Date;

/**
 * Created by el shotodore on 18.02.2015.
 */
public class QueryUtils {
    private Client client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(Config.SERVER_ADDRESS, Config.SERVER_PORT));

    public void getDataByMerchantIdUnsorted(String indexName, String indexType, long merchantId) {
        long startTime =  System.nanoTime();
        QueryBuilder qb = QueryBuilders.matchQuery("merchant_id", merchantId);
        SearchResponse response = client.prepareSearch(indexName)
                .setTypes(indexType)
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(qb)
                .addField("merchant_id")
                .setSize(100).execute().actionGet();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        printResults("getDataByMerchantIdUnsorted", response);
        System.out.println("Query took " + duration + " ms.");
    }


    public void getDataByMerchantIdSortedByDate(String indexName, String indexType, long merchantId) {
        long startTime =  System.nanoTime();
        QueryBuilder qb = QueryBuilders.matchQuery("merchant_id", merchantId);
        SearchResponse response = client.prepareSearch(indexName)
                .setTypes(indexType)
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(qb)
                .addField("merchant_id")
                .addSort("date", SortOrder.ASC)
                .setSize(100).execute().actionGet();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        printResults("getDataByMerchantIdSortedByDate", response);
        System.out.println("Query took " + duration + " ms.");
    }

    public void getAllDataUnsorted(String indexName, String indexType) {
        long startTime =  System.nanoTime();
        QueryBuilder qb = QueryBuilders.matchAllQuery();
        SearchResponse response = client.prepareSearch(indexName)
                .setTypes(indexType)
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(qb)
                .addField("merchant_id")
                .setSize(100).execute().actionGet();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        printResults("getAllDataUnsorted", response);
        System.out.println("Query took " + duration + " ms.");
    }

    public void getDataByMerchantIdByDateRange(String indexName, String indexType, long merchantId, Date dateFrom, Date dateTo) {

        long startTime =  System.nanoTime();

        RangeQueryBuilder queryDateRange = QueryBuilders.rangeQuery("date").to(dateTo).from(dateFrom);
        FilterBuilder filterDateRange = FilterBuilders.queryFilter(queryDateRange);

        QueryBuilder qb = QueryBuilders.matchQuery("merchant_id", merchantId);
        SearchResponse response = client.prepareSearch(indexName)
                .setTypes(indexType)
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(qb)
                .addField("date")
                .setPostFilter(filterDateRange)
                .setSize(100)
                .execute()
                .actionGet();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        printResults("getDataByMerchantIdByDateRange", response);
        System.out.println("Query took " + duration + " ms.");
    }

    private static void printResults(String caller, SearchResponse response) {
        System.out.println("Results for " + caller);
        int i = 0;
        long responseCount = response.getHits().totalHits();
/*
        for (SearchHit hit : response.getHits()) {
            Map<String, SearchHitField> hitFields = hit.getFields();
            String merchantId=hitFields.get("merchant_id").getValue().toString();
        }
*/
        System.out.println("We got a total of " + responseCount + " hits.");
    }
    public void closeClient() {
        client.close();
    }
}
