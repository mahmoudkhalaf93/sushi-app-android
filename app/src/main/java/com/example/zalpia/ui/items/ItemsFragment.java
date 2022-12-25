package com.example.zalpia.ui.items;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.zalpia.R;
import com.example.zalpia.databinding.FragmentItemsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class ItemsFragment extends Fragment {
FragmentItemsBinding binding;
ItemsAdapter adapter;
ItemsViewModel viewModel;
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_items, container, false);

viewModel =new ViewModelProvider(this).get(ItemsViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            ItemsFragmentArgs fragmentArgs= ItemsFragmentArgs.fromBundle(getArguments());

            if(Locale.getDefault().getDisplayLanguage().equals("العربية"))
            {
                binding.catNameitems.setText(fragmentArgs.getNameAr());
            }
            else {
                binding.catNameitems.setText(fragmentArgs.getName());
            }


           viewModel.getItemModelList().observe(getViewLifecycleOwner(), modelArrayList -> {
               adapter = new ItemsAdapter(modelArrayList);
               binding.rvItemsCat.setAdapter(adapter);
           });
           viewModel.setItemModelList(fragmentArgs.getFirbaseId(),fragmentArgs.getOfferOrCats());
        }


        binding.itemsFavoriteFloatingButton.setOnClickListener(view12 -> {

            NavController navController= Navigation.findNavController(view12);
            navController.navigate(R.id.action_itemsFragment_to_nav_favorites);

        });
        binding.itemsMyCartFloatingButton.setOnClickListener(view1 -> {

            NavController navController= Navigation.findNavController(view1);
            navController.navigate(R.id.action_itemsFragment_to_nav_mycart);

        });

    }
    @Override
    public void onResume() {
        super.onResume();
        if(Locale.getDefault().getDisplayLanguage().equals("العربية"))
        { binding.imageView14.setImageResource(R.drawable.layer_2_copy_3rtl);

        }
        else {
            binding.imageView14.setImageResource(R.drawable.layer_2_copy_3);
        }
    }


}