package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.ItemCreateRequest;
import com.kenzie.appserver.controller.model.ItemResponse;
import com.kenzie.appserver.service.StorageService;
import com.kenzie.appserver.service.model.Item;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

@RestController
@RequestMapping("/item")
public class StorageController {

    private StorageService storageService;
    StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> get(@PathVariable("id") String id) {

        Item item = storageService.findById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }

        // Using helper method
        ItemResponse itemResponse = createItemResponse(item);
        return ResponseEntity.ok(itemResponse);
    }

    @PostMapping
    public ResponseEntity<ItemResponse> addNewItem(@RequestBody ItemCreateRequest itemCreateRequest) {
        Item item = new Item(
                randomUUID().toString(),
                itemCreateRequest.getName(),
                itemCreateRequest.getValue(),
                itemCreateRequest.getStatus(),
                itemCreateRequest.getDescription(),
                itemCreateRequest.getQuantity(),
                itemCreateRequest.getInStorage(),
                itemCreateRequest.getStorageLocation(),
                itemCreateRequest.getUsageCount());

        storageService.addNewItem(item);

        // Using helper method
        ItemResponse itemResponse = createItemResponse(item);

        return ResponseEntity.created(URI.create("/item/" + itemResponse.getId())).body(itemResponse);
    }

    // Helper method to create Item response
    private ItemResponse createItemResponse(Item item) {
        ItemResponse response = new ItemResponse();
        response.setId(item.getId());
        response.setName(item.getName());
        response.setValue(item.getValue());
        response.setStatus(item.getStatus());
        response.setDescription(item.getDescription());
        response.setQuantity(item.getQuantity());
        response.setInStorage(item.getInStorage());
        response.setStorageLocation(item.getStorageLocation());
        response.setUsageCount(item.getUsageCount());
        return response;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable String id, @RequestBody ItemCreateRequest itemCreateRequest) {
        Item item = new Item(
                id,
                itemCreateRequest.getName(),
                itemCreateRequest.getValue(),
                itemCreateRequest.getStatus(),
                itemCreateRequest.getDescription(),
                itemCreateRequest.getQuantity(),
                itemCreateRequest.getInStorage(),
                itemCreateRequest.getStorageLocation(),
                itemCreateRequest.getUsageCount()
        );
        Item updatedItem = storageService.updateItem(item);
        if (updatedItem == null) {
            return ResponseEntity.notFound().build();
        }

        ItemResponse itemResponse = createItemResponse(updatedItem);
        return ResponseEntity.ok(itemResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") String id) {
        storageService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        List<Item> items = storageService.findAllItems();
        List<ItemResponse> itemResponses = new ArrayList<>();
        for(Item item: items) {
            ItemResponse itemResponse = createItemResponse(item);
            itemResponses.add(itemResponse);
        }
        return ResponseEntity.ok(itemResponses);
    }

}
