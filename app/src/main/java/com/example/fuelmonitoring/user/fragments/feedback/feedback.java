package com.example.fuelmonitoring.user.fragments.feedback;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fuelmonitoring.R;

public class feedback extends Fragment {

    private Button submitBtn;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback_form, container, false);

        progressDialog = new ProgressDialog(this.getContext());

        submitBtn = view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitBtnOnClick();
            }
        });

        return view;
    }

    public void submitBtnOnClick() {
        //Toast.makeText(getContext(), "Thank you!! Submitting your Feedback.", Toast.LENGTH_SHORT).show();
        progressDialog.setMessage("Submitting your Feedback, please wait...");
        progressDialog.show();

        progressDialog.dismiss();
        Toast.makeText(getContext(), "Thank you for your valuable Feedback", Toast.LENGTH_SHORT).show();
    }
}
