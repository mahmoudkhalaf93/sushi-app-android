package com.example.zalpia.room;


import java.io.Serializable;


public class ItemModel implements Serializable {


    private String name ;
    private String nameAr;
    private String description ;
    private String descriptionAr ;
    private Double price ;
    private String image;
    private int quantity;
    private String firebaseId;

    public ItemModel() {

    }

    public ItemModel(String name, String description, double price, String image, String firebaseId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.firebaseId = firebaseId;
    }

    public ItemModel(String name, String description, double price, String image, int quantity, String firebaseId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
        this.firebaseId = firebaseId;

    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getDescriptionAr() {
        return descriptionAr;
    }

    public void setDescriptionAr(String descriptionAr) {
        this.descriptionAr = descriptionAr;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
