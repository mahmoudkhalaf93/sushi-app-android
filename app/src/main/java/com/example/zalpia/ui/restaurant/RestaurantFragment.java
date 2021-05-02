package com.example.zalpia.ui.restaurant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zalpia.R;
import com.example.zalpia.databinding.FragmentRestaurantBinding;


public class RestaurantFragment extends Fragment {
FragmentRestaurantBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_restaurant,container,false);
        // Inflate the layout for this fragment  inflater.inflate(R.layout.fragment_restaurant, container, false)
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}