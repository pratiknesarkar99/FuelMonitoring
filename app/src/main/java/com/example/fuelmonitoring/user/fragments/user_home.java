package com.example.fuelmonitoring.user.fragments;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.MyApp;
import com.example.fuelmonitoring.MainActivity;
import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.TheftDetection;
import com.example.fuelmonitoring.user.EditProfile;
import com.example.fuelmonitoring.user.fragments.feedback.feedback;
import com.example.fuelmonitoring.user.fragments.wrapperclasses.FuelInputIndicator;
import com.example.fuelmonitoring.user.fragments.wrapperclasses.FuelOutputIndicator;
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
    private  static String fuelamt, fuelprice, fuelOutputAmt;
    private Button dailyUsageBtn, feedbackFormBtn, editProfileBtn;

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

        feedbackFormBtn = view.findViewById(R.id.feedbackFormBtn);
        feedbackFormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Opening Feedback form...", Toast.LENGTH_SHORT).show();

                Fragment fragment = null;
                fragment = new feedback();
                replaceFragment(fragment);
            }
        });

        dailyUsageBtn = view.findViewById(R.id.dailyUsageBtn);
        dailyUsageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Launching Daily Usage panel...", Toast.LENGTH_SHORT).show();

                Fragment fragment = null;
                fragment = new daily_usage();
                replaceFragment(fragment);
            }
        });

        editProfileBtn = view.findViewById(R.id.editProfileBtn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Opening your Profile...", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(view.getContext(), EditProfile.class));
            }
        });

        fetchUserInfo();
        indicateFuelInput();
        fetchFuelPrice();

        detectFuelOutput();

        return view;
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void fetchUserInfo(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());
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
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());
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
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("FuelIn").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    FuelInputIndicator fuelInputIndicator = dataSnapshot.getValue(FuelInputIndicator.class);
                    fuelamt = fuelInputIndicator.getValue() + "";
                    FuelInTV.setText(fuelInputIndicator.getValue() + "");

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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentDateTime = dateFormat.format(new Date());

        //Toast.makeText(this.getContext(), currentDateTime,Toast.LENGTH_SHORT).show();

        //If error occurs here, please check if you have the value for todays petrol price on the User Home Screen
        double x = Double.parseDouble(fuelamt.trim());
        double prc = x * Double.parseDouble(fuelprice);
        Double price = BigDecimal.valueOf(prc)
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue();

        databaseReference.child(currentDateTime).child("amt").setValue(fuelamt);
        databaseReference.child(currentDateTime).child("price").setValue(price.toString());

        try {
            Toast.makeText(MyApp.getContext(), "Fuel Monitoring System: Uploading new fuel input data...", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            System.err.println(e);
        }
    }

    private  void  showNotification(){
        try{
            Intent intent = new Intent(MyApp.getContext(), MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(MyApp.getContext(), 100,
                    intent, PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApp.getContext(), "MyNotifications")
                    .setContentTitle("Fuel Monitoring System")
                    .setSmallIcon(R.drawable.gas)
                    .setAutoCancel(false)
                    .setPriority(999)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setContentText("Fuel Input Detected: "+ FuelInTV.getText().toString());

            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MyApp.getContext());
            notificationManagerCompat.notify(1, builder.build());
        } catch (NullPointerException e){
            System.err.println(e);
        }
    }

    public  void detectFuelOutput(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference().child("FuelOut").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    FuelOutputIndicator fuelOutputIndicator = dataSnapshot.getValue(FuelOutputIndicator.class);
                    fuelOutputAmt = fuelOutputIndicator.getValue() + "";

                    if (Double.parseDouble(fuelOutputAmt )> 0.2) {
                        pushLog();
                        showTheftNotification();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private  void  showTheftNotification(){
        try{
            Intent intent = new Intent(MyApp.getContext(), TheftDetection.class);
            intent.putExtra("theftDetected",  true);

            PendingIntent pendingIntent = PendingIntent.getActivity(MyApp.getContext(), 100,
                    intent, PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApp.getContext(), "MyNotifications")
                    .setContentTitle("Fuel Monitoring System")
                    .setSmallIcon(R.drawable.gas)
                    .setAutoCancel(false)
                    .setPriority(999)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setContentText("Fuel Theft/Leak Detected!!!  Fuel amount dropping too fast suddenly.");

            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MyApp.getContext());
            notificationManagerCompat.notify(1, builder.build());
        } catch (NullPointerException e){
            System.err.println(e);
        }
    }

    public void pushLog(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("TheftLog").child(firebaseAuth.getUid());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        //Toast.makeText(this.getContext(), currentDateTime,Toast.LENGTH_SHORT).show();

        databaseReference.child(currentDateTime).child("amt").setValue(fuelamt);

        try {
            Toast.makeText(MyApp.getContext(), "Fuel Monitoring System: Pushing Theft Logs...", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            System.err.println(e);
        }
    }
}
