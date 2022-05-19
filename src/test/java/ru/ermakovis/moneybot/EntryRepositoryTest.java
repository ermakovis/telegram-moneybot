package ru.ermakovis.moneybot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class EntryRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private EntryRepository repository;

    @AfterEach
    void cleanUp() {
        this.repository.deleteAll();
    }

    @Test
    void shouldReturnLastValues() {
        //given
        var entries = LongStream.range(1, 100).asDoubleStream().boxed()
                .map(Entry::new)
                .collect(Collectors.toList());
        repository.saveAll(entries);

        //when
        var savedList = repository.findLastEntries(5);

        //then
        savedList.forEach(System.out::println);
        assertEquals(5, savedList.size());
    }
}