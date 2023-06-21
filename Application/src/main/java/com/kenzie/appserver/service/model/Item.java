package com.kenzie.appserver.service.model;


public class Item {
    private final String id;
    private final String name;
    private final Double value;
    private final String status;
    private final String description;
    private final Integer quantity;
    private final Boolean inStorage;
    private final String storageLocation;

    public Item(String id, String name, Double value, String status, String description, Integer quantity) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.status = status;
        this.description = description;
        this.quantity = quantity;
        this.inStorage = false;
        this.storageLocation = null;
    }

    public Item(String id, String name, Double value, String status, String description, Integer quantity, Boolean inStorage, String storageLocation) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.status = status;
        this.description = description;
        this.quantity = quantity;
        this.inStorage = inStorage;
        this.storageLocation = storageLocation;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Boolean getInStorage() {
        return inStorage;
    }

    public String getStorageLocation() {
        return storageLocation;
    }
}
