package services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.google.inject.Inject;
//import org.apache.http.HttpHost;
//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.http.client.CredentialsProvider;
//import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestClientBuilder;
import requests.BookRequest;

import java.awt.print.Book;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ElasticService {

    private final ElasticsearchClient client;

    @Inject
    public ElasticService() {
        client = this.getClient();
    }

    private ElasticsearchClient getClient() {
        String serverUrl = "https://" + System.getenv("ELASTIC_IP_HTTP") + ":" + System.getenv("ELASTIC_PORT_HTTP");
        String apiKey = System.getenv("ELASTIC_API_KEY");

        return ElasticsearchClient.of(b -> b
                .host(serverUrl)
                .apiKey(apiKey)
        );
    }

    public boolean index(String id, Object document, String index) throws IOException {
        IndexResponse response = client.index(i -> i.index(index).id(id).document(document));
        return response.result().equals(Result.Created);
    }

    public <T> List<T> search(SearchRequest request, Class<T> documentClass) {
        List<T> returnValue = new ArrayList<>();

        try {
            SearchResponse<T> searchResponse = client.search(request, documentClass);
            returnValue = searchResponse.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
            String sh = "sh";
        } catch (Exception ex) {
            String sh = "sh";
            ex.printStackTrace();
        }

        return returnValue;
    }

    public <T> T get(String index, String id, Class<T> documentClass) {
        GetRequest getRequest = new GetRequest
                .Builder()
                .index(index)
                .id(id)
                .build();

        try {
            GetResponse<T> response = client.get(getRequest, documentClass);
            return response.source();
        } catch (IOException ex) {
            return null;
        }
    }

    public boolean delete(String index, String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest.Builder().index(index).id(id).build();

        DeleteResponse response = client.delete(deleteRequest);
        return response.result().equals(Result.Deleted);
    }
}
