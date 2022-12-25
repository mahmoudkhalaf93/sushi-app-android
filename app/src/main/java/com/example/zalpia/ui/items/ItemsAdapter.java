package com.example.zalpia.ui.items;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zalpia.R;
import com.example.zalpia.databinding.LayoutItemBinding;
import com.example.zalpia.room.ItemModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsAdapterViewHolder> {
    private static final String TAG = "ItemsAdapter1";
    ArrayList<ItemModel> itemModelArrayList;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public ItemsAdapter(ArrayList<ItemModel> itemModelArrayList) {
        this.itemModelArrayList = itemModelArrayList;
    }

    @NonNull
    @Override
    public ItemsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_item, parent, false);
        return new ItemsAdapterViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ItemsAdapterViewHolder holder, int position) {
        ItemModel itemModel = itemModelArrayList.get(position);

        firestore.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .collection("mycart").document(itemModel.getFirebaseId()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.getData() != null) {
                        holder.binding.rightIcon.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                    } else {
                        holder.binding.rightIcon.setImageResource(R.drawable.ic_twotone_shopping_cart_24_orange);
                    }
                }).addOnFailureListener(e -> Log.i(TAG, "onFailure: ", e));

        holder.itemView.setOnClickListener(view -> firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                .collection("mycart").document(itemModel.getFirebaseId()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.getData() != null) {
                        firestore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("mycart")
                                .document(itemModel.getFirebaseId()).delete().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        holder.binding.rightIcon.setImageResource(R.drawable.ic_twotone_shopping_cart_24_orange);
                                        Snackbar.make(view,  R.string.item_deleted, Snackbar.LENGTH_SHORT).
                                                setAction("done", null).show();
                                    } else {
                                        Log.i(TAG, "onComplete: ", task.getException());
                                    }
                                });
                    } else {
                        itemModel.setQuantity(1);
                        firestore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("mycart")
                                .document(itemModel.getFirebaseId()).set(itemModel).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        holder.binding.rightIcon.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                                        Snackbar.make(view, R.string.added, Snackbar.LENGTH_SHORT).
                                                setAction("done", null).show();
                                    } else {
                                        Log.i(TAG, "onComplete: ", task.getException());
                                    }
                                });
                    }
                }).addOnFailureListener(e -> Log.i(TAG, "onFailure: ", e)));



        if(Locale.getDefault().getDisplayLanguage().equals("العربية"))
        {
            holder.binding.itmeDescription.setText(itemModel.getDescriptionAr());
            holder.binding.itmeName.setText(itemModel.getNameAr());
        }
        else {
            holder.binding.itmeDescription.setText(itemModel.getDescription());
            holder.binding.itmeName.setText(itemModel.getName());
        }



        holder.binding.itmePrice.setText(NumberFormat.getInstance().format(itemModel.getPrice()) + holder.itemView.getContext().getString(R.string.EGP));
        holder.binding.itemImage.setImageURI(Uri.parse(itemModel.getImage()));

    }

    @Override
    public int getItemCount() {
        return itemModelArrayList.size();
    }

    static class ItemsAdapterViewHolder extends RecyclerView.ViewHolder {
        LayoutItemBinding binding;

        public ItemsAdapterViewHolder(@NonNull LayoutItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
