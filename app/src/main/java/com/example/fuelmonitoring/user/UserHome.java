package com.example.fuelmonitoring.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.fuelmonitoring.MainActivity;
import com.example.fuelmonitoring.R;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.example.fuelmonitoring.user.fragments.feedback.feedback;
import com.example.fuelmonitoring.user.fragments.fuel_level;
import com.example.fuelmonitoring.user.fragments.monthly_usage;
import com.example.fuelmonitoring.user.fragments.nearby_stations;
import com.example.fuelmonitoring.user.fragments.user_home;
import com.example.fuelmonitoring.user.fragments.daily_usage;
import com.example.fuelmonitoring.user.fragments.weekly_usage;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

public class UserHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new user_home()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(UserHome.this, "Opening settings...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserHome.this, DeleteProfile.class));
            return true;
        }

        if (id == R.id.edit_profile) {
            Toast.makeText(UserHome.this, "Opening update form...", Toast.LENGTH_SHORT).show();
           startActivity(new Intent(UserHome.this, EditProfile.class));
            return true;
        }

        if(id == R.id.logout){
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            Toast.makeText(UserHome.this, "*Logged Out*", Toast.LENGTH_SHORT).show();
                            firebaseAuth.signOut();
                            finish();
                            startActivity(new Intent(UserHome.this, MainActivity.class));
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new user_home()).commit();
        } else if (id == R.id.fuel_level) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fuel_level()).commit();
        } else if (id == R.id.nearby_stations) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new nearby_stations()).commit();
        } else if (id == R.id.daily_usage) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new daily_usage()).commit();
        } else if (id == R.id.weekly_usage) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new weekly_usage()).commit();
        } else if (id == R.id.monthly_usage) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new monthly_usage()).commit();
        } else if (id == R.id.add_vehicle) {
           // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fuel_level()).commit();
            Toast.makeText(UserHome.this, "This module is Coming Soon!!!", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_feedback) {
            Toast.makeText(this, "Opening Feedback Form", Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new feedback()).commit();
        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "Sharing", Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
