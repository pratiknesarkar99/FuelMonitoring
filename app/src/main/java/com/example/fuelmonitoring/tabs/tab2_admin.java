package com.example.fuelmonitoring.tabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fuelmonitoring.admin.AdminFgtPassword;
import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.admin.AdminHome;
import com.example.fuelmonitoring.admin.AdminRegistration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class tab2_admin extends Fragment{
    private EditText uname, pass;
    private Button loginBtn, forgotpassBtn, registerBtn;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_admin, container, false);

        progressDialog = new ProgressDialog(this.getContext());
        firebaseAuth = FirebaseAuth.getInstance();

        uname = (EditText) rootView.findViewById(R.id.userEmail);
        pass = (EditText) rootView.findViewById(R.id.password);

        loginBtn = (Button) rootView.findViewById(R.id.loginbtn);
        forgotpassBtn = (Button) rootView.findViewById(R.id.forgortpassbtn);

        registerBtn = (Button)rootView.findViewById(R.id.registerbtn3);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegistrationForm();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(uname.getText().toString(), pass.getText().toString());
            }
        });

        forgotpassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openResetPassForm();
            }
        });
        return rootView;
    }

    public void validate(String uname, String pass){
        if(TextUtils.isEmpty(uname)){
            Toast.makeText(this.getContext(), "Enter valid email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this.getContext(), "Enter valid password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Logging You in! Please wait.");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(uname, pass).addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(tab2_admin.this.getContext(), "*Login Success*", Toast.LENGTH_SHORT);
                    tab2_admin.this.getActivity().finish();
                    startActivity(new Intent(tab2_admin.this.getContext(), AdminHome.class));
                } else {
                    Toast.makeText(tab2_admin.this.getContext(), "*Login Failed!!! Try Again.*", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    public  void  openResetPassForm(){
        Toast.makeText(this.getContext(), "Opening Password Reset Form", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this.getContext(), AdminFgtPassword.class);
        startActivity(intent);
    }

    public void openRegistrationForm(){
        Toast.makeText(this.getContext(), "Opening Registration Form", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this.getContext(), AdminRegistration.class);
        startActivity(intent);
    }
}
