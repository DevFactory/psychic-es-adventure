package de.exit13;

import de.exit13.utils.FileUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
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
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.indices.IndexAlreadyExistsException;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
/**
 * Created by elshotodore on 17.02.2015.
 */
public class IndexUtils {
    private Client client;
    private FileUtils fileUtils;
    private final static Logger LOGGER = Logger.getLogger(IndexUtils.class.getName());;

    public IndexUtils() {
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", Config.CLUSTER_NAME).put("client.transport.sniff", true).build();
        client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(Config.SERVER_ADDRESS, Config.SERVER_PORT));
        fileUtils = new FileUtils();
    }


    public void doIndexing(String filterExpression, String indexName, String indexType) throws IOException {

        filterExpression = (null != filterExpression | !filterExpression.equals(""))? filterExpression : Config.FILTER_EXPRESSION;

        ArrayList<File> fileList = null;//utils.fileToList(Config.SRC_DATA_DIR, filterExpression);

        for(File file : fileList) {
            processGzipFile(file, Config.MAX_LINES_TO_PROCESS, indexName, indexType);
        }
/*
        try {
            Thread.sleep(666L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/
        closeClient();
    }

    public void createNewIndex(String indexName){
        indexName = (indexName != null | !indexName.equals("")) ? indexName : Config.INDEX_NAME;
        try {
            CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
            // mapping
            XContentBuilder mapping = createMapping();
            createIndexRequestBuilder.addMapping("r", mapping);

            CreateIndexResponse response = createIndexRequestBuilder.execute().actionGet();
        }
        catch (IndexAlreadyExistsException iaee) {
            System.out.println(iaee.getDetailedMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private XContentBuilder createMapping() {
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
                    .startObject("affiliate_id")
                    .field("type", "integer")
                    .field("index", "not_analyzed")
                    .field("store", false)
                    .field("doc_values", true)
                    .endObject()
                    .startObject("banner_id")
                    .field("type", "integer")
                    .field("index", "not_analyzed")
                    .field("store", false)
                    .field("doc_values", true)
                    .endObject()
                    .startObject("group_id")
                    .field("type", "integer")
                    .field("index", "not_analyzed")
                    .field("store", false)
                    .field("doc_values", true)
                    .endObject()
                    .startObject("merchant_id")
                    .field("type", "integer")
                    .field("index", "not_analyzed")
                    .field("store", false)
                    .field("doc_values", true)
                    .endObject()
                    .startObject("count")
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
        //mapping.humanReadable(true);
        return mapping;
    }

    public void deleteExistingIndex(String indexName) {
        try {
            DeleteIndexResponse delete = client.admin().indices()
                    .delete(new DeleteIndexRequest(indexName)).actionGet();
            if (!delete.isAcknowledged()) {
                System.out.println("deleteExistingIndex: Index " + indexName + " wasn't deleted");
            } else {
                System.out.println("deleteExistingIndex: Index " + indexName + " deleted");
            }
        } catch (Exception e) {
            System.out.println("deleteExistingIndex: Index "  + indexName +  " doesn't exists");
        }
    }

    private void processGzipFile(File gzipFile, int maxLines, String indexName, String indexType) throws IOException {
        long fileProcessStartTime = System.nanoTime();
        FileInputStream fileInputStream = null;
        GZIPInputStream gzipInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        BulkProcessor bulkProcessor = null;
        FileUtils fileUtils = new FileUtils();
        try {
            fileInputStream = new FileInputStream(gzipFile);
            gzipInputStream = new GZIPInputStream(fileInputStream);
            inputStreamReader = new InputStreamReader(gzipInputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            bulkProcessor = createBulkprocessor(client);
            String jsonLine = null;
            int i = 1;
            System.out.println("File: " + Config.ANSI_RED_FG + gzipFile.getName() + Config.ANSI_RESET);
            while ((line = bufferedReader.readLine()) != null) {
                jsonLine = fileUtils.convertLineToJson(line.toString()).toJSONString();
                bulkProcessor.add(createIndexRequest(jsonLine, indexName, indexType));
                if(i % 50000 == 0) {
                    System.out.print("processed " + Config.ANSI_RED_FG +  i + Config.ANSI_RESET + " lines...\r");
                }
                // stop if set to value XX > 0 and XX reached;
                if(maxLines > 0) {
                    if (i >= maxLines) break;
                }
                i++;
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            // clean up
            if(bufferedReader != null ) bufferedReader.close();
            if(inputStreamReader != null ) inputStreamReader.close();
            if(gzipInputStream != null ) gzipInputStream.close();
            if(fileInputStream != null ) fileInputStream.close();
            bulkProcessor.flush();
            if(bulkProcessor != null ) bulkProcessor.close();
        }
        long fileProcessEndTime = System.nanoTime();
        long fileProcessDuration = (fileProcessEndTime - fileProcessStartTime) / 1000000;
        System.out.println("Time: " + Config.ANSI_RED_FG + fileProcessDuration + " ms." + Config.ANSI_RESET);
        System.out.println("--------------------------------------------------------------------------------------------------------");
    }

    private IndexRequest createIndexRequest(String source, String indexName, String indexType) {
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.index(indexName);
        indexRequest.type(indexType);
        indexRequest.source(source);
        return indexRequest;
    }

    private BulkProcessor createBulkprocessor(Client client) {
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

    private void closeClient() {
        if(client != null) {
            client.close();
        }
    }
}
