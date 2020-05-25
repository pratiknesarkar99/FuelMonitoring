package com.example.fuelmonitoring.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.fuelmonitoring.MainActivity;
import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.admin.fragments.admin_home;
import com.example.fuelmonitoring.admin.fragments.contact_emails;
import com.example.fuelmonitoring.admin.fragments.fuel_price_today;
import com.example.fuelmonitoring.admin.fragments.user_feedback;
import com.example.fuelmonitoring.admin.fragments.view_users;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

public class AdminHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new admin_home()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
        }

        if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
        }
        else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.logout){
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            Toast.makeText(AdminHome.this, "*Logged Out*", Toast.LENGTH_SHORT).show();
                            firebaseAuth.signOut();
                            finish();
                            startActivity(new Intent(AdminHome.this, MainActivity.class));
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
            // Handle the camera action
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new admin_home()).commit();
        } else if (id == R.id.view_users) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new view_users()).commit();
        } else if (id == R.id.fuel_price_today) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fuel_price_today()).commit();
        } else if (id == R.id.user_feedback) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new user_feedback()).commit();
        } else if (id == R.id.contact_emails) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new contact_emails()).commit();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
