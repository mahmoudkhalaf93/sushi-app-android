package com.example.zalpia;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_order, R.id.nav_menu, R.id.nav_restaurant, R.id.nav_favorites, R.id.nav_mycart,
                R.id.nav_myaccount, R.id.nav_aboutus, R.id.nav_contactus, R.id.nav_terms)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this , R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }





//        else if (id == R.id.nav_gallery) {
//
//            EventFragment eventFragment = new EventFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("selectedEvent", "Cosmos");
//            eventFragment.setArguments(bundle);
//            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,eventFragment).commit();
//
//        } else if (id == R.id.nav_slideshow) {
//
//            EventFragment eventFragment = new EventFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("selectedEvent", "Sink");
//            eventFragment.setArguments(bundle);
//            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,eventFragment).commit();
//
//        } else if (id == R.id.nav_share) {
//            ShopListFragment intent = new ShopListFragment();
//            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, intent).commit();
//
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            drawer.closeDrawer(START);
//            return true;
//        }

}