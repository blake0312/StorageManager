package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "Items")
public class ItemRecord {

    private String id;
    private String name;
    private Double value;
    private String status;
    private String description;
    private Integer quantity;
    private Boolean inStorage;
    private String storageLocation;
    private Integer usageCount;

    @DynamoDBHashKey(attributeName = "Id")
    public String getId() {
        return id;
    }

    @DynamoDBAttribute(attributeName = "Name")
    public String getName() {
        return name;
    }

    @DynamoDBAttribute(attributeName = "Value")
    public Double getValue() {
        return value;
    }

    @DynamoDBAttribute(attributeName = "Status")
    public String getStatus() {
        return status;
    }

    @DynamoDBAttribute(attributeName = "Description")
    public String getDescription() {
        return description;
    }

    @DynamoDBAttribute(attributeName = "Quantity")
    public Integer getQuantity() {
        return quantity;
    }

    @DynamoDBAttribute(attributeName = "InStorage")
    public Boolean getInStorage() {
        return inStorage;
    }

    @DynamoDBAttribute(attributeName = "StorageLocation")
    public String getStorageLocation() {
        return storageLocation;
    }

    @DynamoDBAttribute(attributeName = "UsageCount")
    public Integer getUsageCount() {
        return usageCount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setInStorage(Boolean inStorage) {
        this.inStorage = inStorage;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemRecord itemRecord = (ItemRecord) o;
        return Objects.equals(id, itemRecord.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
