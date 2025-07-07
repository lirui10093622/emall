package org.emall.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("org.emall.user.mapper")
public class EmallUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(org.emall.user.EmallUserApplication.class, args);
    }
}