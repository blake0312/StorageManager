package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.ItemCreateRequest;
import com.kenzie.appserver.controller.model.ItemResponse;
import com.kenzie.appserver.service.StorageService;

import com.kenzie.appserver.service.model.Item;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
                itemCreateRequest.getStorageLocation());

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
        return response;
    }
}
