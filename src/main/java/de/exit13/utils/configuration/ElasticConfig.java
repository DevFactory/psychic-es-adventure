package de.exit13.utils.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

/**
 * Created by dude on 16.03.15.
 */
public class ElasticConfig {
    public static String ES_INDEX_NAME = "climat_monthly";
    public static String ES_INDEX_TYPE = "r";

    public static String ES_SERVER_NAME = "localhost";
    public static String ES_SERVER_PORT = "9300";
    public static String ES_CLUSTER_NAME = "elasticsearch";

    public static List<List<String>>  mappingFieldsDefinition = new ArrayList<List<String>>();

    public ElasticConfig() {
        new ElasticConfig(ES_INDEX_NAME, ES_INDEX_TYPE, ES_SERVER_NAME, ES_SERVER_PORT, ES_CLUSTER_NAME);
        // field name, type, index, store, doc_values
        List<String> settings = null;

        mappingFieldsDefinition.add(settings);
    }

    public ElasticConfig(String ES_INDEX_NAME, String ES_INDEX_TYPE, String ES_SERVER_NAME, String ES_SERVER_PORT, String ES_CLUSTER_NAME) {
        this.ES_INDEX_NAME = ES_INDEX_NAME;
        this.ES_INDEX_TYPE = ES_INDEX_TYPE;
        this.ES_SERVER_NAME = ES_SERVER_NAME;
        this.ES_SERVER_PORT = ES_SERVER_PORT;
        this.ES_CLUSTER_NAME = ES_CLUSTER_NAME;
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
