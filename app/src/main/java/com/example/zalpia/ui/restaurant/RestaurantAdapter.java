package com.example.zalpia.ui.restaurant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zalpia.R;
import com.example.zalpia.databinding.LayoutItemLocationListBinding;
import com.example.zalpia.room.Branches;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {
    ArrayList<Branches> listBranches;
    LatLng userLocation;
    private static final String TAG = "RestaurantAdapter1";
    RestaurantI restaurantOnClickAdapter;
    int nearestBranchPosition ;
    public RestaurantAdapter(ArrayList<Branches> listBranches, LatLng userLocation,RestaurantI restaurantOnClickAdapter) {
        this.listBranches = listBranches;
        this.userLocation = userLocation;
        this.restaurantOnClickAdapter=restaurantOnClickAdapter;
        this.nearestBranchPosition = getNearestBranchs(userLocation);

    }


    @NotNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutItemLocationListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_item_location_list, parent, false);

        return new RestaurantViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull  RestaurantViewHolder holder, int position) {
        Branches brancheItem = listBranches.get(position);
       // Log.i(TAG, "onBindViewHolder: "+brancheItem.getNameAr());
       Context context = holder.itemView.getContext();
        if(Locale.getDefault().getDisplayLanguage().equals("العربية"))
            holder.binding.branchName.setText(brancheItem.getNameAr());
       else
        holder.binding.branchName.setText(brancheItem.getName());
       
        if (brancheItem.getStatus().equals("open")) {
            holder.binding.branchStatus.setTextColor(holder.itemView.getResources().getColor(R.color.green_open));
            if(position==nearestBranchPosition)
                holder.binding.branchStatus.setText(context.getString(R.string.open)+ context.getString(R.string.nearest) );
            else
                holder.binding.branchStatus.setText(context.getString(R.string.open));
        }
        else {
            holder.binding.branchStatus.setTextColor(holder.itemView.getResources().getColor(R.color.status_red));
            if(position==nearestBranchPosition)
                holder.binding.branchStatus.setText(context.getString(R.string.closed)+ holder.itemView.getContext().getString(R.string.nearest) );
            else
                holder.binding.branchStatus.setText(context.getString(R.string.closed));
        }


        LatLng branchLatLng =new LatLng(brancheItem.getLocation().getLatitude(),brancheItem.getLocation().getLongitude());
        holder.binding.branchDistance.setText(context.getString(R.string.away_by)+    new DecimalFormat("####.##").format(CalculationByDistance(branchLatLng, userLocation))   +context.getString(R.string.Km) );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restaurantOnClickAdapter.goToBranch(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBranches.size();
    }

    static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        LayoutItemLocationListBinding binding;

        public RestaurantViewHolder(@NonNull  LayoutItemLocationListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

//    int getNearestBranch(LatLng userLocation) {
//        int nearestBranchPosition = 0;
//        for (int i = 0; i < listBranches.size()-1; i++) {
//            LatLng currnetLatLng = new LatLng(listBranches.get(i).getLocation().getLatitude()
//                    , listBranches.get(i).getLocation().getLongitude());
//            LatLng nexttLatLng = new LatLng(listBranches.get(i++).getLocation().getLatitude()
//                    , listBranches.get(i++).getLocation().getLongitude());
//
//            if (CalculationByDistance(userLocation, currnetLatLng) < CalculationByDistance(userLocation, nexttLatLng))
//                nearestBranchPosition = i;
//
//        }
//        return nearestBranchPosition;
//    }
    int getNearestBranchs(LatLng userLocation) {
        int nearestBranch = 0;
        for (int i = 1; i < listBranches.size(); i++) {
            LatLng nearestLatLng = new LatLng(listBranches.get(nearestBranch).getLocation().getLatitude(),
                    listBranches.get(nearestBranch).getLocation().getLongitude());
            LatLng nextLatLng = new LatLng(listBranches.get(i).getLocation().getLatitude()
                    , listBranches.get(i).getLocation().getLongitude());
            if(CalculationByDistance(userLocation,nextLatLng)<CalculationByDistance(userLocation,nearestLatLng)){
                nearestBranch =i;
            }
        }
        return nearestBranch;
    }
    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371; // radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
//        DecimalFormat newFormat = new DecimalFormat("####.##");
//        newFormat.format(Radius * c)
//   new DecimalFormat("####.##").format(Radius * c)
        return  truncateTo(Radius * c, 2);
    }
    static double truncateTo( double unroundedNumber, int decimalPlaces ){
        int truncatedNumberInt = (int)( unroundedNumber * Math.pow( 10, decimalPlaces ) );
        double truncatedNumber = (double)( truncatedNumberInt / Math.pow( 10, decimalPlaces ) );
        return truncatedNumber;
    }
}
