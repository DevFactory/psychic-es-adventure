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
import org.json.simple.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
/**
 * Created by frank.vogel on 17.02.2015.
 */
public class IndexUtils {

    private Client client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(Config.SERVER_ADDRESS, Config.SERVER_PORT));

    public void doIndexing(String filterExpression, String indexName, String indexType) throws IOException {

        indexName = (null != indexName | !indexName.equals(""))? indexName : Config.INDEX_NAME;
        indexType = (null != indexType | !indexType.equals(""))? indexType : Config.INDEX_TYPE;
        filterExpression = (null != filterExpression | !filterExpression.equals(""))? filterExpression : Config.FILTER_EXPRESSION;

        ArrayList<File> fileList = getFilesFromDirectory(Config.SRC_DIRECTORY, filterExpression);

        for(File file : fileList) {
            processFile(file, Config.MAX_LINES_TO_PROCESS, indexName, indexType);
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
                            .startObject("impression")
                            .startObject("properties")
                            .startObject("date")
                            .field("type", "date")
                            .field("format", "yyyy-MM-dd HH:mm:ss")
                            .endObject()
                            .startObject("affiliate_id")
                            .field("type", "long")
                            .endObject()
                            .startObject("banner_id")
                            .field("type", "long")
                            .endObject()
                            .startObject("group_id")
                            .field("type", "long")
                            .endObject()
                            .startObject("merchant_id")
                            .field("type", "long")
                            .endObject()
                            .startObject("is_membership_soft")
                            .field("type", "integer")
                            .endObject()
                            .startObject("platform")
                            .field("type", "string")
                            .endObject()
                            .endObject()
                            .endObject()
                            .endObject();
            createIndexRequestBuilder.addMapping("impression", mapping);

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

    private static ArrayList<File> getFilesFromDirectory(String directory, String filterExpression) {
        Boolean filtered = true;
        File file = new File(directory);
        ArrayList<File> fileList = new ArrayList<File>();
        // Reading directory contents
        System.out.println("Reading files from directory: " + Config.ANSI_RED + Config.SRC_DIRECTORY + Config.ANSI_RESET);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            // filter the file names and then add
            if (null != filterExpression) {
                if(files[i].getName().matches(filterExpression)){
                    fileList.add(files[i]);
                }
            }
        }
        System.out.println(Config.ANSI_RED + fileList.size() + Config.ANSI_RESET + " files to process.");
        return fileList;
    }

    private void processFile(File gzipFile, int maxLines, String indexName, String indexType) throws IOException {
        long fileProcessStartTime = System.nanoTime();
        FileInputStream fileInputStream = null;
        GZIPInputStream gzipInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        BulkProcessor bulkProcessor = null;
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
                jsonLine = buildJson(line.toString()).toJSONString();
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

    private JSONObject buildJson(String line) {
        String jsonString = "";
        String[] pieces = line.replace("\"", "").split(",");
        JSONObject json = new JSONObject();
        // "date","merchant_id","affiliate_id","banner_id","group_id","is_membership_soft","platform"
        json.put("date", pieces[0]);
        long merchantId = parseLong(pieces[1]);
        json.put("merchant_id", merchantId);
        long affiliateId = parseLong(pieces[2]);
        json.put("affiliate_id", affiliateId);
        long bannerId = parseLong(pieces[3]);
        json.put("banner_id", bannerId);
        long groupId = parseLong(pieces[4]);
        json.put("group_id", groupId);
        int isMembershipSoft = parseInt(pieces[5]);
        json.put("is_membership_soft", isMembershipSoft);
        String platform = pieces[6];
        json.put("platform", platform);

        return json;
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
