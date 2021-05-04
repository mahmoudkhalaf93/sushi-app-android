package com.example.zalpia.ui.mycart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zalpia.R;
import com.example.zalpia.databinding.ItemMycartBinding;

import java.util.ArrayList;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartAdapterViewHolder> {
    ArrayList<ItemModel> itemModelsList;
    int quant=0;
    public MyCartAdapter(ArrayList<ItemModel> itemModelsList) {
        this.itemModelsList = itemModelsList;
    }

    @NonNull
    @Override
    public MyCartAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMycartBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_mycart, parent, false);
        return new MyCartAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartAdapterViewHolder holder, int position) {
        ItemModel itemModel = itemModelsList.get(position);
        holder.binding.itemMycartImage.setImageResource(itemModel.getImage());
        holder.binding.itmeMycartName.setText(itemModel.getName());
        holder.binding.itmeMycartPrice.setText(itemModel.getPrice());
        holder.binding.itmeMycartDescription.setText(itemModel.getDescription());
        holder.binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quant++;
                holder.binding.quantity.setText(quant);
            }
        });
        holder.binding.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quant--;
                holder.binding.quantity.setText(quant);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemModelsList.size();
    }

    class MyCartAdapterViewHolder extends RecyclerView.ViewHolder {
        ItemMycartBinding binding;

        public MyCartAdapterViewHolder(@NonNull ItemMycartBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
