package de.exit13.utils;

import de.exit13.search.ElasticImpl;
import de.exit13.utils.configuration.Config;
import de.exit13.utils.configuration.ElasticConfig;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

/**
 * Created by dude on 13.03.15.
 */
public class ElasticImportUtils {
    ElasticConfig elasticConfig = new ElasticConfig();
    Client client;
    ElasticImpl elastic = new ElasticImpl();


    public ElasticImportUtils() {
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", Config.CLUSTER_NAME).put("client.transport.sniff", true).build();
        client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(Config.SERVER_ADDRESS, Config.SERVER_PORT));
    }

    public void elasticImport()  throws Exception{
        System.out.println(Config.ANSI_RED_FG + "elastic" + Config.ANSI_RESET +" import...");
        FileUtils fu = new FileUtils();

        String directory = "/data/rawdata/CLIMAT";
        String filter = "CLIMAT_RAW_2003.*.txt";
        ArrayList<String> fileList = fu.readDirectory(directory, filter);
        System.out.println("Read " + fileList.size() + " files from " + directory + ".");

        for(String file : fileList) {
            System.out.println("File: " + file);
            ArrayList<String> fileContent = fu.readFileContent(file);
            processFileContent(fileContent);

        }

        System.out.println(Config.ANSI_GREEN_FG + "Done!" + Config.ANSI_RESET);
    }

    private void processFileContent(ArrayList<String> fileContent) {
        int i = 0;
        BulkProcessor bulkProcessor = createBulkProcessor(client);

        for(String line : fileContent ){
            if(i > 0) { // start with line 1 ;)
                bulkProcessor.add(createIndexRequest(convertLineToJson(line).toJSONString(), Config.INDEX_NAME, Config.INDEX_TYPE));
            }
            if(i % 1 == 0) {
                System.out.print("processed " + Config.ANSI_RED_FG +  i + Config.ANSI_RESET + " lines...\r");
            }
            i++;
        }
        bulkProcessor.flush();
    }


    private JSONObject convertLineToJson(String line) {
        String[] pieces = line.split(";",-1); // split preserving empty fields
        Map<String, Object> mapping = new HashMap<String, Object>();
        for(int i = 0; i< pieces.length; i++) {
            // set all the zero value pieces to -9999
            if (null == pieces[i] || pieces[i].isEmpty()) {
                pieces[i] = "-9999";
            }
        }
        // for a reference where the values come from -> see /resources/datafile_format.txt
        mapping.put("year", parseInt(pieces[0]));
        mapping.put("month", parseInt(pieces[1]));
        mapping.put("station_id", parseInt(pieces[2]));
        mapping.put("mean_monthly_station_level_pressure", parseInt(pieces[4]));
        int sign = (parseInt(pieces[8]) > 0) ? -1: +1;
        mapping.put("mean_monthly_air_temp", sign * parseInt(pieces[9]));

        mapping.put("total_monthly_precipitation", parseInt(pieces[19]));
        //System.out.println(i + " --- " + parseInt(pieces[i]));
        JSONObject jsonObject = new JSONObject();
        return jsonObject;
    }

    private IndexRequest createIndexRequest(String source, String indexName, String indexType) {
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.index(indexName);
        indexRequest.type(indexType);
        indexRequest.source(source);
        return indexRequest;
    }

    private BulkProcessor createBulkProcessor(Client client) {
        BulkProcessor bulkProcessor = BulkProcessor.builder(
                client,
                new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId,
                                           BulkRequest request) {
                        //System.out.println("# of actions to index -> " + request.numberOfActions());
                    }
                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          BulkResponse response) {
                        //System.out.println("AFTER: hasFailures? -> " + response.hasFailures());
                    }
                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          Throwable failure) {
                        failure.printStackTrace();
                    }
                })
                .setBulkActions(Config.BULK_ACTION_COUNT)
                .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(1)
                .build();
        return bulkProcessor;
    }
}
