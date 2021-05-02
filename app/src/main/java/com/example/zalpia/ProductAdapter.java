package com.example.zalpia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdapter extends  RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    Context context;
    ArrayList<Products> productsList=new ArrayList<>();

    public ProductAdapter(Context context, ArrayList<Products> products) {
        this.context = context;
        this.productsList = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_item,parent,false);
        ProductViewHolder viewHolder= new ProductViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
       Products product = productsList.get(position);
       holder.itemName.setText(product.getName());
       holder.itemDesc.setText(product.getDesc());
       holder.itemImage.setImageResource(product.getImage());
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
ImageView itemImage;
TextView itemName,itemDesc;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage=itemView.findViewById(R.id.item_image);
            itemName=itemView.findViewById(R.id.itme_name);
            itemDesc=itemView.findViewById(R.id.itme_description);
        }
    }
}
