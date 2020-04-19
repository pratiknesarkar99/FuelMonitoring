package com.example.fuelmonitoring.tabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.MyApp;
import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.admin.AdminHome;
import com.example.fuelmonitoring.user.UserFgtPassword;
import com.example.fuelmonitoring.user.UserHome;
import com.example.fuelmonitoring.user.UserRegistration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class tab1_user extends Fragment {

    private  static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{4,}" +               //at least 4 characters
            "$");

    private EditText emailaddr, pass;
        private Button loginBtn, forgotpassBtn, registerBtn;

        private ProgressDialog progressDialog;
        private FirebaseAuth firebaseAuth;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View rootView = inflater.inflate(R.layout.tab1_user, container, false);

                progressDialog = new ProgressDialog(this.getContext());
                firebaseAuth = FirebaseAuth.getInstance();

               if(firebaseAuth.getCurrentUser() != null ) {
                   if (firebaseAuth.getUid().contains("h082BRisKuXPXT4DgdyL9vu8Sk12")) {
                       startActivity(new Intent(this.getContext(), AdminHome.class));
                   } else {
                       startActivity(new Intent(this.getContext(), UserHome.class));
                   }
               }
                emailaddr = (EditText) rootView.findViewById(R.id.userEmail);
                pass = (EditText) rootView.findViewById(R.id.password);

                loginBtn = (Button) rootView.findViewById(R.id.loginbtn);
                forgotpassBtn = (Button) rootView.findViewById(R.id.forgortpassbtn);
                registerBtn = (Button) rootView.findViewById(R.id.registerbtn);

                //OnClickListener for Login Button
                loginBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        validate(emailaddr.getText().toString(), pass.getText().toString());
                    }
                });

                //OnClickListener for Forgot Password Button
                forgotpassBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openResetPassForm();
                    }
                });

                //OnClickListener for Register Button
                registerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openRegistrationForm();
                    }
                });
            return rootView;
        }

        public void validate(String emailaddr, String pass) {

            if(TextUtils.isEmpty(emailaddr)){
                Toast.makeText(this.getContext(), "e-mail cannot be empty!!!", Toast.LENGTH_SHORT).show();
                return;
            } else if(!Patterns.EMAIL_ADDRESS.matcher(emailaddr).matches()){
                Toast.makeText(this.getContext(), "Enter valid email", Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(pass)){
                Toast.makeText(this.getContext(), "Password cannot be empty!!!", Toast.LENGTH_SHORT).show();
                return;
            } else if(!PASSWORD_PATTERN.matcher(pass).matches()){
                Toast.makeText(this.getContext(), "Password is weak!!! Try a stronger Password.", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.setMessage("Logging You in! Please wait.");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(emailaddr, pass).addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        checkEmailVerification();
                    } else {
                        Toast.makeText(MyApp.getContext(), "email or password is wrong!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        public  void  openRegistrationForm(){
            Toast toast1 = Toast.makeText(this.getContext(), "Opening Registration Form", Toast.LENGTH_SHORT);
            toast1.show();
            Intent intent = new Intent(this.getContext(), UserRegistration.class);
            startActivity(intent);
        }

    public  void  openResetPassForm(){
        Toast toast1 = Toast.makeText(this.getContext(), "Opening Password Reset Form", Toast.LENGTH_SHORT);
        toast1.show();
        Intent intent = new Intent(this.getContext(), UserFgtPassword.class);
        startActivity(intent);
    }

    public void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        boolean verified = false;

        verified = firebaseUser.isEmailVerified();

        if(verified){
            Toast.makeText(MyApp.getContext(), "*Login Success*", Toast.LENGTH_SHORT);
            tab1_user.this.getActivity().finish();
            startActivity(new Intent(tab1_user.this.getContext(), UserHome.class));
        } else {
            firebaseAuth.signOut();
            Toast.makeText(MyApp.getContext(), "Please verify your e-mail!!!", Toast.LENGTH_SHORT).show();
        }
    }
}
