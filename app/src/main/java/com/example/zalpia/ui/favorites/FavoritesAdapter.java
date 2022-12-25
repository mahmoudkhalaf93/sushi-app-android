package com.example.zalpia.ui.favorites;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zalpia.R;
import com.example.zalpia.databinding.LayoutItemBinding;
import com.example.zalpia.room.ItemModel;

import java.util.List;
import java.util.Locale;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {
    List<ItemModel> itemModelList;
    Context context;

    public FavoritesAdapter(List<ItemModel> itemModelList, Context context) {
        this.itemModelList = itemModelList;
        this.context=context;

    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_item, parent, false);

        return new FavoritesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        ItemModel itemModel = itemModelList.get(position);
        if(Locale.getDefault().getDisplayLanguage().equals("العربية"))
        {
            holder.binding.itmeName.setText(itemModel.getNameAr());
            holder.binding.itmeDescription.setText(itemModel.getDescriptionAr());
        }
        else {
            holder.binding.itmeName.setText(itemModel.getName());
            holder.binding.itmeDescription.setText(itemModel.getDescription());
        }
        holder.binding.rightIcon.setImageResource(R.drawable.ic_baseline_favorite_24);

        holder.binding.itemImage.setImageURI(Uri.parse(itemModel.getImage()));







        holder.itemView.setOnClickListener(view -> {
            final NavController navController = Navigation.findNavController(view);

            FavoritesFragmentDirections.ActionNavFavoritesToItemProfileFavFragment action =
                    FavoritesFragmentDirections.actionNavFavoritesToItemProfileFavFragment(itemModel);
            navController.navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }






    static class FavoritesViewHolder extends RecyclerView.ViewHolder {
        LayoutItemBinding binding;

        public FavoritesViewHolder(@NonNull LayoutItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
