package com.example.zalpia.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zalpia.R;
import com.example.zalpia.databinding.LayoutOffersBinding;

import java.util.ArrayList;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OffersViewHolder> {
    Context context;
ArrayList<OfferModel> offerModelArrayList;

    public OffersAdapter(Context context, ArrayList<OfferModel> offerModelArrayList) {
        this.context = context;
        this.offerModelArrayList = offerModelArrayList;
    }

    @NonNull
    @Override
    public OffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutOffersBinding offersBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_offers,
                parent,false);
        return new OffersViewHolder(offersBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersViewHolder holder, int position) {
        OfferModel offerModel=offerModelArrayList.get(position);

        holder.binding.offerImage.setImageResource(offerModel.getOfferimage());
        holder.binding.offerNameLine1.setText(offerModel.getOfferNameLine1());
        holder.binding.offerNameLine2.setText(offerModel.getOfferNameLine2());
        if(offerModel.getIsImageCalendar()){
            holder.binding.offerTimeCalendarImage.setImageResource(R.drawable.ic_baseline_calendar_today_24);
        }
        else{
            holder.binding.offerTimeCalendarImage.setImageResource(R.drawable.ic_baseline_access_time_24);
        }
        holder.binding.offerTimeCalendarText.setText(offerModel.getTimeText());

    }

    @Override
    public int getItemCount() {
        return offerModelArrayList.size();
    }

    class OffersViewHolder extends RecyclerView.ViewHolder{
LayoutOffersBinding binding;
        public OffersViewHolder(@NonNull LayoutOffersBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
