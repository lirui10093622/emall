package org.emall.facade.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author Li Rui
 * @date 2025-09-02
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "sms")
public class SmsConfig {
    Map<String, Long> ttlMap;
}