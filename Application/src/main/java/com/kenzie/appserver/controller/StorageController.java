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

        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setId(item.getId());
        itemResponse.setName(item.getName());
        return ResponseEntity.ok(itemResponse);
    }

    @PostMapping
    public ResponseEntity<ItemResponse> addNewItem(@RequestBody ItemCreateRequest itemCreateRequest) {
        Item item = new Item(randomUUID().toString(),
                itemCreateRequest.getName());
        storageService.addNewItem(item);

        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setId(item.getId());
        itemResponse.setName(item.getName());

        return ResponseEntity.created(URI.create("/item/" + itemResponse.getId())).body(itemResponse);
    }
}
