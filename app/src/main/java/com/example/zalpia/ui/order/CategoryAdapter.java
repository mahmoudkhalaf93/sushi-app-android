package com.example.zalpia.ui.order;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zalpia.R;
import com.example.zalpia.databinding.LayoutItemBinding;
import com.example.zalpia.room.CategoryModel;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CatViewHolder> {
    List<CategoryModel> categoryModelsList;

    public CategoryAdapter(List<CategoryModel> categoryModelsList) {
        this.categoryModelsList = categoryModelsList;
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_item, parent, false);
        return new CatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {
        CategoryModel categoryModel = categoryModelsList.get(position);


        if(Locale.getDefault().getDisplayLanguage().equals("العربية"))
        {
            holder.binding.itmeName.setText(categoryModel.getNameAr());
            holder.binding.itmeDescription.setText(categoryModel.getDescriptionAR());
        }
        else {
            holder.binding.itmeName.setText(categoryModel.getName());
            holder.binding.itmeDescription.setText(categoryModel.getDescription());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NavController navController = Navigation.findNavController(holder.itemView);
                OrderFragmentDirections.ActionNavOrderToItemsFragment action =
                        OrderFragmentDirections.actionNavOrderToItemsFragment(categoryModel.getName(), categoryModel.getFirebaseId()
                                ,"cat",categoryModel.getNameAr());
                navController.navigate(action);
            }
        });

      //  Picasso.with(holder.itemView.getContext()).load(categoryModel.getImage()).into(holder.binding.itemImage);
        holder.binding.itemImage.setImageURI(Uri.parse(categoryModel.getImage()));

    }

    @Override
    public int getItemCount() {
        return categoryModelsList.size();
    }

    public class CatViewHolder extends RecyclerView.ViewHolder {
        LayoutItemBinding binding;

        public CatViewHolder(@NonNull LayoutItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
