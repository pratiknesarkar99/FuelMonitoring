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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.MyApp;
import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.user.fragments.dailyusage.DailyUsageAdapter;
import com.example.fuelmonitoring.user.fragments.dailyusage.dailyGraphUsage;
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
    private Button showGraphBtn;

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
                    String d = s.substring(0, 8);
                    String t = s.substring(8, 14);

                    DailyUsageDataFetcher dailyUsageDataFetcher = dataSnapshot1.getValue(DailyUsageDataFetcher.class);
                    dailyUsageDataFetcher.setDate(d.substring(0, 4)+"/"+d.substring(4, 6)+"/"+d.substring(6, 8));
                    dailyUsageDataFetcher.setTime(t.substring(0, 2)+":"+d.substring(2, 4)+":"+d.substring(4, 6));

                    list.add(dailyUsageDataFetcher);
                }
                dailyUsageAdapter = new DailyUsageAdapter(view.getContext(), list);
                recyclerView.setAdapter(dailyUsageAdapter);

                showGraphBtn = view.findViewById(R.id.openGraphicalView);
                showGraphBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MyApp.getContext(), "Launching Graphical view...",
                                Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getContext(), dailyGraphUsage.class));
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getView().getContext(), "Error!!!",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
