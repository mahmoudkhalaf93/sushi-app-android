package com.example.zalpia.ui.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zalpia.R;
import com.example.zalpia.databinding.LayoutItemBinding;
import com.example.zalpia.ui.menu.CatModel;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.CatViewHolder> {
    ArrayList<CatModel> catModelsList;

    public CatAdapter(ArrayList<CatModel> catModelsList) {
        this.catModelsList = catModelsList;
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_item,parent,false);
        return new CatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {
        CatModel catModel=catModelsList.get(position);
        holder.binding.itmeName.setText(catModel.getName());
        holder.binding.itemImage.setImageResource(catModel.getImage());
        holder.binding.itmeDescription.setText(catModel.getDescription());

    }

    @Override
    public int getItemCount() {
        return catModelsList.size();
    }

    public class CatViewHolder extends RecyclerView.ViewHolder{
LayoutItemBinding binding;
       public CatViewHolder(@NonNull LayoutItemBinding binding) {
           super(binding.getRoot());
           this.binding=binding;
       }
   }
}
