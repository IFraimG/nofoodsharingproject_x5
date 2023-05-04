package com.example.nofoodsharingproject.models;

public class GetterProductItem extends Product {
    private String name;

    public GetterProductItem(String name){
        super(name);
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

}
