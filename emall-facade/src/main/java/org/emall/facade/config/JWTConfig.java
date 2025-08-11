package org.emall.facade.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Li Rui
 * @date 2025-08-07
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JWTConfig {
    private String authorizationType;
    private String secret;
    private int ttl;
}