package com.example.zalpia.ui.mycart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zalpia.R;
import com.example.zalpia.databinding.FragmentMyCartBinding;

import java.util.ArrayList;


public class MyCartFragment extends Fragment {
FragmentMyCartBinding binding;
ArrayList<ItemModel> itemModels =new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_cart, container, false);
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_my_cart, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        itemModels.add(new ItemModel("sushi","it's very good sushi" , "50.6EGP",R.drawable.sushi_icon_png));
        itemModels.add(new ItemModel("mango","it's very nice mang" , "20.86EGP",R.drawable.mango));
        itemModels.add(new ItemModel("pepsi","it's good pepsi" , "10.6EGP",R.drawable.pepsi));
MyCartAdapter myCartAdapter=new MyCartAdapter(itemModels);
binding.mycartRvOrdersCat.setAdapter(myCartAdapter);

    }
}