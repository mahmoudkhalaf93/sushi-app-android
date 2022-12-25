package com.example.zalpia.ui.restaurant;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.zalpia.R;
import com.example.zalpia.databinding.FragmentRestaurantBinding;
import com.example.zalpia.room.Branches;
import com.example.zalpia.room.RestaurantsModel;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RestaurantFragment extends Fragment {
    protected static final int REQUEST_CHECK_SETTINGS = 0x2;
    private static final String TAG = "RestaurantFragment1";
    FragmentRestaurantBinding binding;
    RestaurantViewModel viewModel;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FusedLocationProviderClient fusedLocationProviderClient;
    LatLng latLngf;
    String fullAddressEnglish;
    String fullAddressArabic;
    RestaurantsModel restaurantsModel;
    RestaurantAdapter restaurantAdapter;
    LatLng getLastestLocationlatLng = null;
    SupportMapFragment supportMapFragment;
    private Geocoder geocoderEnglish;
    private Geocoder geocoderArbic;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_restaurant, container, false);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());


        askLocationPermission();

        return binding.getRoot();
    }

    void onCreateView() {
        //Toast.makeText(requireActivity(), "onCreateView", Toast.LENGTH_SHORT).show();
        binding.shareLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        binding.shareLocation.setImageResource(R.drawable.ic_outline_share_24);
        viewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
        viewModel.setMutableRestaurant();
        Locale lHebrew = new Locale("ar_EG");
        geocoderArbic = new Geocoder(requireActivity(), lHebrew);

        Locale english = new Locale("en_US");
        geocoderEnglish = new Geocoder(requireActivity(), english);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        binding.getDirection.setText(R.string.get_direction);

        binding.getDirection.setOnClickListener(view -> {
            if (binding.addressCardviewVisibil.getVisibility() == View.GONE) {
                getLatLngGeocoderAddressArabicAndSetLayout(latLngf);
                getLatLngGeocoderAddressEnglishAndSetLayout(latLngf);
                binding.backgroundwhiteRestaurantVisibil.setVisibility(View.VISIBLE);
                binding.addressCardviewVisibil.setVisibility(View.VISIBLE);
                Animation anim3 = AnimationUtils.loadAnimation(getActivity(), R.anim.dirction_from_hide_to_show);
                binding.backgroundwhiteRestaurantVisibil.setAnimation(anim3);
                binding.addressCardviewVisibil.setAnimation(anim3);
            } else {
                Animation anim3 = AnimationUtils.loadAnimation(getActivity(), R.anim.dirction_from_show_to_hide);
                binding.backgroundwhiteRestaurantVisibil.setAnimation(anim3);
                binding.addressCardviewVisibil.setAnimation(anim3);
                binding.backgroundwhiteRestaurantVisibil.setVisibility(View.GONE);
                binding.addressCardviewVisibil.setVisibility(View.GONE);

            }
        });
        binding.onListCLick.setOnClickListener(view -> {
            if (binding.cardviewListLocation.getVisibility() == View.VISIBLE) {
                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.splash_anim_frombig_small);
                binding.cardviewListLocation.startAnimation(anim);
                binding.cardviewListLocation.setVisibility(View.GONE);
            } else {
                binding.cardviewListLocation.setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.splash_anim_fromsmall_to_big);
                binding.cardviewListLocation.startAnimation(anim);
            }
        });
        //SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        Objects.requireNonNull(supportMapFragment).getMapAsync(googleMap -> {
            //  latLngf = new LatLng(30.052479376812034, 31.202767364001556);
            // googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngf, 14));
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
                    latLngf = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                    getLatLngGeocoderAddressArabicAndSetLayout(latLngf);
                    getLatLngGeocoderAddressEnglishAndSetLayout(latLngf);
                    return false;
                }
            });
            googleMap.clear();
            googleMap.setOnMapClickListener(null);
            viewModel.getMutableRestaurant().observe(getViewLifecycleOwner(), restaurantsModel -> {
                RestaurantFragment.this.restaurantsModel = restaurantsModel;
                RestaurantI restaurantOnClickAdapter = position -> {
                    Branches branche = restaurantsModel.getBranches().get(position);
                    LatLng latLngBranchLoc = new LatLng(branche.getLocation().getLatitude(), branche.getLocation().getLongitude());
                    binding.cardviewListLocation.setVisibility(View.GONE);
//                    if (getLastestLocationlatLng == null)
//                    { getLastestLocationlatLng = new LatLng(30.052970827285186, 31.202726131160556);}
                    goToBranchOnMap(googleMap, getLastestLocationlatLng, latLngBranchLoc);
                };
                //   if (getLastestLocationlatLng == null)
                // getLastestLocationlatLng = new LatLng(30.052970827285186, 31.202726131160556);
                restaurantAdapter = new RestaurantAdapter(restaurantsModel.getBranches(), getLastestLocationlatLng, restaurantOnClickAdapter);
                binding.locationRv.setAdapter(restaurantAdapter);
                Branches nearestBranch = getNearestBranch(getLastestLocationlatLng);
                LatLng nearLatLngBranchLoc1 = new LatLng(nearestBranch.getLocation().getLatitude(), nearestBranch.getLocation().getLongitude());
                goToBranchOnMap(googleMap, getLastestLocationlatLng, nearLatLngBranchLoc1);

//                    googleMap.setOnMapClickListener(latLng -> {
//                        googleMap.clear();
//                        setAllBranchesOnMap(googleMap,restaurantsModel.getBranches());
//                    });
                setAllBranchesOnMap(googleMap, restaurantsModel.getBranches(), getLastestLocationlatLng);
            });
        });

    }

    void getUserLocationFromMap() {
        //   Toast.makeText(requireActivity(), "getUserLocationFromMap", Toast.LENGTH_SHORT).show();
        binding.shareLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askLocationPermission();
            }
        });
        binding.shareLocation.setImageResource(R.drawable.ic_baseline_gps_fixed_24);
        //SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        Objects.requireNonNull(supportMapFragment).getMapAsync(googleMap -> {
            googleMap.clear();
            latLngf = new LatLng(30.052479376812034, 31.202767364001556);
            googleMap.addMarker(new MarkerOptions().position(latLngf).title(getString(R.string.my_location))
                    .icon(BitmapFromVector(requireActivity(), R.drawable.userpng)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngf, 14));
            binding.getDirection.setText(R.string.save_this_location);
            binding.getDirection.setOnClickListener(view -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle(R.string.save_location)
                        .setMessage(R.string.do_you_want_to_save)
                        .setPositiveButton("yes", (dialogInterface, i) -> {
                            getLastestLocationlatLng = latLngf;
                            onCreateView();
                        })
                        .setNegativeButton(R.string.no, null).create().show();
            });
            googleMap.setOnMapClickListener(latLng -> {
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.my_location))
                        .icon(BitmapFromVector(requireActivity(), R.drawable.userpng)));
                // googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                latLngf = latLng;

            });
        });
    }

    private void askLocationPermission() {
//هل ليا صلاحية استخدم اللوكشن والا لا
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            displayLocationSettingsRequest(requireActivity());
            // getLastestLocation();

        } else {
            //ask for location permisison
            // ActivityCompat.requestpermissions
            //  ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            mPermissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @SuppressLint("WrongConstant")
    private void displayLocationSettingsRequest(Context context) {
        final LocationManager manager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1);
            locationRequest.setFastestInterval(1);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(result1 -> {
                final Status status = result1.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        getLastestLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            // status.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS);
                            // startIntentSenderForResult(status.getResolution().getIntentSender(), REQUEST_CHECK_SETTINGS, null, 0, 0, 0, null);

//                            private ActivityResultLauncher<IntentSenderRequest> mPermissionOpenGps = registerForActivityResult(
//                                    new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
//                                        @Override
//                                        public void onActivityResult(ActivityResult result) {
//                                            if (result.getResultCode() == RESULT_OK) {
//                                                //Toast.makeText(requireActivity(), "XXXXXXXXXXXXXXXXonActivityResult", Toast.LENGTH_SHORT).show();
//                                                binding.mainlayoutRest.setEnabled(false);
//                                                new Handler().postDelayed(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        getLastestLocation();
//                                                    }
//                                                }, 3300);
//                                            } else {
//                                                getUserLocationFromMap();
//                                                //Toast.makeText(requireActivity(), "onActivityResult", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    });


                            IntentSenderRequest.Builder intentSenderBuilder1 = new IntentSenderRequest.Builder(
                                    status.getResolution().getIntentSender());
                            intentSenderBuilder1.setFillInIntent(null);
                            IntentSenderRequest mPermissionOpenGpsIntentSender = intentSenderBuilder1.build();
                            mPermissionOpenGps.launch(mPermissionOpenGpsIntentSender);
                            //.setFlags(0,0);
                            //  IntentSenderRequest newsad=new IntentSenderRequest(status.getResolution().getIntentSender().getCreatorUserHandle());
                            //  mPermissionResult2.launch( );
                        } catch (Exception e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            });
        } else {
            getLastestLocation();
        }
    }    private ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        Log.e(TAG, "onActivityResult: PERMISSION GRANTED");
                        displayLocationSettingsRequest(requireActivity());
                    } else {
                        Log.e(TAG, "onActivityResult: PERMISSION DENIED");
                        getUserLocationFromMap();
                    }
                }
            });

    private void getLastestLocation() {
        try {
            binding.mainlayoutRest.setEnabled(true);

            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location == null) {
                        getUserLocationFromMap();
                    } else {
                        getLastestLocationlatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        onCreateView();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    getUserLocationFromMap();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "getLastestLocation: ", e);
        }

    }

    void setAllBranchesOnMap(GoogleMap googleMap, ArrayList<Branches> listBranches, LatLng userLatLng) {
        googleMap.addMarker(new MarkerOptions().position(userLatLng).title(getString(R.string.my_location))
                .icon(BitmapFromVector(requireActivity(), R.drawable.userpng)));
        for (Branches branche : listBranches) {
            LatLng branchLatlng = new LatLng(branche.getLocation().getLatitude(), branche.getLocation().getLongitude());
            googleMap.addMarker(new MarkerOptions().position(branchLatlng).title(branche.getName())
                    .snippet(branche.getStatus())
                    .icon(BitmapFromVector(requireActivity(), R.mipmap.ic_launcher_round)));

        }

    }
    private ActivityResultLauncher<IntentSenderRequest> mPermissionOpenGps = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        //Toast.makeText(requireActivity(), "XXXXXXXXXXXXXXXXonActivityResult", Toast.LENGTH_SHORT).show();
                        binding.mainlayoutRest.setEnabled(false);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getLastestLocation();
                            }
                        }, 3300);
                    } else {
                        getUserLocationFromMap();
                        //Toast.makeText(requireActivity(), "onActivityResult", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    Branches getNearestBranch(LatLng userLocation) {
        Branches nearestBranch = restaurantsModel.getBranches().get(0);
        for (int i = 1; i < restaurantsModel.getBranches().size(); i++) {
            LatLng nearestLatLng = new LatLng(nearestBranch.getLocation().getLatitude(),
                    nearestBranch.getLocation().getLongitude());
            LatLng nextLatLng = new LatLng(restaurantsModel.getBranches().get(i).getLocation().getLatitude()
                    , restaurantsModel.getBranches().get(i).getLocation().getLongitude());
            if (CalculationByDistance(userLocation, nextLatLng) < CalculationByDistance(userLocation, nearestLatLng)) {
                nearestBranch = restaurantsModel.getBranches().get(i);
            }
        }
        return nearestBranch;
    }

    private void goToBranchOnMap(GoogleMap googleMap, LatLng userLatLng, LatLng branchLatlng) {

        binding.shareLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "geo:" + branchLatlng.latitude + ","
                        + branchLatlng.longitude + "?q=" + branchLatlng.latitude
                        + "," + branchLatlng.longitude;
                startActivity(new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(uri)));
            }
        });
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 16), 1000,
                new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
//            int strokcolor = R.color.light_orange_gradient_hard;
//            int shadecolor = 0x44ff0000;
                        // googleMap.addCircle(new CircleOptions().center(branchLatlng)
//                    .radius(20).strokeColor(strokcolor).fillColor(shadecolor));
                        latLngf = branchLatlng;
                        getLatLngGeocoderAddressArabicAndSetLayout(latLngf);
                        getLatLngGeocoderAddressEnglishAndSetLayout(latLngf);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(branchLatlng, 16), 2000, null);
                    }

                    @Override
                    public void onCancel() {

                    }
                });

    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        int height = 80;
        int width = 80;
        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, width, height);

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
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


        return Radius * c;
    }

    public void getLatLngGeocoderAddressEnglishAndSetLayout(LatLng latLng) {
        try {
            List<Address> addressesList = geocoderEnglish.getFromLocation(latLng.latitude, latLng.longitude, 1);
            //    for (Address address : addressesList) {
            fullAddressEnglish = addressesList.get(0).getAddressLine(0);
            binding.addressInEnglish.setText(fullAddressEnglish);
            Log.i(TAG, "getLatLngGeocoder: " + fullAddressEnglish);
            //  }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "getLatLngGeocoder: " + e.getLocalizedMessage());
        }
    }

    public void getLatLngGeocoderAddressArabicAndSetLayout(LatLng latLng) {
        try {
            List<Address> addressesList = geocoderArbic.getFromLocation(latLng.latitude, latLng.longitude, 1);
            //  for (Address address : addressesList) {
            fullAddressArabic = addressesList.get(0).getAddressLine(0);
            binding.addressInArabic.setText(fullAddressArabic);
            Log.i(TAG, "getLatLngGeocoder: " + fullAddressArabic);
            // }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "getLatLngGeocoder: " + e.getLocalizedMessage());
        }
    }




}