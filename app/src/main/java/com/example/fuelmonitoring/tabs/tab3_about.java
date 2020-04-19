package com.example.fuelmonitoring.tabs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.admin.fragments.wrapperclasses.ContactEmails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class tab3_about extends Fragment {
    private EditText mail;
    private Button submitBtn;
    private ImageButton fbBtn, googleBtn, instagramBtn, twitterBtn;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_about, container, false);

        mail = (EditText) rootView.findViewById(R.id.userEmail);
        submitBtn = (Button) rootView.findViewById(R.id.submitbtn);
        fbBtn = (ImageButton) rootView.findViewById(R.id.fbBtn);
        googleBtn = (ImageButton) rootView.findViewById(R.id.googleBtn);
        instagramBtn = (ImageButton) rootView.findViewById(R.id.instaBtn);
        twitterBtn = (ImageButton) rootView.findViewById(R.id.twitterBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit(mail.getText().toString());
            }
        });

        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.facebook.com/";
                onFbBtnClick(url);
            }
        });

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.google.com/";
                onFbBtnClick(url);
            }
        });

        instagramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.instagram.com/?hl=en";
                onFbBtnClick(url);
            }
        });

        twitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://twitter.com/login";
                onFbBtnClick(url);
            }
        });
        return rootView;
    }

    public void onSubmit(String mail){
        if(mail==null || mail.length()<5 || !(mail.contains("@")) || (!(mail.contains(".com")) && !(mail.contains(".in")) && !(mail.contains(".edu")))){
            Toast toast = Toast.makeText(this.getContext(), "Enter valid e-mail address!!!", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            Toast.makeText(this.getContext(), "Mail Submitted Successfully", Toast.LENGTH_SHORT).show();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date());

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ContactEmails");
            ContactEmails contactEmails = new ContactEmails(mail);
            databaseReference.child(currentDateTime).setValue(contactEmails);
        }
    }

    public void onFbBtnClick(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void onGoogleBtnClick(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void onInstagramBtnClick(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void onTwitterBtnClick(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
