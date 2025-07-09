package org.emall.user;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@EnableDubbo
@SpringBootApplication
@MapperScan("org.emall.user.mapper")
public class EmallUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(org.emall.user.EmallUserApplication.class, args);
    }
}