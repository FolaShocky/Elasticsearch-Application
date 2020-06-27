package Utility;

import java.io.*;
import java.nio.file.*;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ApplicationLogic {

    /*
    * curl -X GET "localhost:9200/articles/_search?pretty"
    * -H "Content-Type:application/json" -d "{\"query\" :{\"match\":{\"content\":{\"query\":\"New\",\"operator\":\"or\"} }}}"
    *
    * */

    //org.elasticsearch.client
    private static ApplicationLogic applicationLogicInstance;
    private static final String HOST = "localhost";
    private static final int PORT_ONE = 9200;
    private static final int PORT_TWO = 9201;
    private static final int PORT_THREE = 9300;

    private static final String TARGET_INDEX = "article";
    private static final String TYPE = "_doc";
    private static final String SCHEME = "http";


    /*Used to make compliance with singleton design pattern possible.
     * Because the this method provides the sole entry point for accessing the contents of this class
     * from a different file without a NullPointerException being thrown
     * */
    public static ApplicationLogic getInstance() {
        if(applicationLogicInstance == null){
            applicationLogicInstance = new ApplicationLogic();
        }
        return applicationLogicInstance;
    }

    /**
    * The arrays passed into this method should have a correlation.
    * For example, fieldArr[0] and valueArr[0] should be related.
    * In the context of my application, this means the field "id" (fieldArr[0])
    * has an implicit mapping to the JTextField value entered in "SearchOptionsDialog" for the Id
    * This is also the same with fieldArr[1] and valueArr[1], for example
    * @param fieldArr An array representing the various fields that compose an ArticleDocument
     * @param valueArr An array representing the values to filter the search by*/
    public static String retrieveDocumentsByField(String[] fieldArr,String[] valueArr){
        try(RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(HOST,PORT_ONE,SCHEME)))){
            String id = valueArr[0].trim();
            String content = valueArr[1].trim();
            String media_type = valueArr[2].trim();
            String source = valueArr[3].trim();
            BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery()
            .should(QueryBuilders.matchPhraseQuery(fieldArr[0],id))
            .should(QueryBuilders.matchPhraseQuery(fieldArr[1],content))
            .should(QueryBuilders.matchPhraseQuery(fieldArr[2],media_type ))
            .should(QueryBuilders.matchPhraseQuery(fieldArr[3],source ));
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            SearchRequest searchRequest = new SearchRequest().source(searchSourceBuilder.query(booleanQueryBuilder));
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
            StringBuffer stringBuffer = new StringBuffer();
            System.out.println("Response: " + searchResponse.toString());
            for(SearchHit searchHit : searchResponse.getHits()){
                if(searchHit != null){
                    stringBuffer.append("Id: " + searchHit.field(ArticleDocument.ID_FIELD).getValue() + "\n");
                    stringBuffer.append("Content: " + searchHit.field(ArticleDocument.CONTENT_FIELD).getValue() + "\n");
                    stringBuffer.append("Media Type: " + searchHit.field(ArticleDocument.MEDIA_TYPE_FIELD).getValue() + "\n");
                    stringBuffer.append("Published: " + searchHit.field(ArticleDocument.PUBLISHED_FIELD).getValue() + "\n");
                    stringBuffer.append("Source: " + searchHit.field(ArticleDocument.SOURCE_FIELD).getValue() + "\n");
                }
            }
            System.out.println("StringBuffer: " + stringBuffer.toString());
            return stringBuffer.toString();
        }
        catch (Exception ex){
            System.out.println("An error or exception occurred: " + ex.getMessage());
            ex.printStackTrace();
            return "EMPTY";
        }
    }

    /**A method used for inserting data into ElasticSearch
    * @param chosenPath The path of the file whose contents are to be stored in ElasticSearch */
    public static boolean insertArticleDocument(String chosenPath){
        JSONParser jsonParser = new JSONParser();
        Path jsonFilePath = Paths.get(chosenPath.trim());
        List<ArticleDocument> articleDocumentList = new ArrayList<>();
        try(BufferedReader bufferedReader = Files.newBufferedReader(jsonFilePath);
            RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                new HttpHost(HOST,PORT_ONE,SCHEME)))){
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine())!=null) {
                stringBuilder.append(line);
                JSONObject jsonObject = (JSONObject) jsonParser.parse(line);
                articleDocumentList.add(
                    new ArticleDocument(
                    jsonObject.get(ArticleDocument.ID_FIELD).toString(),
                    jsonObject.get(ArticleDocument.CONTENT_FIELD).toString(),
                    jsonObject.get(ArticleDocument.MEDIA_TYPE_FIELD).toString(),
                    jsonObject.get(ArticleDocument.SOURCE_FIELD).toString(),
                    LocalDateTime.parse(
                            jsonObject.get(ArticleDocument.PUBLISHED_FIELD).toString(),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))));
            }
            for(ArticleDocument articleDocument : articleDocumentList){
                Map<String,Object>dataMap = new HashMap<>();
                dataMap.put(ArticleDocument.ID_FIELD,articleDocument.getId());
                dataMap.put(ArticleDocument.CONTENT_FIELD,articleDocument.getContent());
                dataMap.put(ArticleDocument.SOURCE_FIELD,articleDocument.getSource());
                dataMap.put(ArticleDocument.MEDIA_TYPE_FIELD,articleDocument.getMediaType());
                dataMap.put(ArticleDocument.PUBLISHED_FIELD,articleDocument.getPublishedLocalDateTime());
                IndexRequest indexRequest = new IndexRequest(TARGET_INDEX,TYPE).source(dataMap);
                IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
                System.out.println(indexResponse.getResult().toString());
            }
            return true;
        }
        catch (Exception ex){
            System.out.println("An exception occurred: " + ex.getMessage());
            String reminder = "Also ensure the selected file is in the JSON format and has the following fields: \n"
            + ArticleDocument.ID_FIELD + "\n"
            + ArticleDocument.SOURCE_FIELD + "\n"
            + ArticleDocument.MEDIA_TYPE_FIELD + "\n"
            + ArticleDocument.PUBLISHED_FIELD + "\n"
            + ArticleDocument.CONTENT_FIELD;
            System.out.println(reminder);
            ex.printStackTrace();
            return false;
        }
    }
}
