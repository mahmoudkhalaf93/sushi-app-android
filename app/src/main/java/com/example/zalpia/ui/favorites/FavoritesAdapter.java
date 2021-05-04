package com.example.zalpia.ui.favorites;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zalpia.R;
import com.example.zalpia.databinding.LayoutItemBinding;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {
    List<Favorites> favoritesList;

    public FavoritesAdapter(List<Favorites> favoritesList) {
        this.favoritesList = favoritesList;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_item, parent, false);

        return new FavoritesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        Favorites favorites = favoritesList.get(position);
        holder.binding.rightIcon.setImageResource(R.drawable.ic_baseline_favorite_24);
        holder.binding.itmeName.setText(favorites.getName());
        holder.binding.itmeDescription.setText(favorites.getDescription());
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    class FavoritesViewHolder extends RecyclerView.ViewHolder {
        LayoutItemBinding binding;

        public FavoritesViewHolder(@NonNull LayoutItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
