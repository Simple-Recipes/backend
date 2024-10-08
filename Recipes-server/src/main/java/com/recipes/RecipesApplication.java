package com.recipes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
//@EnableJpaRepositories(basePackages = "com.recipes.repository")
@EntityScan(basePackages = "com.recipes.entity")
@EnableTransactionManagement // 开启注解方式的事务管理
public class RecipesApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecipesApplication.class, args);
        log.info("server started");
    }
}
