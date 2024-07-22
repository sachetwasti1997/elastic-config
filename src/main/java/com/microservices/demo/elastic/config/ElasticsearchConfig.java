package com.microservices.demo.elastic.config;

import com.microservices.demo.config.ElasticConfigData;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.AbstractElasticsearchTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Configuration
public class ElasticsearchConfig {

    private final ElasticConfigData elasticConfigData;

    public ElasticsearchConfig(ElasticConfigData elasticConfigData) {
        this.elasticConfigData = elasticConfigData;
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(elasticConfigData.getConnectionUrl()).build();
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(
                                Objects.requireNonNull(uriComponents.getHost()),
                                uriComponents.getPort(),
                                uriComponents.getScheme()
                        )
                ).setRequestConfigCallback(
                        builder -> builder.
                                setConnectTimeout(elasticConfigData.getConnectionTimeoutMs()).
                                setSocketTimeout(elasticConfigData.getSocketTimeoutMs())
                )
        );
    }

}

