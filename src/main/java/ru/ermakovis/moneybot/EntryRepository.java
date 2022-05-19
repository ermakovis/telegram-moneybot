package ru.ermakovis.moneybot;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface EntryRepository extends MongoRepository<Entry, Long> {
    @Query(value = "{}", sort = "{ _id : -1 }")
    List<Entry> findLast(Pageable pageable);

    default List<Entry> findLastEntries(Integer n) {
        return this.findLast(PageRequest.of(0, n));
    }
}
