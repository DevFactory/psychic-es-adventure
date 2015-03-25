package de.exit13.utils.configuration;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dude on 16.03.15.
 */
public class ElasticConfig {
    private Client client;
    public static String ES_INDEX_NAME = "climate";
    public static String ES_INDEX_TYPE = "r";

    public static String ES_SERVER_NAME = "localhost";
    public static String ES_SERVER_PORT = "9300";
    public static String ES_CLUSTER_NAME = "elasticsearch";

    public static Map<String, Map<String, String>> MAPPING_FIELDS_DEFINITION = new HashMap<>();

    public ElasticConfig() {
        new ElasticConfig(ES_INDEX_NAME, ES_INDEX_TYPE, ES_SERVER_NAME, ES_SERVER_PORT, ES_CLUSTER_NAME);
    }

    public ElasticConfig(String ES_INDEX_NAME, String ES_INDEX_TYPE, String ES_SERVER_NAME, String ES_SERVER_PORT, String ES_CLUSTER_NAME) {
        ElasticConfig.ES_INDEX_NAME = ES_INDEX_NAME;
        ElasticConfig.ES_INDEX_TYPE = ES_INDEX_TYPE;
        ElasticConfig.ES_SERVER_NAME = ES_SERVER_NAME;
        ElasticConfig.ES_SERVER_PORT = ES_SERVER_PORT;
        ElasticConfig.ES_CLUSTER_NAME = ES_CLUSTER_NAME;
        MAPPING_FIELDS_DEFINITION = getMappingFieldsDefinition();
    }

    public Client getClient() {
        Settings indexSettings = ImmutableSettings.settingsBuilder().put("cluster.name", Config.CLUSTER_NAME).put("client.transport.sniff", true).build();
        client = new TransportClient(indexSettings).addTransportAddress(new InetSocketTransportAddress(Config.SERVER_ADDRESS, Config.SERVER_PORT));
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public static XContentBuilder createMapping(Map<String, Map<String, String>> mappingFieldsDefinition) {
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
                    .field("enabled", "true")
                    .endObject()
                    .startObject("properties");
            // for loop for all the fields
            int i = 0;
            for ( String key : mappingFieldsDefinition.keySet() ) {
                  //  System.out.println( key + mappingFieldsDefinition.get(key) );
                    String type = mappingFieldsDefinition.get(key).get("type");
                    String index = mappingFieldsDefinition.get(key).get("index");
                    Boolean store = Boolean.parseBoolean(mappingFieldsDefinition.get(key).get("store"));
                    Boolean docValues = Boolean.parseBoolean(mappingFieldsDefinition.get(key).get("doc_values"));
                    mapping.startObject(key)
                        .field("type", type)
                        .field("index", index)
                        .field("store", true)
                        .field("doc_values", docValues)
                    .endObject();
                //System.out.println(i++);
            }
            // end of loop
            mapping.endObject()
                    .endObject()
                    .endObject();
            //mapping.humanReadable(true);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return mapping;
    }

    public static Map<String, Map<String, String>> getMappingFieldsDefinition() {
        // field name, type, index, store, doc_values
        String key;
        Map<String, String> settings = new HashMap<>();
        settings.put("type", "integer");
        settings.put("index", "not_analyzed");
        settings.put("store", "true");
        settings.put("doc_values", "false");

        // 0
        key = "year";
        MAPPING_FIELDS_DEFINITION.put(key, settings);
        // 1
        key = "month";
        MAPPING_FIELDS_DEFINITION.put(key, settings);
        // 2
        key = "station_id";
        MAPPING_FIELDS_DEFINITION.put(key, settings);
        // 4
        key = "mean_monthly_station_level_pressure";
        MAPPING_FIELDS_DEFINITION.put(key, settings);
        // 8 + 9
        key = "mean_monthly_air_temp";
        MAPPING_FIELDS_DEFINITION.put(key, settings);
        // 19
        key = "total_monthly_precipitation";
        MAPPING_FIELDS_DEFINITION.put(key, settings);
        // 21
        key = "number_of_days_with_precipitation";
        MAPPING_FIELDS_DEFINITION.put(key, settings);
        // 23
        key = "total_monthly_sunshine";
        MAPPING_FIELDS_DEFINITION.put(key, settings);
        // 66
        key = "days_with_max_temp_gt_25";
        MAPPING_FIELDS_DEFINITION.put(key, settings);
        // 67
        key = "days_with_max_temp_gt_30";
        MAPPING_FIELDS_DEFINITION.put(key, settings);
        // 69
        key = "days_with_max_temp_gt_35";
        MAPPING_FIELDS_DEFINITION.put(key, settings);
        // 70
        key = "days_with_max_temp_gt_40";
        MAPPING_FIELDS_DEFINITION.put(key, settings);
        // 72
        key = "days_with_min_temp_lt_0";
        MAPPING_FIELDS_DEFINITION.put(key, settings);
        // 73
        key = "days_with_max_temp_lt_0";
        MAPPING_FIELDS_DEFINITION.put(key, settings);

        // 106 + 107
        key = "max_temp_per_month";
        MAPPING_FIELDS_DEFINITION.put(key, settings);
        // 111 + 112
        key = "min_temp_per_month";
        MAPPING_FIELDS_DEFINITION.put(key, settings);
        // 118
        key = "max_gust_wind_speed_per_month";
        MAPPING_FIELDS_DEFINITION.put(key, settings);
        // 121
        key = "number_of_days_with_thunderstorms";
        MAPPING_FIELDS_DEFINITION.put(key, settings);
        // 122
        key = "number_of_days_with_hail";
        MAPPING_FIELDS_DEFINITION.put(key, settings);


        return MAPPING_FIELDS_DEFINITION;
    }

    public static String getEsIndexName() {
        return ES_INDEX_NAME;
    }

    public static void setEsIndexName(String esIndexName) {
        ES_INDEX_NAME = esIndexName;
    }

    public static String getEsIndexType() {
        return ES_INDEX_TYPE;
    }

    public static void setEsIndexType(String esIndexType) {
        ES_INDEX_TYPE = esIndexType;
    }

    public static String getEsServerName() {
        return ES_SERVER_NAME;
    }

    public static void setEsServerName(String esServerName) {
        ES_SERVER_NAME = esServerName;
    }

    public static String getEsServerPort() {
        return ES_SERVER_PORT;
    }

    public static void setEsServerPort(String esServerPort) {
        ES_SERVER_PORT = esServerPort;
    }

    public static String getEsClusterName() {
        return ES_CLUSTER_NAME;
    }

    public static void setEsClusterName(String esClusterName) {
        ES_CLUSTER_NAME = esClusterName;
    }
}
