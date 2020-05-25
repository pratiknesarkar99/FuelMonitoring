package com.example.fuelmonitoring.admin.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.MyApp;
import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.admin.fragments.viewuserfeedback.ViewUserFeedbackAdapter;
import com.example.fuelmonitoring.admin.fragments.viewusers.ViewUsersAdapter;
import com.example.fuelmonitoring.admin.fragments.wrapperclasses.ViewUserFeedback;
import com.example.fuelmonitoring.admin.fragments.wrapperclasses.ViewUsers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class user_feedback extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReferenceRootNode, databaseReference;
    private ArrayList<ViewUserFeedback> list;
    private ViewUserFeedbackAdapter viewUserFeedbackAdapter;

    private String r1, r2, r3, r4, ans1, ans2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_admin_view_user_feedback, container, false);

        recyclerView = view.findViewById(R.id.viewUserFeedbackRecycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();

        databaseReferenceRootNode = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReferenceRootNode.child("Feedback");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    Log.d("Feedback: ", dataSnapshot1.toString());

                    String uid = dataSnapshot1.getKey();

                    String s = dataSnapshot1.child("currentDate").getValue().toString();
                    String d = s.substring(0, 10);
                    String t = s.substring(10, 16);
                    //Log.d("Feedback Date: ", d + " " + t );

                    r1 = dataSnapshot1.child("r1").getValue().toString();
                    r2 = dataSnapshot1.child("r2").getValue().toString();
                    r3 = dataSnapshot1.child("r3").getValue().toString();
                    r4 = dataSnapshot1.child("r4").getValue().toString();

                    ans1 = dataSnapshot1.child("ans1").getValue().toString();
                    ans2 = dataSnapshot1.child("ans2").getValue().toString();

                    ViewUserFeedback viewUserFeedback = dataSnapshot1.getValue(ViewUserFeedback.class);
                    viewUserFeedback.setDate(d);
                    viewUserFeedback.setTime(t);
                    viewUserFeedback.setUid(uid);

                    viewUserFeedback.setR1(r1);
                    viewUserFeedback.setR2(r2);
                    viewUserFeedback.setR3(r3);
                    viewUserFeedback.setR4(r4);

                    viewUserFeedback.setAns1(ans1);
                    viewUserFeedback.setAns2(ans2);

                    viewUserFeedback.setUid(uid);

                    list.add(viewUserFeedback);

                }
                viewUserFeedbackAdapter = new ViewUserFeedbackAdapter(view.getContext(), list);
                recyclerView.setAdapter(viewUserFeedbackAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyApp.getContext(), "outter nest Error!!!",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
