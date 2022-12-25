package com.example.zalpia.ui.mycart;

import android.annotation.SuppressLint;
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
import com.example.zalpia.databinding.FragmentMyCartBinding;
import com.example.zalpia.room.ItemModel;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Locale;


public class MyCartFragment extends Fragment {
    FragmentMyCartBinding binding;
  MyCartViewModel viewModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_cart, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(MyCartViewModel.class);
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getMyCartList().observe(getViewLifecycleOwner(), itemModels -> {
            double totalPrice = 0 ;
            for (ItemModel model: itemModels) {
                totalPrice = totalPrice + (model.getPrice()*model.getQuantity());
            }
//            if(Locale.getDefault().getDisplayLanguage().equals("العربية"))
//                binding.totalPrice.setText(" جنية"+NumberFormat.getInstance().format(totalPrice));
//                else
            binding.totalPrice.setText(NumberFormat.getInstance().format(totalPrice)+getString(R.string.EGP));

            MyCartAdapter myCartAdapter = new MyCartAdapter(itemModels,binding.totalPrice,totalPrice);
            binding.mycartRvOrdersCat.setAdapter(myCartAdapter);

        });

        viewModel.setMyCartList();
        binding.addMoreInCart.setOnClickListener(view1 -> {
            NavController navController= Navigation.findNavController(view1);
            navController.navigate(R.id.action_nav_mycart_to_nav_order);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if(Locale.getDefault().getDisplayLanguage().equals("العربية"))
        { binding.imageView11.setImageResource(R.drawable.shopping_cart_background_rtl);
            binding.addMoreInCart.setImageResource(R.drawable.add_more_rtls);
        }
        else {
            binding.imageView11.setImageResource(R.drawable.shopping_cart_background);
            binding.addMoreInCart.setImageResource(R.drawable.add_more);
        }
    }
}