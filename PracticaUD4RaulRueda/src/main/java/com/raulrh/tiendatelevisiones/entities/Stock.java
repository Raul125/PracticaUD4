package com.raulrh.tiendatelevisiones.entities;

import org.bson.types.ObjectId;

import java.time.LocalDate;

public class Stock {
    private ObjectId id;
    private ObjectId televisionId;  // Embedded Television object
    private ObjectId supplierId;      // Embedded Supplier object
    private Integer quantity;
    private LocalDate entryDate;

    // Constructors
    public Stock() {
    }

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getTelevisionId() {
        return televisionId;
    }

    public void setTelevisionId(ObjectId id) {
        this.televisionId = id;
    }

    public ObjectId getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(ObjectId id) {
        this.supplierId = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }
}