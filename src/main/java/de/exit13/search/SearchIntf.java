package de.exit13.search;

import org.elasticsearch.action.search.SearchResponse;

/**
 * Created by elshotodore on 12.03.15.
 */
public interface SearchIntf {

    SearchResponse query();

    // dealing with indices
    void createIndex(String name, String type);
    void deleteIndex(String name);
    void openIndex(String name);
    void closeIndex(String name);


}
