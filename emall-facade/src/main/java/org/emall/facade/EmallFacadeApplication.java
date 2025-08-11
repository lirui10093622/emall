package org.emall.facade;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
public class EmallFacadeApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmallFacadeApplication.class, args);
    }
}