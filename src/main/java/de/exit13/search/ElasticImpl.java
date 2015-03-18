package de.exit13.search;

import de.exit13.utils.configuration.Config;
import de.exit13.utils.configuration.ElasticConfig;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.indices.IndexAlreadyExistsException;

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
    public void readMapping() {

    }

    @Override
    public void createIndex(String name, XContentBuilder mapping) {
        try {
            ElasticConfig elasticConfig = new ElasticConfig();
            Client client = elasticConfig.getClient();
            CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(name);
            // mapping

            createIndexRequestBuilder.addMapping("r", mapping);

            CreateIndexResponse response = createIndexRequestBuilder.execute().actionGet();
            System.out.println("Index " + name + " created");
        }
        catch (IndexAlreadyExistsException iaee) {
            System.out.println(iaee.getDetailedMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteIndex(String name) {
        try {
            Client client = new ElasticConfig().getClient();
            DeleteIndexResponse delete = client.admin().indices()
                    .delete(new DeleteIndexRequest(name)).actionGet();
            if (!delete.isAcknowledged()) {
                System.out.println("Index " + name + " wasn't deleted");
            } else {
                System.out.println("Index " + name + " deleted");
            }
            client.close();
        } catch (Exception e) {
            System.out.println("Index "  + name +  " doesn't exists");
        }
    }

    @Override
    public void openIndex(String name) {

    }

    @Override
    public void closeIndex(String name) {

    }
}
