package com.example.zalpia.ui.itemprofile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zalpia.R;
import com.example.zalpia.databinding.FragmentItemProfileBinding;


public class ItemProfileFragment extends Fragment {

    ItemProfileViewModel viewModel;
FragmentItemProfileBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_item_profile, container, false);
        viewModel = new ViewModelProvider(this).get(ItemProfileViewModel.class);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.itemProfileFavoriteSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.itemProfileHeartImage.setImageResource(R.drawable.ic_round_favorite_24);
            }
        });

    }
}