package com.example.zalpia.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.zalpia.R;
import com.example.zalpia.databinding.FragmentOrderBinding;
import com.example.zalpia.room.CategoryModel;

import java.util.List;
import java.util.Locale;

public class OrderFragment extends Fragment {

    private OrderViewModel orderViewModel;
FragmentOrderBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_order, container, false);
        orderViewModel =
                new ViewModelProvider(this).get(OrderViewModel.class);

        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
        if(Locale.getDefault().getDisplayLanguage().equals("العربية"))
        binding.backgroundImage.setImageResource(R.drawable.layer_2_copy_3rtl);
        else
            binding.backgroundImage.setImageResource(R.drawable.layer_2_copy_3);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderViewModel.getCatModelList().observe(getViewLifecycleOwner(), new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> categoryModels) {
                CategoryAdapter categoryAdapter =new CategoryAdapter(categoryModels);
                binding.rvOrdersCat.setAdapter(categoryAdapter);
            }
        });
        orderViewModel.setCatModelList();




        binding.homeFavoriteFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavController navController= Navigation.findNavController(view);
                navController.navigate(R.id.action_nav_order_to_nav_favorites);

            }
        });
        binding.homeMyCartFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavController navController= Navigation.findNavController(view);
                navController.navigate(R.id.action_nav_order_to_nav_mycart);

            }
        });
    }
}