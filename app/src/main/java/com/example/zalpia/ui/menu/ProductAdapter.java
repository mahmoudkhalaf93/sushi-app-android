package com.example.zalpia.ui.menu;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zalpia.R;
import com.example.zalpia.room.ItemModel;

import java.util.List;
import java.util.Locale;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ItemViewHolder> {
    Context context;
    List<ItemModel> ItemModelList;

    public ProductAdapter(Context context, List<ItemModel> ItemModel) {
        this.context = context;
        this.ItemModelList = ItemModel;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemModel product = ItemModelList.get(position);

        if(Locale.getDefault().getDisplayLanguage().equals("العربية"))
        {
            holder.itemName.setText(product.getNameAr());
            holder.itemDesc.setText(product.getDescriptionAr());
        }
        else {
            holder.itemName.setText(product.getName());
            holder.itemDesc.setText(product.getDescription());
        }

        holder.itemImage.setImageURI(Uri.parse(product.getImage()));

       // Picasso.with(context).load(product.getImage()).into(holder.itemImage);
        holder.itemView.setOnClickListener(view -> {
            final NavController navController = Navigation.findNavController(view);

            MenuFragmentDirections.ActionNavMenuToItemProfileFragment action =
                    MenuFragmentDirections.actionNavMenuToItemProfileFragment(product);

            navController.navigate(action);
        });

    }

    @Override
    public int getItemCount() {
        return ItemModelList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName, itemDesc;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.itme_name);
            itemDesc = itemView.findViewById(R.id.itme_description);
        }
    }
}
