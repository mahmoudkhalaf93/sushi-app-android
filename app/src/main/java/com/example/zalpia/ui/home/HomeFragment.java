package com.example.zalpia.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;

import com.example.zalpia.R;
import com.example.zalpia.databinding.FragmentHomeBinding;
import com.facebook.drawee.backends.pipeline.Fresco;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    private OffersAdapter offersAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(requireActivity());
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        homeViewModel.getOfferList().observe(getViewLifecycleOwner(), offerModels -> {
            offersAdapter = new OffersAdapter(getActivity(), offerModels);
            binding.rvHomepageOffers.setAdapter(offersAdapter);
        });

        homeViewModel.setOfferList();
        return binding.getRoot();
    }
}