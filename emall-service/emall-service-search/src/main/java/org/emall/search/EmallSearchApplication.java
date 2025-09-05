package org.emall.search;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Li Rui
 * @date 2025-09-02
 */
@EnableDubbo
@SpringBootApplication
public class EmallSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmallSearchApplication.class, args);
    }
}