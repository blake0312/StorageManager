package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.model.ItemRecord;
import com.kenzie.appserver.repositories.StorageRepository;
import com.kenzie.appserver.service.model.Item;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StorageService {
    private final StorageRepository storageRepository;

    public StorageService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    public Item findById(String id) {
        Item itemFromBackend = storageRepository
                .findById(id)
                .map(item -> new Item(item.getId(), item.getName(), item.getValue(), item.getStatus(),
                        item.getDescription(), item.getQuantity(), item.getInStorage(), item.getStorageLocation(), item.getUsageCount()))
                .orElse(null);

        return itemFromBackend;
    }

    public Item addNewItem(Item item) {
        ItemRecord itemRecord = convertToItemRecord(item);
        storageRepository.save(itemRecord);
        return item;
    }

    public Item updateItem(Item item) {
        Optional<ItemRecord> itemRecordOptional = storageRepository.findById(item.getId());

        if (itemRecordOptional.isPresent()) {
            ItemRecord itemRecord = itemRecordOptional.get();
            itemRecord.setName(item.getName());
            itemRecord.setValue(item.getValue());
            itemRecord.setStatus(item.getStatus());
            itemRecord.setDescription(item.getDescription());
            itemRecord.setQuantity(item.getQuantity());
            //itemRecord.setInStorage(item.getInStorage());
            itemRecord.setStorageLocation(item.getStorageLocation());

            if (itemRecord.getInStorage() != item.getInStorage()) {
                // update count
                itemRecord.setInStorage(item.getInStorage());
                itemRecord.setUsageCount(item.getUsageCount() + 1);
            }
            else {
                itemRecord.setInStorage(item.getInStorage());
                itemRecord.setUsageCount(item.getUsageCount());
            }
            storageRepository.save(itemRecord);
            return item;
        }
        return null;
    }

    public void deleteItem(String id) {
        storageRepository.deleteById(id);
    }

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

    private Item convertToItem(ItemRecord itemRecord) {
        return new Item(itemRecord.getId(), itemRecord.getName(), itemRecord.getValue(),
                itemRecord.getStatus(), itemRecord.getDescription(), itemRecord.getQuantity(),
                itemRecord.getInStorage(), itemRecord.getStorageLocation(), itemRecord.getUsageCount());
    }


    public List<Item> findAllItems() {
        List<ItemRecord> itemRecords = (List<ItemRecord>) storageRepository.findAll();
        List<Item> items = new ArrayList<>();
        for(ItemRecord itemRecord: itemRecords) {
            items.add(convertToItem(itemRecord));
        }
        return items;
    }

}
