package com.example.zalpia.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.zalpia.R;
import com.example.zalpia.databinding.FragmentOrderBinding;
import com.example.zalpia.room.DaoDatabases;

public class OrderFragment extends Fragment {

    private OrderViewModel orderViewModel;
FragmentOrderBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_order, container, false);
        orderViewModel =
                new ViewModelProvider(this).get(OrderViewModel.class);
        //View root = inflater.inflate(R.layout.fragment_order, container, false);

        orderViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.textOrder.setText(s);
            }
        });
        return binding.getRoot();
    }

}