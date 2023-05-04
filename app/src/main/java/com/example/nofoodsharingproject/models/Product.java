package com.example.nofoodsharingproject.models;

public class Product {
    String title;
    String type;
    String id;
    String cost;

    public Product(String title) {
        this.title = title;
    }

    public Product(String title, String cost) {
        this.title = title;
        this.cost = cost;
    }

    public Product(String title, String cost, String id, String type) {
        this.title = title;
        this.cost = cost;
        this.id = id;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
