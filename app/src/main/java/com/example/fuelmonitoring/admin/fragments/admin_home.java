package com.example.fuelmonitoring.admin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.user.fragments.feedback.feedback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class admin_home extends Fragment {

    private  static String usrCount;

    private TextView usrCountTV;
    private Button viewSysUsersBtn, viewFeedbackBtn, setFuelPriceBtn, viewConactEmailsbtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        usrCount = "00";
        usrCountTV = view.findViewById(R.id.userCountTV);
        viewSysUsersBtn = view.findViewById(R.id.viewSysUsersBtn);
        viewFeedbackBtn = view.findViewById(R.id.viewFeedbackBtn);
        setFuelPriceBtn = view.findViewById(R.id.updateTodaysPriceBtn);
        viewConactEmailsbtn = view.findViewById(R.id.viewContachEmailsBtn);

        viewSysUsersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Opening System Users list...", Toast.LENGTH_SHORT).show();

                Fragment fragment = null;
                fragment = new view_users();
                replaceFragment(fragment);

            }
        });

        viewFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Opening System Users Feedback list...", Toast.LENGTH_SHORT).show();

                Fragment fragment = null;
                fragment = new user_feedback();
                replaceFragment(fragment);

            }
        });

        setFuelPriceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Opening Update Today's Fuel Price form...", Toast.LENGTH_SHORT).show();

                Fragment fragment = null;
                fragment = new fuel_price_today();
                replaceFragment(fragment);

            }
        });

        viewConactEmailsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Opening list of emails to contact to...", Toast.LENGTH_SHORT).show();

                Fragment fragment = null;
                fragment = new contact_emails();
                replaceFragment(fragment);

            }
        });
        fetchUserCount();

        return view;
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public  void  fetchUserCount(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    try {
                        long cnt = dataSnapshot.getChildrenCount();
                        usrCountTV.setText("0" + cnt +"");
                    } catch (Exception e){}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
