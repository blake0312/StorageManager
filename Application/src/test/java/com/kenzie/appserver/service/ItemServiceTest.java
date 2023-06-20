package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.StorageRepository;
import com.kenzie.appserver.repositories.model.ItemRecord;
import com.kenzie.appserver.service.model.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemServiceTest {
    private StorageRepository storageRepository;
    private StorageService storageService;

    @BeforeEach
    void setup() {
        storageRepository = mock(StorageRepository.class);
        storageService = new StorageService(storageRepository);
    }
    /** ------------------------------------------------------------------------
     *  exampleService.findById
     *  ------------------------------------------------------------------------ **/

    @Test
    void findById() {
        // GIVEN
        String id = randomUUID().toString();

        ItemRecord record = new ItemRecord();
        record.setId(id);
        record.setName("concertname");

        // WHEN
        when(storageRepository.findById(id)).thenReturn(Optional.of(record));
        Item item = storageService.findById(id);

        // THEN
        Assertions.assertNotNull(item, "The object is returned");
        Assertions.assertEquals(record.getId(), item.getId(), "The id matches");
        Assertions.assertEquals(record.getName(), item.getName(), "The name matches");
    }

    @Test
    void findByConcertId_invalid() {
        // GIVEN
        String id = randomUUID().toString();

        when(storageRepository.findById(id)).thenReturn(Optional.empty());

        // WHEN
        Item item = storageService.findById(id);

        // THEN
        Assertions.assertNull(item, "The example is null when not found");
    }

}
