package com.example.fuelmonitoring.user.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.example.fuelmonitoring.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class user_home extends Fragment {

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private TextView FuelInTV, Uname;
    private  String fnm, lnm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        progressDialog = new ProgressDialog(this.getContext());
        firebaseAuth = FirebaseAuth.getInstance();

        Uname = (TextView) view.findViewById(R.id.username);
        FuelInTV= (TextView) view.findViewById(R.id.fuelin);

        progressDialog.setMessage("Fetching your Data! Please wait.");
        progressDialog.show();
        fetchUserInfo();
        indicateFuelInput();
        return view;
    }

    public void fetchUserInfo(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).child("Profileinfo");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UnameFetcher unameFetcher= dataSnapshot.getValue(UnameFetcher.class);

                fnm = unameFetcher.getFname();
                lnm = unameFetcher.getLname();
                Uname.setText(fnm + " " + lnm);
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
             FuelInputIndicator fuelInputIndicator= dataSnapshot.getValue(FuelInputIndicator.class);

                FuelInTV.setText(fuelInputIndicator.getValue());
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                else {
                    showNotification();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private  void  showNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getView().getContext(), "MyNotifications")
                .setContentTitle("Fuel Monitoring System")
                .setSmallIcon(R.drawable.gas)
                .setAutoCancel(false)
                .setPriority(999)
                .setContentText("Fuel Input Detected: "+ FuelInTV.getText().toString());

        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getView().getContext());
        notificationManagerCompat.notify(1, builder.build());
    }
}
