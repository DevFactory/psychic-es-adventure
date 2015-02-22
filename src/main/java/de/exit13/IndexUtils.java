package de.exit13;

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
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.indices.IndexAlreadyExistsException;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
/**
 * Created by frank.vogel on 17.02.2015.
 */
public class IndexUtils {

    private Client client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(Config.SERVER_ADDRESS, Config.SERVER_PORT));
    private Utils utils = new Utils();
    public void doIndexing(String filterExpression, String indexName, String indexType) throws IOException {

        indexName = (null != indexName | !indexName.equals(""))? indexName : Config.INDEX_NAME;
        indexType = (null != indexType | !indexType.equals(""))? indexType : Config.INDEX_TYPE;
        filterExpression = (null != filterExpression | !filterExpression.equals(""))? filterExpression : Config.FILTER_EXPRESSION;

        ArrayList<File> fileList = utils.readFilesFromDirectory(Config.DATA_DIR, filterExpression);

        for(File file : fileList) {
            processTxtFile(file, Config.MAX_LINES_TO_PROCESS, indexName, indexType);
        }
    }

    public void createNewIndex(String indexName){
        indexName = (indexName != null | !indexName.equals("")) ? indexName : Config.INDEX_NAME;
        try {
            CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
            // mapping
            XContentBuilder mapping =
                    XContentFactory.jsonBuilder()
                            .startObject()
                            .startObject("record")
                            .startObject("properties")
                            .startObject("station_id")
                            .field("type", "integer")
                            .endObject()
                            .startObject("date")
                            .field("type", "date")
                            .field("format", "yyyy-MM-dd")
                            .endObject()
                            .startObject("qualitaets_niveau")
                            .field("type", "integer")
                            .endObject()
                            .startObject("lufttemperatur")
                            .field("type", "float")
                            .endObject()
                            .startObject("dampfdruck")
                            .field("type", "float")
                            .endObject()
                            .startObject("bedeckungsgrad")
                            .field("type", "float")
                            .endObject()
                            .startObject("luftdruck_stationshoehe")
                            .field("type", "float")
                            .endObject()
                            .startObject("rel_feuchte")
                            .field("type", "float")
                            .endObject()
                            .startObject("windgeschwindigkeit")
                            .field("type", "float")
                            .endObject()
                            .startObject("lufttemperatur_maximum")
                            .field("type", "float")
                            .endObject()
                            .startObject("lufttemperatur_minimum")
                            .field("type", "float")
                            .endObject()
                            .startObject("lufttemperatur_am_erdb_minimum")
                            .field("type", "float")
                            .endObject()
                            .startObject("windspitze_maximum")
                            .field("type", "float")
                            .endObject()
                            .startObject("niederschlagshoehe")
                            .field("type", "float")
                            .endObject()
                            .startObject("niederschlagshoehe_ind")
                            .field("type", "float")
                            .endObject()
                            .startObject("sonnenscheindauer")
                            .field("type", "float")
                            .endObject()
                            .endObject()
                            .endObject()
                            .endObject();
            createIndexRequestBuilder.addMapping("record", mapping);

            CreateIndexResponse response = createIndexRequestBuilder.execute().actionGet();
        }
        catch (IndexAlreadyExistsException iaee) {
            System.out.println(iaee.getDetailedMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
    private void processTxtFile(File file, int limit, String indexName, String indexType) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(file));
        BulkProcessor bp = createBulkprocessor(client);
        String line;
        int i = 0;
        while ((line = br.readLine()) != null) {
            String json = "{}";
            if(i > 1) {
                try {
                    json = utils.convertLineToJson(line).toJSONString();
                    bp.add(createIndexRequest(json, indexName, indexType));
                    if(i % 5000 == 0) {
                        System.out.print("processed " + i + " lines...\r");
                    }
                    //System.out.println(i);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
                //System.out.println(json);
                if (limit > 0) {
                    if (i == limit) {
                        break;
                    }
                }
            }
            i++;
        }
        br.close();
        bp.flush();
        if(bp != null ) bp.close();
    }

    private void processGzipFile(File gzipFile, int maxLines, String indexName, String indexType) throws IOException {
        long fileProcessStartTime = System.nanoTime();
        FileInputStream fileInputStream = null;
        GZIPInputStream gzipInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        BulkProcessor bulkProcessor = null;
        Utils utils = new Utils();
        try {
            fileInputStream = new FileInputStream(gzipFile);
            gzipInputStream = new GZIPInputStream(fileInputStream);
            inputStreamReader = new InputStreamReader(gzipInputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            bulkProcessor = createBulkprocessor(client);
            String jsonLine = null;
            int i = 1;
            System.out.println("File: " + Config.ANSI_RED + gzipFile.getName() + Config.ANSI_RESET);
            while ((line = bufferedReader.readLine()) != null) {
                jsonLine = utils.convertLineToJson(line.toString()).toJSONString();
                bulkProcessor.add(createIndexRequest(jsonLine, indexName, indexType));
                if(i % 50000 == 0) {
                    System.out.print("processed " + Config.ANSI_RED +  i + Config.ANSI_RESET + " lines...\r");
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
        System.out.println("Time: " + Config.ANSI_RED  + fileProcessDuration + " ms." + Config.ANSI_RESET);
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
    public void closeClient() {
        client.close();
    }

}
