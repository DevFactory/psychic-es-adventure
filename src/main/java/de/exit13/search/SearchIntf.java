package de.exit13.search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;

/**
 * Created by elshotodore on 12.03.15.
 */
public interface SearchIntf {

    SearchResponse query();
    // dealing with mapping
    void readMapping();


    // dealing with indices
    void createIndex(String name, XContentBuilder mapping);
    void deleteIndex(String name);
    void openIndex(String name);
    void closeIndex(String name);


}
