package com.raulrh.tiendatelevisiones.entities;

import org.bson.types.ObjectId;

import java.time.LocalDate;

public class Television {
    private ObjectId id;  // Will map to "_id" in MongoDB
    private String model;
    private String brand;
    private Double price;
    private LocalDate releaseDate;
    private Short type;
    private Boolean isSmart = false;

    // Constructors
    public Television() {
    }

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Boolean getIsSmart() {
        return isSmart;
    }

    public void setIsSmart(Boolean isSmart) {
        this.isSmart = isSmart;
    }

    @Override
    public String toString() {
        return getId() + " - " + getModel();
    }
}