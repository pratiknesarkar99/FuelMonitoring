package com.example.fuelmonitoring.user.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.user.fragments.dailyusage.DailyUsageAdapter;
import com.example.fuelmonitoring.user.fragments.wrapperclasses.DailyUsageDataFetcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class daily_usage extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ArrayList<DailyUsageDataFetcher> list;
    private DailyUsageAdapter dailyUsageAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_daily_usage, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.myRecycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("UsageDetails").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    String s = dataSnapshot1.getKey();
                    String d = s.substring(0,10);
                    String t = s.substring(11, 16);

                    DailyUsageDataFetcher dailyUsageDataFetcher = dataSnapshot1.getValue(DailyUsageDataFetcher.class);
                    dailyUsageDataFetcher.setDate(d);
                    dailyUsageDataFetcher.setTime(t);

                    list.add(dailyUsageDataFetcher);
                }
                dailyUsageAdapter = new DailyUsageAdapter(view.getContext(), list);
                recyclerView.setAdapter(dailyUsageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getView().getContext(), "Error!!!",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
