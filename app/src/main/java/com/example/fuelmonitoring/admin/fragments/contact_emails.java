package com.example.fuelmonitoring.admin.fragments;

import android.os.Bundle;
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
import com.example.fuelmonitoring.admin.fragments.wrapperclasses.ContactEmails;
import com.example.fuelmonitoring.admin.fragments.wrapperclasses.ContactEmailsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class contact_emails extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private ArrayList<ContactEmails> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_contact_emails, container, false);

        recyclerView = view.findViewById(R.id.mailRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("ContactEmails");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    String s = dataSnapshot1.getKey();
                    String d = s.substring(0, 10);
                    String t = s.substring(10, 16);

                    ContactEmails contactEmails = dataSnapshot1.getValue(ContactEmails.class);
                    contactEmails.setDate(d);
                    contactEmails.setTime(t);

                    list.add(contactEmails);
                }
                ContactEmailsAdapter contactEmailsAdapter = new ContactEmailsAdapter(view.getContext(),list);
                recyclerView.setAdapter(contactEmailsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyApp.getContext(), "Error!!!",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
