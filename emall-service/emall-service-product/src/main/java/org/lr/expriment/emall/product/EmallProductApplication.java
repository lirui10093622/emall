package org.lr.expriment.emall.product;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Li Rui
 * @date 2025-09-03
 */
@EnableDubbo
@SpringBootApplication
public class EmallProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmallProductApplication.class, args);
    }
}