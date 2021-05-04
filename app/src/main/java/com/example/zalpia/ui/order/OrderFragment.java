package com.example.zalpia.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.zalpia.R;
import com.example.zalpia.databinding.FragmentOrderBinding;
import com.example.zalpia.room.DaoDatabases;
import com.example.zalpia.ui.menu.CatModel;

import java.util.ArrayList;

public class OrderFragment extends Fragment {

    private OrderViewModel orderViewModel;
FragmentOrderBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_order, container, false);
        orderViewModel =
                new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.textOrder.setText(s);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderViewModel.getCatModelList().observe(getViewLifecycleOwner(), new Observer<ArrayList<CatModel>>() {
            @Override
            public void onChanged(ArrayList<CatModel> catModels) {
                CatAdapter catAdapter=new CatAdapter(catModels);
              //  Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                binding.rvOrdersCat.setAdapter(catAdapter);
            }
        });

    }
}