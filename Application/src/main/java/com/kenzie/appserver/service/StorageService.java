package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.model.ItemRecord;
import com.kenzie.appserver.repositories.StorageRepository;
import com.kenzie.appserver.service.model.Item;

import org.springframework.stereotype.Service;

@Service
public class StorageService {
    private StorageRepository storageRepository;

    public StorageService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    public Item findById(String id) {
        Item itemFromBackend = storageRepository
                .findById(id)
                .map(item -> new Item(item.getId(), item.getName()))
                .orElse(null);

        return itemFromBackend;
    }

    public Item addNewItem(Item item) {
        ItemRecord itemRecord = new ItemRecord();
        itemRecord.setId(item.getId());
        itemRecord.setName(item.getName());
        storageRepository.save(itemRecord);
        return item;
    }
}
