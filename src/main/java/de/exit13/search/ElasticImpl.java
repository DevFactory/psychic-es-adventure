package de.exit13.search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

/**
 * Created by elshotodore on 12.03.15.
 */
public class ElasticImpl implements SearchIntf{

    @Override
    public SearchResponse query() {
        return null;
    }

    @Override
    public void createMapping() {
        XContentBuilder mapping =
                null;
        try {
            mapping = XContentFactory.jsonBuilder()
                    .startObject()
                        .startObject("r")
                            .startObject("_all")
                                .field("enabled", "true")
                            .endObject()
                            .startObject("_source")
                                .field("enabled", "true")
                            .endObject()
                            .startObject("properties");
                // for loop for all the fields
                            mapping.startObject("date")
                                    .field("type", "integer")
                                    .field("index", "not_analyzed")
                                    .field("store", false)
                                    .field("doc_values", true)
                                .endObject();
                // end of loop
                           mapping.endObject()
                        .endObject()
                    .endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readMapping() {

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
