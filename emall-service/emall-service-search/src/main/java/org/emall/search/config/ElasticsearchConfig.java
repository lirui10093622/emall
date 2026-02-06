package org.emall.search.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author Li Rui
 * @date 2026-01-28
 */
@Slf4j
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "elastic-search")
public class ElasticsearchConfig {

    private String indexName;

    private Integer maxResultWindow = 10000;
}