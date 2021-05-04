package com.example.zalpia.ui.favorites;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zalpia.Products;
import com.example.zalpia.R;
import com.example.zalpia.databinding.FragmentFavoritesBinding;
import com.example.zalpia.room.DaoDatabases;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
FavoritesViewModel viewModel;
FragmentFavoritesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_favorites,container,false);
        viewModel = new ViewModelProvider(getActivity()).get(FavoritesViewModel.class);
        binding.addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int j=0;j<6;j++){
                    Favorites favorites = new Favorites("Product "+j,"Description "+j+" this product is so beautiful i love sushi so much i hope i can cook it really really it's a good sushi  ");
                    DaoDatabases.getInstance(getActivity()).daoDML().addToFavorites(favorites);

                }
                Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();
                viewModel.setFavoritesValue(getActivity());
            }
        });
        viewModel.getFavoritesList().observe(getViewLifecycleOwner(), new Observer<List<Favorites>>() {
            @Override
            public void onChanged(List<Favorites> favorites) {
                FavoritesAdapter favoritesAdapter = new FavoritesAdapter(favorites);
                binding.favoriteRv.setAdapter(favoritesAdapter);
            }
        });
        viewModel.setFavoritesValue(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}