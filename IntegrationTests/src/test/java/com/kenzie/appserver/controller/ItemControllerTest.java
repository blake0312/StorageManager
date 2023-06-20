package com.kenzie.appserver.controller;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.ItemCreateRequest;
import com.kenzie.appserver.service.StorageService;
import com.kenzie.appserver.service.model.Item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
class ItemControllerTest {
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

        Item item = new Item(id, name);
        Item persistedItem = storageService.addNewItem(item);
        mvc.perform(get("/example/{id}", persistedItem.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id")
                        .value(is(id)))
                .andExpect(jsonPath("name")
                        .value(is(name)))
                .andExpect(status().isOk());
    }

    @Test
    public void createExample_CreateSuccessful() throws Exception {
        String name = mockNeat.strings().valStr();

        ItemCreateRequest itemCreateRequest = new ItemCreateRequest();
        itemCreateRequest.setName(name);

        mapper.registerModule(new JavaTimeModule());

        mvc.perform(post("/example")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemCreateRequest)))
                .andExpect(jsonPath("id")
                        .exists())
                .andExpect(jsonPath("name")
                        .value(is(name)))
                .andExpect(status().isCreated());
    }
}