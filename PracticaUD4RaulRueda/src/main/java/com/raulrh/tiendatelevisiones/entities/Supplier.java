package com.raulrh.tiendatelevisiones.entities;

import org.bson.types.ObjectId;

import java.util.LinkedHashSet;
import java.util.Set;

public class Supplier {
    private ObjectId id;  // Will map to "_id" in MongoDB
    private String name;
    private String phone;
    private String address;
    private String email;

    // Constructors
    public Supplier() {
    }

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return getId() + " - " + getName();
    }
}