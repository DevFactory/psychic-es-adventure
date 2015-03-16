package de.exit13.search;

import org.elasticsearch.action.search.SearchResponse;

/**
 * Created by elshotodore on 12.03.15.
 */
public class ElasticImpl implements SearchIntf{

    @Override
    public SearchResponse query() {
        return null;
    }

    @Override
    public void createIndex(String name, String type, String mapping) {

    }

    @Override
    public void deleteIndex(String name) {

    }

    @Override
    public void openIndex(String name) {

    }

    @Override
    public void closeIndex(String name) {

    }
}
