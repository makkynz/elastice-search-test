import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainCli {
    public static void main(String[] args) {

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));
        SearchSourceBuilder builder = new SearchSourceBuilder()
                .postFilter(QueryBuilders.queryStringQuery("elasticsearch"));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH);
        searchRequest.source(builder);
      //  SearchRequest searchRequest = new SearchRequest();
        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchHit[] searchHits = response.getHits().getHits();
        List<SearchHit> results = Arrays.stream(searchHits)
                //  .map(hit -> JSON.parseObject(hit.getSourceAsString(), Person.class))
                .collect(Collectors.toList());

        for (SearchHit result : results) {
            System.out.println(result.getSourceAsString());
            System.out.println(result.getScore());
        }

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Finished");
    }
}
