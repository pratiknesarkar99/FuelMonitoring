package com.example.fuelmonitoring.user.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.MyApp;
import com.example.fuelmonitoring.MainActivity;
import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.user.fragments.wrapperclasses.FuelInputIndicator;
import com.example.fuelmonitoring.user.fragments.wrapperclasses.FuelPriceToday;
import com.example.fuelmonitoring.user.fragments.wrapperclasses.UnameFetcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class user_home extends Fragment {

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private TextView FuelInTV, UnameTV, FuelPriceTV, CityTV;
    private  String fnm, lnm;
    public   static  String cname;
    private  static String fuelamt, fuelprice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        progressDialog = new ProgressDialog(this.getContext());
        firebaseAuth = FirebaseAuth.getInstance();

        UnameTV = (TextView) view.findViewById(R.id.username);
        FuelInTV= (TextView) view.findViewById(R.id.fuelin);
        CityTV = (TextView) view.findViewById(R.id.cityNm);

        progressDialog.setMessage("Fetching your Data! Please wait.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        FuelPriceTV = view.findViewById(R.id.fuelpricetv);

        fetchUserInfo();
        indicateFuelInput();
        fetchFuelPrice();

        return view;
    }

    public void fetchUserInfo(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).child("Profileinfo");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UnameFetcher unameFetcher = dataSnapshot.getValue(UnameFetcher.class);

                    try {
                        fnm = unameFetcher.getFname();
                        lnm = unameFetcher.getLname();
                        UnameTV.setText(fnm + " " + lnm);
                        CityTV.setText(unameFetcher.getCity());
                    } catch (NullPointerException e) {
                        System.err.println(e);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private  void  fetchFuelPrice(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).child("Profileinfo");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UnameFetcher unameFetcher = dataSnapshot.getValue(UnameFetcher.class);
                    cname = unameFetcher.getCity();

                    final String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                    try {
                        switch (cname) {
                            case "Kolhapur": {
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = firebaseDatabase.getReference().child("FuelPrices").child(date);

                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        FuelPriceToday fuelPriceToday = dataSnapshot.getValue(FuelPriceToday.class);
                                        try {
                                            fuelprice = fuelPriceToday.getKolhapur();
                                            FuelPriceTV.setText(fuelPriceToday.getKolhapur());
                                        } catch (NullPointerException e) {
                                            System.err.println(e);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getView().getContext(), "Values not Available!!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            }
                            case "Mumbai": {
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = firebaseDatabase.getReference().child("FuelPrices").child(date);

                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        FuelPriceToday fuelPriceToday = dataSnapshot.getValue(FuelPriceToday.class);
                                        try {
                                            fuelprice = fuelPriceToday.getMumbai();
                                            FuelPriceTV.setText(fuelPriceToday.getMumbai());
                                        } catch (NullPointerException e) {
                                            System.err.println(e);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getView().getContext(), "Values not Available!!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            }
                            case "Pune": {
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = firebaseDatabase.getReference().child("FuelPrices").child(date);

                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        FuelPriceToday fuelPriceToday = dataSnapshot.getValue(FuelPriceToday.class);
                                        try {
                                            fuelprice = fuelPriceToday.getPune();
                                            FuelPriceTV.setText(fuelPriceToday.getPune());
                                        } catch (NullPointerException e) {
                                            System.err.println(e);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getView().getContext(), "Values not Available!!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public  void indicateFuelInput(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).child("FuelIn");;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    FuelInputIndicator fuelInputIndicator = dataSnapshot.getValue(FuelInputIndicator.class);
                    fuelamt = fuelInputIndicator.getValue();
                    FuelInTV.setText(fuelInputIndicator.getValue());

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    } else {
                        showNotification();
                        uploadFuelInData();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private  void uploadFuelInData(){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("UsageDetails").child(firebaseAuth.getUid());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd(HH:mm:ss)");
        String currentDateTime = dateFormat.format(new Date());

        //Toast.makeText(this.getContext(), currentDateTime,Toast.LENGTH_SHORT).show();

        databaseReference.child(currentDateTime).child("amt").setValue(fuelamt);

        double prc = Double.parseDouble(fuelamt)* Double.parseDouble(fuelprice);
        Double price = BigDecimal.valueOf(prc)
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue();
        databaseReference.child(currentDateTime).child("price").setValue(price.toString());

        try {
            Toast.makeText(MyApp.getContext(), "Uploading new fuel input data...",Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            System.err.println(e);
        }
    }

    private  void  showNotification(){
        try{
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApp.getContext(), "MyNotifications")
                    .setContentTitle("Fuel Monitoring System")
                    .setSmallIcon(R.drawable.gas)
                    .setAutoCancel(false)
                    .setPriority(999)
                    .setContentText("Fuel Input Detected: "+ FuelInTV.getText().toString());

            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MyApp.getContext());
            notificationManagerCompat.notify(1, builder.build());
        } catch (NullPointerException e){
            System.err.println(e);
        }
    }
}
