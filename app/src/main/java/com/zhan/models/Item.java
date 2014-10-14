package com.zhan.models;

import java.util.Date;

/**
 * Created by Zhan on 2014-10-12.
 */
public class Item {
    private int id;
    private String title,type;
    private Date date;
    private Category category;
    private Double price;

    public Item(){}

    public Item(int aId, String aTitle, String aType, Date aDate, Category aCategory, Double aPrice){
        this.id = aId;
        this.title = aTitle;
        this.type = aType;
        this.date = aDate;
        this.category = aCategory;
        this.price = aPrice;
    }

    //Getters
    public int getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public String getType(){
        return this.type;
    }

    public Date getDate(){
        return this.date;
    }

    public Category getCategory(){
        return this.category;
    }

    public Double getPrice(){
        return this.price;
    }

    //Setters
    public void setId(int aId){
        this.id = aId;
    }

    public void setTitle(String aTitle){
        this.title = aTitle;
    }

    public void setType(String aType){
        this.type = aType;
    }

    public void setDate(Date aDate){
        this.date = aDate;
    }

    public void setCategory(Category aCategory){
        this.category = aCategory;
    }

    public void setPrice(Double aPrice){
        this.price = aPrice;
    }
}
