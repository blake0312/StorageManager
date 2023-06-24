package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.ItemCreateRequest;
import com.kenzie.appserver.service.StorageService;
import com.kenzie.appserver.service.model.Item;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class StorageControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    StorageService storageService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();


    @Test
    public void getById_Exists() throws Exception {
        String id = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String status = mockNeat.strings().valStr();
        String description = mockNeat.strings().valStr();

        Item item = new Item(id, name,10.0,status,description,1);
        Item persistedItem = storageService.addNewItem(item);
        mvc.perform(get("/item/{id}", persistedItem.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id")
                        .value(is(id)))
                .andExpect(jsonPath("name")
                        .value(is(name)))
                .andExpect(status().isOk());
    }

    @Test
    public void createItem_ValidItem_CreateSuccessful() throws Exception {
        String name = mockNeat.strings().valStr();
        String status = mockNeat.strings().get();
        ItemCreateRequest itemCreateRequest = new ItemCreateRequest();
        itemCreateRequest.setName(name);
        itemCreateRequest.setStatus(status);
        itemCreateRequest.setDescription(mockNeat.strings().get());
        itemCreateRequest.setQuantity(1);
        itemCreateRequest.setValue(10.0);


        mvc.perform(post("/item")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemCreateRequest)))
                .andExpect(jsonPath("id")
                        .exists())
                .andExpect(jsonPath("name")
                        .value(is(name)))
                .andExpect(jsonPath("status")
                        .value(is(status)))
                .andExpect(status().isCreated());
    }
    @Test
    public void updateItem_ExistingItem_UpdateSuccessful() throws Exception {
        String id = UUID.randomUUID().toString();

        //Add item to make sure it exist before updating
        Item item = new Item(id, "name",10.0,"status","description",1);
        storageService.addNewItem(item);

        //Create the ItemCreateRequest with updated data
        String updatedName = "Updated Item Name";
        String updatedStatus = "Updated Item Status";
        String updatedDescription = "Updated Item Description";
        ItemCreateRequest itemCreateRequest = new ItemCreateRequest();
        itemCreateRequest.setId(id);
        itemCreateRequest.setName(updatedName);
        itemCreateRequest.setStatus(updatedStatus);
        itemCreateRequest.setDescription(updatedDescription);
        itemCreateRequest.setQuantity(2);
        itemCreateRequest.setValue(20.0);

        mvc.perform(put("/item/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(is(id)))
                .andExpect(jsonPath("name")
                        .value(is(updatedName)))
                .andExpect(jsonPath("status")
                        .value(is(updatedStatus)))
                .andExpect(jsonPath("description")
                        .value(is(updatedDescription)));
    }

    @Test
    public void updateItem_InvalidItem_UpdateFail() throws Exception {
        //Item does not exist yet, Service will return null, Controller then returns notfound 4xx error.
        String name = mockNeat.strings().valStr();
        String status = mockNeat.strings().get();
        String id = UUID.randomUUID().toString();
        ItemCreateRequest itemCreateRequest = new ItemCreateRequest();
        itemCreateRequest.setId(id);
        itemCreateRequest.setName(name);
        itemCreateRequest.setStatus(status);
        itemCreateRequest.setDescription(mockNeat.strings().get());
        itemCreateRequest.setQuantity(1);
        itemCreateRequest.setValue(10.0);


        mvc.perform(put("/item/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemCreateRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void deleteItem_ValidItem_DeleteSuccess() throws Exception {
        String id = UUID.randomUUID().toString();

        //Add item to make sure it exist before deleting
        Item item = new Item(id, "name",10.0,"status","description",1);
        storageService.addNewItem(item);

        //Check to make sure item exist prior to delete request
        Assertions.assertNotNull(storageService.findById(id));

        mvc.perform(delete("/item/{id}", id))
                .andExpect(status().isNoContent());

        //Check after delete request if item still exist
        Assertions.assertNull(storageService.findById(id));
    }
}