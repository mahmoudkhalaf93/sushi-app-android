package com.example.zalpia;

import java.util.ArrayList;
import java.util.List;

public class Cat {
    String name;
    int image=R.drawable.catdef;
ArrayList<Products> productsList;

    public Cat(String name, ArrayList<Products> productsList) {
        this.name = name;
        this.productsList = productsList;
    }

    public ArrayList<Products> getProductsList() {
        return productsList;
    }

    public void setProductsList(ArrayList<Products> productsList) {
        this.productsList = productsList;
    }

    public int getimage() {
        return image;
    }

    public void setimage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
