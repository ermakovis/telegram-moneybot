package ru.ermakovis.moneybot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class MoneybotApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneybotApplication.class, args);
    }
}
