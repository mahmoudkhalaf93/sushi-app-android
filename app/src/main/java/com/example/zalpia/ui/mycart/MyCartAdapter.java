package com.example.zalpia.ui.mycart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zalpia.R;
import com.example.zalpia.databinding.ItemMycartBinding;
import com.example.zalpia.room.ItemModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartAdapterViewHolder> {
    private static final String TAG = "MyCartAdapter1";
    ArrayList<ItemModel> itemModelsList;
    TextView totalPriceTv;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    double totalPrice;

    public MyCartAdapter(ArrayList<ItemModel> itemModelsList, TextView totalPriceTv, double totalPrice) {
        this.itemModelsList = itemModelsList;
        this.totalPriceTv = totalPriceTv;
        this.totalPrice = totalPrice;
    }

    @NonNull
    @Override
    public MyCartAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMycartBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_mycart, parent, false);
        return new MyCartAdapterViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyCartAdapterViewHolder holder, int position) {
        ItemModel itemModel = itemModelsList.get(position);
        Context context = holder.itemView.getContext();
        // holder.binding.itemMycartImage.setImageResource(itemModel.getImage());
//        FavoritesViewModel viewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(FavoritesViewModel.class);

        //  Picasso.with(context).load(itemModel.getImage()).into(holder.binding.itemMycartImage);

        if(Locale.getDefault().getDisplayLanguage().equals("العربية"))
        {
            holder.binding.itmeMycartName.setText(itemModel.getNameAr());
            holder.binding.itmeMycartDescription.setText(itemModel.getDescriptionAr());
        }
        else {
            holder.binding.itmeMycartName.setText(itemModel.getName());
            holder.binding.itmeMycartDescription.setText(itemModel.getDescription());
        }
        holder.binding.itemMycartImage.setImageURI(Uri.parse(itemModel.getImage()));
        holder.binding.itmeMycartPrice.setText(NumberFormat.getInstance().format(itemModel.getPrice())+context.getString(R.string.EGP));
        holder.binding.quantity.setText(String.valueOf(itemModel.getQuantity()));
        final int[] quant = {itemModel.getQuantity()};
        // holder.binding.quantity.setText(String.valueOf(quant[0]));
        holder.binding.plus.setOnClickListener(view -> {
            if (quant[0] < 10) {
                quant[0]++;
                //updata quantity to firestore
                try { itemModelsList.get(position).setQuantity(quant[0]);
                    HashMap<String,Object> updateQuant = new HashMap<>();
                    updateQuant.put("quantity", quant[0]);
                    firestore.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                            .collection("mycart").document(itemModel.getFirebaseId()).update(updateQuant).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Log.i(TAG, "onFailure: ",e);
                        }
                    });
//                            .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            removeAt(position);
//                            totalPrice = totalPrice - itemModel.getPrice();
//                            totalPriceTv.setText(NumberFormat.getInstance().format(totalPrice)+context.getString(R.string.EGP));
//                            Snackbar.make(view,  R.string.your_item_has_been_deleted, Snackbar.LENGTH_LONG)
//                                    .setAction(R.string.done, null).show();
//                        } else
//                            Log.i(TAG, "onComplete: ", task.getException());
//                    });
                }catch (Exception ex){
                    Log.i(TAG, "onBindViewHolder: ",ex);
                }
                //end of updata quantity to firestore
                totalPrice = totalPrice + itemModel.getPrice();
                totalPriceTv.setText(NumberFormat.getInstance().format(totalPrice)+context.getString(R.string.EGP));
                holder.binding.quantity.setText(String.valueOf(quant[0]));
            }
        });
        holder.binding.minus.setOnClickListener(view -> {
            if (quant[0] > 1) {
                quant[0]--;
                //updata quantity to firestore
                try { itemModelsList.get(position).setQuantity(quant[0]);
                    HashMap<String,Object> updateQuant = new HashMap<>();
                    updateQuant.put("quantity", quant[0]);
                    firestore.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                            .collection("mycart").document(itemModel.getFirebaseId()).update(updateQuant).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Log.i(TAG, "onFailure: ",e);
                        }
                    });
//                            .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            removeAt(position);
//                            totalPrice = totalPrice - itemModel.getPrice();
//                            totalPriceTv.setText(NumberFormat.getInstance().format(totalPrice)+context.getString(R.string.EGP));
//                            Snackbar.make(view,  R.string.your_item_has_been_deleted, Snackbar.LENGTH_LONG)
//                                    .setAction(R.string.done, null).show();
//                        } else
//                            Log.i(TAG, "onComplete: ", task.getException());
//                    });
                }catch (Exception ex){
                    Log.i(TAG, "onBindViewHolder: ",ex);
                }
                //end of updata quantity to firestore
                totalPrice = totalPrice - itemModel.getPrice();
                totalPriceTv.setText(NumberFormat.getInstance().format(totalPrice)+context.getString(R.string.EGP));
                holder.binding.quantity.setText(String.valueOf(quant[0]));

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage(context.getResources().getString(R.string.dialog_fire_missiles)
                        + " " + itemModel.getName())
                        .setIcon(R.drawable.ic_baseline_delete_forever_24)
                        .setPositiveButton(R.string.yes, (dialog, id) -> firestore.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                .collection("mycart").document(itemModel.getFirebaseId()).delete().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        removeAt(position);
                                        totalPrice = totalPrice - itemModel.getPrice();
                                        totalPriceTv.setText(NumberFormat.getInstance().format(totalPrice)+context.getString(R.string.EGP));
                                        Snackbar.make(view,  R.string.your_item_has_been_deleted, Snackbar.LENGTH_LONG)
                                                .setAction(R.string.done, null).show();
                                    } else
                                        Log.i(TAG, "onComplete: ", task.getException());
                                }))
                        .setNegativeButton(R.string.no, null).create().show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemModelsList.size();
    }

    public void removeAt(int position) {
        itemModelsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemModelsList.size());
    }

    static class MyCartAdapterViewHolder extends RecyclerView.ViewHolder {
        ItemMycartBinding binding;

        public MyCartAdapterViewHolder(@NonNull ItemMycartBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
