package com.example.fuelmonitoring.admin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.admin.fragments.wrapperclasses.FuelPrices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class fuel_price_today extends Fragment {

    private Button uploadBtn;
    private TextView todaysDate;
    private ImageButton refreshBtn;
    private EditText kop, mum, pun;
    private  static  String date;

    private FirebaseAuth firebaseAuth;
    private  String kopprice, mumprice, punprice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fuel_prices_today, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        uploadBtn = view.findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUploadBtnClick();
            }
        });

        refreshBtn = view.findViewById(R.id.dateRefreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateRefreshBtnClicked();
            }
        });

        todaysDate = view.findViewById(R.id.todaysDt);

        kop = view.findViewById(R.id.priceKop);
        mum = view.findViewById(R.id.priceMum);
        pun = view.findViewById(R.id.pricePune);

        return view;
    }

    public  void dateRefreshBtnClicked(){
        date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        todaysDate.setText(date);
    }

    public void onUploadBtnClick(){
        if(!todaysDate.getText().toString().equals("---") ){
            if(kop.getText().toString().isEmpty() || mum.getText().toString().isEmpty() || pun.getText().toString().isEmpty()) {
                if(kop.getText().toString().isEmpty()){
                    kop.requestFocus();
                } else if(mum.getText().toString().isEmpty()){
                    mum.requestFocus();
                } else {
                    pun.requestFocus();
                }
                Toast.makeText(getContext(), "Please add all the prices to continue...", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference().child("FuelPrices");

                FuelPrices fuelPrices = new FuelPrices(kop.getText().toString(), mum.getText().toString(), pun.getText().toString());
                databaseReference.child(todaysDate.getText().toString()).setValue(fuelPrices);

                Toast.makeText(getContext(), "Uploading Prices for Today...", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Wait for date to appear...", Toast.LENGTH_SHORT).show();
        }
    }
}
