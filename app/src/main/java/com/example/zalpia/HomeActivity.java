package com.example.zalpia;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    //protected static final int REQUEST_CHECK_SETTINGS = 0x2;
    private static final String TAG = "HomeActivity1";
   // public static GeoPoint userLocation;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ConnectivityManager connectivityManager;
    AlertDialog.Builder builder;
    AlertDialog alert;
    protected static final int REQUEST_CHECK_SETTINGS = 0x2;
    boolean isAppOpenNow = true;
    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_home);
        drawerLayout = findViewById(R.id.drawer_layout_s);
        navigationView = findViewById(R.id.nav_view);

        builder = new AlertDialog.Builder(HomeActivity.this);
        alert = builder.setTitle( R.string.connection_error)
                .setMessage(R.string.pls_conncet_internet)
                .setIcon(R.drawable.ic_baseline_wifi_off_24)
                .setCancelable(false).create();

        if (!thereisInternet())
            alert.show();

        connectivityManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    if (isAppOpenNow) {
                        //Toast.makeText(HomeActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                alert.dismiss();
                            }
                        });
                    }
                }

                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                    if (isAppOpenNow) {
                        // Toast.makeText(HomeActivity.this, "not con", Toast.LENGTH_SHORT).show();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                alert.show();
                            }
                        });

                    }
                }
            });
        }

        SetNavHeaderData(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_order, R.id.nav_menu, R.id.nav_restaurant, R.id.nav_favorites, R.id.nav_mycart,
                R.id.nav_myaccount, R.id.nav_aboutus, R.id.nav_contactus, R.id.nav_terms, R.id.nav_aboutus, R.id.nav_contactus)
                .setOpenableLayout(drawerLayout)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

//        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//            if (destination.getId() == R.id.nav_restaurant) {
//
//
//            }
//        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.nav_logout) {

                    GoogleSignInClient mGoogleSignInClient;
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();
                    mGoogleSignInClient = GoogleSignIn.getClient(HomeActivity.this, gso);
                    mGoogleSignInClient.revokeAccess();
                    LoginManager.getInstance().logOut();
                    FirebaseAuth.getInstance().signOut();

                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.reg_anim_fromhideleft_to_show,
                            R.anim.reg_anim_fromshow_to_hidetorightside);
                    finish();
                } else {
                    NavigationUI.onNavDestinationSelected(item, navController);
                    drawerLayout.closeDrawers();
                }
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        isAppOpenNow = false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        isAppOpenNow = true;

    }

    boolean thereisInternet() {
        //  boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else
            return false;
    }

    public void SetNavHeaderData(Context context) {
        View headerView = navigationView.getHeaderView(0);
        ImageView userImage = headerView.findViewById(R.id.my_image_view);
        TextView userName = headerView.findViewById(R.id.user_name);
        TextView userEmail = headerView.findViewById(R.id.user_email);
        firestore.collection("users").document(FirebaseAuth.getInstance().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            UserModell user = task.getResult().toObject(UserModell.class);

                            userName.setText(user.getName());
                            userEmail.setText(user.getEmail());
                            // Picasso.with(context).load(user.getImage()).into(userImage);
                            Picasso.get().load(user.getImage()).into(userImage);

                        } else {
                            Toast.makeText(context, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "onComplete: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

}