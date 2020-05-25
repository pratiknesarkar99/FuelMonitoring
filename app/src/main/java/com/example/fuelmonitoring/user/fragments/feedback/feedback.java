package com.example.fuelmonitoring.user.fragments.feedback;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.user.fragments.user_home;
import com.example.fuelmonitoring.user.fragments.wrapperclasses.FeedbackForm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class feedback extends Fragment {

    private  RatingBar ratingBar1, ratingBar2, ratingBar3, ratingBar4;
    private  EditText editText1, editText2, feedbackEmail;

    private  String mail, ans1, ans2;
    private float r1, r2, r3, r4;

    private Button submitBtn;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_feedback_form, container, false);

        feedbackEmail = view.findViewById(R.id.feedbackemail);

        ratingBar1 = view.findViewById(R.id.ratingBar1);
        ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Toast.makeText(view.getContext(), "Your Rating: "+ ratingBar1.getRating(), Toast.LENGTH_SHORT).show();
            }
        });

        ratingBar2 = view.findViewById(R.id.ratingBar2);
        ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Toast.makeText(view.getContext(), "Your Rating: "+ ratingBar2.getRating(), Toast.LENGTH_SHORT).show();
            }
        });

        ratingBar3 = view.findViewById(R.id.ratingBar3);
        ratingBar3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Toast.makeText(view.getContext(), "Your Rating: "+ ratingBar3.getRating(), Toast.LENGTH_SHORT).show();
            }
        });

        ratingBar4 = view.findViewById(R.id.ratingBar4);
        ratingBar4.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Toast.makeText(view.getContext(), "Your Rating: "+ ratingBar4.getRating(), Toast.LENGTH_SHORT).show();
            }
        });

        editText1 = view.findViewById(R.id.ans1);
        editText2 = view.findViewById(R.id.ans2);

        progressDialog = new ProgressDialog(this.getContext());

        submitBtn = view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail = feedbackEmail.getText().toString();
                ans1 = editText1.getText().toString();
                ans2 = editText2.getText().toString();

                r1 = ratingBar1.getRating();
                r2 = ratingBar2.getRating();
                r3 = ratingBar3.getRating();
                r4 = ratingBar4.getRating();


                if(ans1.equals("")){
                    ans1 = "No issues.";
                }

                if (ans2.equals("")){
                    ans2="No further scope for development needed";
                }

                if(mail==null || mail.length()<5 || !(mail.contains("@")) || (!(mail.contains(".com")) && !(mail.contains(".in")) && !(mail.contains(".edu")))){
                    Toast toast = Toast.makeText(view.getContext(), "Enter valid e-mail address!!!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    submitBtnOnClick(mail, r1, r2, r3, r4, ans1, ans2);
                }
            }
        });

        return view;
    }

    public void submitBtnOnClick(String mail, float r1, float r2, float r3, float r4, String ans1, String ans2) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Feedback");

        //Toast.makeText(getContext(), "Thank you!! Submitting your Feedback.", Toast.LENGTH_SHORT).show();
        progressDialog.setMessage("Submitting your Feedback, please wait...");
        progressDialog.show();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        FeedbackForm feedbackForm = new FeedbackForm(mail, ans1, ans2, r1+"", r2+"", r3+"", r4+"", currentDateTime);
        databaseReference.child(firebaseAuth.getUid()).setValue(feedbackForm);

        progressDialog.dismiss();
        Toast.makeText(getContext(), "Thank you for your valuable Feedback", Toast.LENGTH_SHORT).show();

        for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        Fragment fragment = null;
        fragment = new user_home();
        replaceFragment(fragment);
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
