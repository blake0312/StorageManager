package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.StorageRepository;
import com.kenzie.appserver.repositories.model.ItemRecord;
import com.kenzie.appserver.service.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;

public class StorageServiceTest {

    private StorageRepository storageRepository;
    private StorageService storageService;

    @BeforeEach
    public void setup() {
        storageRepository = Mockito.mock(StorageRepository.class);
        storageService = new StorageService(storageRepository);
    }

    @Test
    public void find_By_Id_Test() {
        // GIVEN
        String id = "testId";
        ItemRecord record = new ItemRecord();
        record.setId(id);
        Mockito.when(storageRepository.findById(id)).thenReturn(Optional.of(record));

        // WHEN
        Item item = storageService.findById(id);

        // THEN
        assertNotNull(item);
        assertEquals(id, item.getId());
        Mockito.verify(storageRepository, times(1)).findById(id);
    }

    @Test
    public void add_New_Item_Test() {
        // GIVEN
        String id = "testId";
        String name = "testName";
        double value = 1.0;
        String status = "testStatus";
        String description = "testDescription";
        int quantity = 1;
        boolean inStorage = true;
        String storageLocation = "testLocation";
        Item item = new Item(id, name, value, status, description, quantity, inStorage, storageLocation);
        Mockito.when(storageRepository.save(any(ItemRecord.class))).thenReturn(null);

        // WHEN
        Item addedItem = storageService.addNewItem(item);

        // THEN
        assertNotNull(addedItem);
        assertEquals(item.getId(), addedItem.getId());
        Mockito.verify(storageRepository, times(1)).save(any(ItemRecord.class));
    }

    @Test
    public void update_Item_Test() {
        // GIVEN
        String id = "testId";
        String name = "Updated Name";
        double value = 1.0;
        String status = "testStatus";
        String description = "testDescription";
        int quantity = 1;
        boolean inStorage = true;
        String storageLocation = "testLocation";
        Item item = new Item(id, name, value, status, description, quantity, inStorage, storageLocation);
        ItemRecord record = new ItemRecord();
        record.setId(id);
        Mockito.when(storageRepository.findById(id)).thenReturn(Optional.of(record));
        Mockito.when(storageRepository.save(any(ItemRecord.class))).thenReturn(record);

        // WHEN
        Item updatedItem = storageService.updateItem(item);

        // THEN
        assertNotNull(updatedItem);
        assertEquals(item.getId(), updatedItem.getId());
        assertEquals(item.getName(), updatedItem.getName());
        Mockito.verify(storageRepository, times(1)).save(any(ItemRecord.class));
    }

    @Test
    public void delete_Item_Test() {
        // GIVEN
        String id = "testId";

        // WHEN
        storageService.deleteItem(id);

        // THEN
        Mockito.verify(storageRepository, times(1)).deleteById(id);
    }
}