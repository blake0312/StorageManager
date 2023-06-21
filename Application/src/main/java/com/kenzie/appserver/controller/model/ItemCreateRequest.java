package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class ItemCreateRequest {

    @NotEmpty
    @JsonProperty("name")
    private String name;

    @NotEmpty
    @JsonProperty("Id")
    private String id;

    @JsonProperty("Value")
    private Double value;
    @NotEmpty
    @JsonProperty("Status")
    private String status;
    @NotEmpty
    @JsonProperty("Description")
    private String description;
    @NotEmpty
    @JsonProperty("Quantity")
    private Integer quantity;

    @JsonProperty("InStorage")
    private Boolean inStorage;

    @JsonProperty("StorageLocation")
    private String storageLocation;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getInStorage() {
        return inStorage;
    }

    public void setInStorage(Boolean inStorage) {
        this.inStorage = inStorage;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }
}
