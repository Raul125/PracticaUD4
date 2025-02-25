package com.raulrh.tiendatelevisiones.entities;

import org.bson.types.ObjectId;

import java.time.LocalDate;

public class Sale {
    private ObjectId id;  // Will map to "_id" in MongoDB
    private ObjectId customerId;  // Reference to Customer
    private ObjectId televisionId;  // Reference to Television
    private LocalDate saleDate;
    private Integer quantity;
    private Double total;

    // Constructors
    public Sale() {
    }

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getCustomerId() {
        return customerId;
    }

    public void setCustomerId(ObjectId customerId) {
        this.customerId = customerId;
    }

    public ObjectId getTelevisionId() {
        return televisionId;
    }

    public void setTelevisionId(ObjectId televisionId) {
        this.televisionId = televisionId;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}