package com.example.fuelmonitoring.admin.fragments.viewusers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.user.UserProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MoreUserDetails extends AppCompatActivity {

    private TextView uidTV, fnameTV, lnameTV, mobileTV, cityTV, stateTV, mailTV;
    private  Button goBackBtn;
    //private ProgressDialog progressDialog;
    private  static  String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_user_details);

         uid = getIntent().getStringExtra("uid");
        //Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();

        uidTV = findViewById(R.id.uidTVadmin);
        fnameTV = findViewById(R.id.fnameTVadmin);
        lnameTV = findViewById(R.id.lnameTVadmin);
        mobileTV = findViewById(R.id.mobileTVadmin);
        cityTV = findViewById(R.id.cityTVadmin);
        stateTV = findViewById(R.id.stateTVadmin);
        mailTV = findViewById(R.id.mailTVadmin);

     /*   progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    */

        fetchUserData();

        goBackBtn = findViewById(R.id.goBack);
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public  void fetchUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                        uidTV.setText(uid);
                        //System.out.println(userProfile.getFname());
                        fnameTV.setText(userProfile.getFname());
                        lnameTV.setText(userProfile.getLname());
                        mobileTV.setText(userProfile.getMobile());
                        cityTV.setText(userProfile.getCity());
                        stateTV.setText(userProfile.getState());
                        mailTV.setText(userProfile.getMail());

                       // progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println("Err: ---- " + databaseError);
                }
            });
    }
}
