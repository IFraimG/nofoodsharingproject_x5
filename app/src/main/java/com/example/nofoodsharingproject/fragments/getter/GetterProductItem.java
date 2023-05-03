package com.example.nofoodsharingproject.fragments.getter;

public class GetterProductItem {
    private String name;

    public GetterProductItem(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

}
