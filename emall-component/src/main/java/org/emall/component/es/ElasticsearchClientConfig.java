package org.emall.component.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Li Rui
 * @date 2025-10-10
 */
@Slf4j
@ConditionalOnClass(value = {RestClient.class, ElasticsearchClient.class, RestHighLevelClient.class})
@Configuration
public class ElasticsearchClientConfig {
    @Value("${spring.elasticsearch.uris}")
    private String url;
    @Value("${spring.elasticsearch.username}")
    private String username;
    @Value("${spring.elasticsearch.password}")
    private String password;

    @Bean
    @ConditionalOnClass(value = {RestClient.class})
    public RestClient restClient() {
        return restClientBuilder().build();
    }

    @Bean
    @ConditionalOnBean(value = {RestClient.class})
    public ElasticsearchClient elasticSearchClient(RestClient restClient) {
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper()
        );
        return new ElasticsearchClient(transport);
    }

    @Deprecated
    @Bean
    @ConditionalOnClass(value = {RestHighLevelClient.class})
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(restClientBuilder());
    }

    private RestClientBuilder restClientBuilder() {
        HttpHost host = HttpHost.create(url);
        RestClientBuilder restClientBuilder = RestClient.builder(host);
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
            restClientBuilder.setHttpClientConfigCallback(httpAsyncClientBuilder ->
                    httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
            );
        }
        return restClientBuilder;
    }
}