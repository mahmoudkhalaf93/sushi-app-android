package com.example.zalpia.ui.home;

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
import com.example.zalpia.databinding.LayoutOffersBinding;

import java.util.ArrayList;
import java.util.Locale;

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
        LayoutOffersBinding offersBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_offers,
                parent, false);
        return new OffersViewHolder(offersBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersViewHolder holder, int position) {
        OfferModel offerModel = offerModelArrayList.get(position);


        holder.itemView.setOnClickListener(view -> {
            final NavController navController = Navigation.findNavController(holder.itemView);

            HomeFragmentDirections.ActionNavHomeToItemsFragment action =
                    HomeFragmentDirections.actionNavHomeToItemsFragment(offerModel.getNameline1()+" "+
                            offerModel.getNameline2(), offerModel.getFirebaseId(),"offers",offerModel.getNameline1Ar()+" "+offerModel.getNameline2Ar());
            navController.navigate(action);
        });

        if(Locale.getDefault().getDisplayLanguage().equals("العربية"))
        {
            holder.binding.offerNameLine1.setText(offerModel.getNameline1Ar());
            holder.binding.offerNameLine2.setText(offerModel.getNameline2Ar());
            holder.binding.offerTimeCalendarText.setText(offerModel.getTimeAr());
        }
        else {
            holder.binding.offerNameLine1.setText(offerModel.getNameline1());
            holder.binding.offerNameLine2.setText(offerModel.getNameline2());
            holder.binding.offerTimeCalendarText.setText(offerModel.getTime());
        }

       // Picasso.with(context).load(offerModel.getOfferimage()).into(holder.binding.offerImage);
        holder.binding.offerImage.setImageURI(Uri.parse(offerModel.getImage()));

        if (offerModel.getIsTimeCalendar()) {
            holder.binding.offerTimeCalendarImage.setImageResource(R.drawable.ic_baseline_calendar_today_24);
        } else {
            holder.binding.offerTimeCalendarImage.setImageResource(R.drawable.ic_baseline_access_time_24);
        }


    }

    @Override
    public int getItemCount() {
        return offerModelArrayList.size();
    }

    static class OffersViewHolder extends RecyclerView.ViewHolder {
        LayoutOffersBinding binding;

        public OffersViewHolder(@NonNull LayoutOffersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
