package com.zhan.models;

/**
 * Created by Zhan on 2014-10-12.
 */
public class Category {

    private int id;
    private String title;

    public Category(){}

    public Category(int aId, String aTitle){
        this.id = aId;
        this.title = aTitle;
    }

    //Getters
    public int getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }


    //Setters
    public void setId(int aId){
        this.id = aId;
    }

    public void setTitle(String aTitle){
        this.title = aTitle;
    }

}
