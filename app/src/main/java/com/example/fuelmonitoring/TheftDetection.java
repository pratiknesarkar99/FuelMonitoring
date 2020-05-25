package com.example.fuelmonitoring;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.MyApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TheftDetection extends AppCompatActivity {

    private FirebaseAuth  firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theft_detection);

        firebaseAuth = FirebaseAuth.getInstance();

        theftAlertBox();
    }

    public  void  theftAlertBox(){
        boolean hasTheftOccured;

        hasTheftOccured = getIntent().getBooleanExtra("theftDetected", false);

        if(hasTheftOccured){
            //Toast.makeText(TheftDetection.this, hasTheftOccured+"", Toast.LENGTH_SHORT).show();

            new AlertDialog.Builder(TheftDetection.this)
                    .setCancelable(false)
                    .setIcon(R.drawable.gas)
                    .setTitle("Fuel Theft Alert!!!")
                    .setMessage("A suspicious amount of fuel drop has been detected. Please check if there has been some kind of " +
                            "fuel theft / leak from your vehicle recently. \n\nA report of the same has been stored in your Theft Log section. " +
                            "Refer this section for more details" +
                            "\n\nWe hope this alert was helpful to you. If so please provide your feedback in " +
                            "the feedback section later. Thank you!")
                    .setPositiveButton("OK.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getIntent().putExtra("theftDetected", false);
                            resetOutputAmt();
                            finish();
                        }
                    }).show();
        }
    }

    public void resetOutputAmt(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("FuelOut").child(firebaseAuth.getUid());

        databaseReference.child("value").setValue("0.0");

        try {
            Toast.makeText(MyApp.getContext(), "Fuel Monitoring System: Resetting Output Value...", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            System.err.println(e);
        }
    }
}
