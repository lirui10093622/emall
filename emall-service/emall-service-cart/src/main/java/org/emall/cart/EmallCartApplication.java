package org.emall.cart;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Li Rui
 * @date 2025-09-03
 */
@EnableDubbo
@SpringBootApplication
public class EmallCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmallCartApplication.class, args);
    }
}