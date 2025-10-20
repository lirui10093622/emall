package org.emall.order;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Li Rui
 * @date 2025-09-03
 */
@EnableDubbo
@SpringBootApplication
public class EmallOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmallOrderApplication.class, args);
    }
}
