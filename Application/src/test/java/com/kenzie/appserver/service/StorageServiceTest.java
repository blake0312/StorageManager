package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.StorageRepository;
import com.kenzie.appserver.repositories.model.ItemRecord;
import com.kenzie.appserver.service.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.testcontainers.lifecycle.TestDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

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
        when(storageRepository.findById(id)).thenReturn(Optional.of(record));

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
        Integer usageCount = 0;
        Item item = new Item(id, name, value, status, description, quantity, inStorage, storageLocation, usageCount);
        when(storageRepository.save(any(ItemRecord.class))).thenReturn(null);

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
        Integer usageCount = 0;
        Item item = new Item(id, name, value, status, description, quantity, inStorage, storageLocation, usageCount);
        ItemRecord record = new ItemRecord();
        record.setId(id);
        when(storageRepository.findById(id)).thenReturn(Optional.of(record));
        when(storageRepository.save(any(ItemRecord.class))).thenReturn(record);

        // WHEN
        Item updatedItem = storageService.updateItem(item);

        // THEN
        assertNotNull(updatedItem);
        assertEquals(item.getId(), updatedItem.getId());
        assertEquals(item.getName(), updatedItem.getName());
        Mockito.verify(storageRepository, times(1)).save(any(ItemRecord.class));
    }

    @Test
    public void update_item_test_changes_count() {
        // GIVEN
        // the original item
        Item item = new Item("testId", "Updated Name", 1.0, "testStatus",
                "TestDescription", 1, true, "testLocation", 0);
        // the requested update
        Item updatedItem = new Item("testId", "Updated Name", 1.0, "testStatus",
                "TestDescription", 1, false, "testLocation", 0);
        // the final updated item
        Item updatedItemCount = new Item("testId", "Updated Name", 1.0, "testStatus",
                "TestDescription", 1, false, "testLocation", 1);
        ItemRecord record = convertToItemRecord(item);

        // This is the itemRecord that the repository saves after the count is changed
        ItemRecord updatedRecordCount = convertToItemRecord(updatedItemCount);

        when(storageRepository.findById(updatedItem.getId())).thenReturn(Optional.of(record));

        // WHEN
        // result will have updated usageCount
        Item result = storageService.updateItem(updatedItem);

        // THEN
        assertNotNull(result);
        assertEquals(updatedItemCount.getId(), result.getId());
        assertEquals(updatedItemCount.getName(), result.getName());
        assertEquals(updatedItemCount.getValue(), result.getValue());
        assertEquals(updatedItemCount.getStatus(), result.getStatus());
        assertEquals(updatedItemCount.getDescription(), result.getDescription());
        assertEquals(updatedItemCount.getQuantity(), result.getQuantity());
        assertEquals(updatedItemCount.getInStorage(), result.getInStorage());
        assertEquals(updatedItemCount.getStorageLocation(), result.getStorageLocation());
        assertEquals(updatedItemCount.getUsageCount(), result.getUsageCount());

        Mockito.verify(storageRepository, times(1)).save(updatedRecordCount);

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

    @Test
    public void find_All_Items_Test() {
        // GIVEN
        List<ItemRecord> itemRecords = new ArrayList<>();
        ItemRecord itemRecord1 = new ItemRecord();
        itemRecord1.setId("id1");
        itemRecord1.setName("name1");
        itemRecord1.setValue(1.0);
        itemRecord1.setStatus("status1");
        itemRecord1.setDescription("description1");
        itemRecord1.setQuantity(1);
        itemRecord1.setInStorage(true);
        itemRecord1.setStorageLocation("location1");
        itemRecord1.setUsageCount(0);
        itemRecords.add(itemRecord1);

        ItemRecord itemRecord2 = new ItemRecord();
        itemRecord2.setId("id2");
        itemRecord2.setName("name2");
        itemRecord2.setValue(2.0);
        itemRecord2.setStatus("status2");
        itemRecord2.setDescription("description2");
        itemRecord2.setQuantity(2);
        itemRecord2.setInStorage(false);
        itemRecord2.setStorageLocation("location2");
        itemRecord2.setUsageCount(0);
        itemRecords.add(itemRecord2);

        when(storageRepository.findAll()).thenReturn(itemRecords);

        // WHEN
        List<Item> items = storageService.findAllItems();

        // THEN
        assertNotNull(items);
        assertEquals(itemRecords.size(), items.size());
        for (int i = 0; i < itemRecords.size(); i++) {
            ItemRecord itemRecord = itemRecords.get(i);
            Item item = items.get(i);
            assertEquals(itemRecord.getId(), item.getId());
            assertEquals(itemRecord.getName(), item.getName());
            assertEquals(itemRecord.getValue(), item.getValue());
            assertEquals(itemRecord.getStatus(), item.getStatus());
            assertEquals(itemRecord.getDescription(), item.getDescription());
            assertEquals(itemRecord.getQuantity(), item.getQuantity());
            assertEquals(itemRecord.getInStorage(), item.getInStorage());
            assertEquals(itemRecord.getStorageLocation(), item.getStorageLocation());
            assertEquals(itemRecord.getUsageCount(), item.getUsageCount());
        }
        Mockito.verify(storageRepository, times(1)).findAll();
    }

    // helper method
    private ItemRecord convertToItemRecord(Item item) {
        ItemRecord itemRecord = new ItemRecord();
        itemRecord.setId(item.getId());
        itemRecord.setName(item.getName());
        itemRecord.setValue(item.getValue());
        itemRecord.setStatus(item.getStatus());
        itemRecord.setDescription(item.getDescription());
        itemRecord.setQuantity(item.getQuantity());
        itemRecord.setInStorage(item.getInStorage());
        itemRecord.setStorageLocation(item.getStorageLocation());
        itemRecord.setUsageCount(item.getUsageCount());
        return itemRecord;
    }

    @Test
    void updateItemTest_ItemExists_InStorageStatusUnchanged() {
        // Given
        Item item = new Item("1", "item1", 10.5, "active", "This is item1",
                5, true, "A1", 2);
        ItemRecord record = new ItemRecord();
        record.setId("1");
        when(storageRepository.findById("1")).thenReturn(Optional.of(record));
        when(storageRepository.save(any(ItemRecord.class))).thenReturn(record);

        // When
        Item updatedItem = storageService.updateItem(item);

        // Then
        assertNotNull(updatedItem);
        assertEquals(record.getId(), updatedItem.getId());
        assertEquals(record.getName(), updatedItem.getName());
        assertEquals(record.getValue(), updatedItem.getValue());
        assertEquals(record.getStatus(), updatedItem.getStatus());
        assertEquals(record.getDescription(), updatedItem.getDescription());
        assertEquals(record.getQuantity(), updatedItem.getQuantity());
        assertEquals(record.getInStorage(), updatedItem.getInStorage());
        assertEquals(record.getStorageLocation(), updatedItem.getStorageLocation());
        assertEquals(record.getUsageCount(), updatedItem.getUsageCount());
    }

    @Test
    void updateItemTest_ItemDoesNotExist() {
        // Given
        Item item = new Item("2", "item2", 20.5, "inactive", "This is item2",
                10, true, "A2", 2);
        when(storageRepository.findById("2")).thenReturn(Optional.empty());

        // When
        Item updatedItem = storageService.updateItem(item);

        // Then
        assertNull(updatedItem);
    }

    @Test
    public void testUpdateItem() {
        String itemId = "12345";
        ItemRecord itemRecord = new ItemRecord();
        itemRecord.setId(itemId);
        itemRecord.setUsageCount(3);
        itemRecord.setInStorage(true);

        Item item = new Item(itemId, "name", 123.0, "status", "description", 2, false, "location", 3);

        when(storageRepository.findById(itemId)).thenReturn(Optional.of(itemRecord));
        when(storageRepository.save(any(ItemRecord.class))).thenAnswer(i -> i.getArguments()[0]);

        Item updatedItem = storageService.updateItem(item);

        assertNotNull(updatedItem);
        assertEquals(4, updatedItem.getUsageCount());
        assertFalse(updatedItem.getInStorage());
    }

    @Test
    public void deleteItem_Nonexistent_Test() {
        // GIVEN
        String id = "nonexistentId";
        doThrow(EmptyResultDataAccessException.class).when(storageRepository).deleteById(id);

        // THEN
        assertThrows(EmptyResultDataAccessException.class, () -> {
            // WHEN
            storageService.deleteItem(id);
        });
        Mockito.verify(storageRepository, times(1)).deleteById(id);
    }

    @Test
    public void findAllItems_EmptyList_Test() {
        // GIVEN
        List<ItemRecord> itemRecords = new ArrayList<>();
        when(storageRepository.findAll()).thenReturn(itemRecords);

        // WHEN
        List<Item> items = storageService.findAllItems();

        //THEN
        assertNotNull(items);
        assertTrue(items.isEmpty());
        Mockito.verify(storageRepository, times(1)).findAll();
    }
}