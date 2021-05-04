package com.example.zalpia.ui.menu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zalpia.Products;
import com.example.zalpia.R;

import java.util.ArrayList;

public class MenuViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
private  MutableLiveData<ArrayList<CatModel>> catArray;
    public MenuViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is menu fragment");
        catArray=new MutableLiveData<>();
        catArray.setValue(setCatArray());
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<ArrayList<CatModel>> getCatList() {
        return catArray;
    }

    public    ArrayList<CatModel>  setCatArray(){

        ArrayList<CatModel> catList=new ArrayList<>();
        for(int i=0;i<7;i++){
            ArrayList<Products> productsList = new ArrayList<>();
            for(int j=0;j<8;j++){
                Products product = new Products("Product "+j,"Description "+j+" this product is so beautiful i love sushi so much i hope i can cook it really really it's a good sushi  ");
                productsList.add(product);
            }
            CatModel cat=new CatModel("Category "+i,productsList);
            catList.add(cat);
        }
        catList.get(0).setImage(R.drawable.cat1);
        catList.get(1).setImage(R.drawable.cat2);
        catList.get(2).setImage(R.drawable.cat3);

        ArrayList<Products> productsList0 = new ArrayList<>();
        Products product = new Products("piza ","Description  pizaa this product is so beautiful i love sushi so much i hope i can cook it really really it's a good sushi  ");
        productsList0.add(product);
        Products product0 = new Products("sushi ","Description  pizaashisi this product is so beautiful i love sushi so much i hope i can cook it really really it's a good sushi  ");
        productsList0.add(product0);
        Products product1 = new Products("pepsi ","Description  pespi pizaashisi this product is so beautiful i love sushi so much i hope i can cook it really really it's a good sushi  ");
        product1.setImage(R.drawable.pepsi);
        productsList0.add(product1);
        ArrayList<Products> productsList1 = new ArrayList<>();
        Products producta = new Products("zlpia ","Description  pizaa this product is so beautiful i love sushi so much i hope i can cook it really really it's a good sushi  ");
        producta.setImage(R.drawable.zalpia);
        productsList1.add(producta);
        Products product0a = new Products("manga ","Description  pizaashisi this product is so beautiful i love sushi so much i hope i can cook it really really it's a good sushi  ");
        product0a.setImage(R.drawable.mango);
        productsList1.add(product0a);
        Products product1a = new Products("blela ","Description  pespi pizaashisi this product is so beautiful i love sushi so much i hope i can cook it really really it's a good sushi  ");
        productsList1.add(product1a);
        Products product2a = new Products("banana ","Description  bananapespi pizaashisi this product is so beautiful i love sushi so much i hope i can cook it really really it's a good sushi  ");
        productsList1.add(product2a);
        catList.get(0).setProductsList(productsList0);
        catList.get(1).setProductsList(productsList1);
return  catList;
    }


}