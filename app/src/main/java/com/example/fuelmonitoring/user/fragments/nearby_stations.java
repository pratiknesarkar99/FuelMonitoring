package com.example.fuelmonitoring.user.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.user.fragments.mapdirectory.getNearbyStations;

public class nearby_stations extends Fragment{

    Button getMap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby_stations, container, false);

        getMap = (Button) view.findViewById(R.id.getLocBtn);

        getMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnclick();
            }
        });
        return view;
    }

    public void btnclick(){
        startActivity(new Intent(this.getContext(), getNearbyStations.class));
        Toast.makeText(this.getContext(), "Loading Map", Toast.LENGTH_SHORT).show();
    }
}
