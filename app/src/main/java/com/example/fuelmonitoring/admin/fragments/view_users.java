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
import com.example.fuelmonitoring.admin.fragments.wrapperclasses.ViewUsers;
import com.example.fuelmonitoring.admin.fragments.viewusers.ViewUsersAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;

public class view_users extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReferenceRootNode, databaseReference;
    private ArrayList<ViewUsers> list;
    private ViewUsersAdapter viewUsersAdapter;
    private  static JSONObject jsonObject;
    private  static  String datasnapResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_view_users, container, false);

        recyclerView = view.findViewById(R.id.viewUsersRecycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();

        databaseReferenceRootNode = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReferenceRootNode.child("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    Log.d("Users: ", dataSnapshot1.toString());

                    String uid = dataSnapshot1.getKey();
                    ViewUsers viewUsers = dataSnapshot1.getValue(ViewUsers.class);
                    viewUsers.setUid(uid);

                    list.add(viewUsers);

/*
                    final String uid = dataSnapshot1.getKey();
                    ViewUsers viewUsers = new ViewUsers();
                    viewUsers.setUid(uid);
                    //System.out.println(uid);

                    datasnapResult =  dataSnapshot1.getValue().toString();
                    //System.out.println(datasnapResult);
                    try {
                        jsonObject = new JSONObject(datasnapResult);
                        JSONObject ProfileInfo = jsonObject.getJSONObject("Profileinfo");

                        String fnm = ProfileInfo.getString("fname");
                        String lnm = ProfileInfo.getString("lname");
                        String mail = ProfileInfo.getString("mail");

                        viewUsers.setFname(fnm);
                        viewUsers.setLname(lnm);
                        viewUsers.setMail(mail);

                        list.add(viewUsers);
                    } catch (Exception e){
                        Log.d("Exception", String.valueOf(e));
                    }
 */
                }
                viewUsersAdapter = new ViewUsersAdapter(view.getContext(), list);
                recyclerView.setAdapter(viewUsersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyApp.getContext(), "outter nest Error!!!",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
