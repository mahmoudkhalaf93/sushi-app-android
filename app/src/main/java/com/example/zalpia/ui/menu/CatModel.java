package com.example.zalpia.ui.menu;

import com.example.zalpia.Products;
import com.example.zalpia.R;

import java.util.ArrayList;
import java.util.List;

public class CatModel {
    String name,description;
    int image= R.drawable.catdef;
ArrayList<Products> productsList;

    public CatModel(String name, ArrayList<Products> productsList) {
        this.name = name;
        this.productsList = productsList;
    }

    public CatModel(String name, String description,  ArrayList<Products> productsList) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.productsList = productsList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public ArrayList<Products> getProductsList() {
        return productsList;
    }

    public void setProductsList(ArrayList<Products> productsList) {
        this.productsList = productsList;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
