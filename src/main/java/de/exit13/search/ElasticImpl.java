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
                                .field("enabled", "false")
                            .endObject()
                            .startObject("_source")
                                .field("enabled", "false")
                            .endObject()
                            .startObject("properties")
                                .startObject("date")
                                    .field("type", "integer")
                                    .field("index", "not_analyzed")
                                    .field("store", false)
                                    .field("doc_values", true)
                                .endObject()
                            .endObject()
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
