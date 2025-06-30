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
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import requests.BookRequest;

import javax.net.ssl.SSLContext;
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
        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(System.getenv("ELASTIC_USERNAME"), System.getenv("ELASTIC_PASSWORD")));

        RestClientBuilder builder = RestClient.builder(
                new HttpHost(System.getenv("ELASTIC_IP_HTTP"), Integer.parseInt(System.getenv("ELASTIC_PORT_HTTP")), System.getenv("ELASTIC_SCHEME")));

        if(System.getenv("ELASTIC_USE_CREDENTIALS").equals("1"))
        {
            builder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                    .setDefaultCredentialsProvider(credentialsProvider));
        }


        // Create the low-level client
        RestClient restClient = builder.build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        return new ElasticsearchClient(transport);
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

    public <T> T get(String index, String id, Class<T> documentClass) throws IOException {
        GetRequest getRequest = new GetRequest
                .Builder()
                .index(index)
                .id(id)
                .build();

        GetResponse<T> response = client.get(getRequest, documentClass);
        return response.source();
    }

    public boolean delete(String index, String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest.Builder().index(index).id(id).build();

        DeleteResponse response = client.delete(deleteRequest);
        return response.result().equals(Result.Deleted);
    }
}
