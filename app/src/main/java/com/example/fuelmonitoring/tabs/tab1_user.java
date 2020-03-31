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

import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.user.UserFgtPassword;
import com.example.fuelmonitoring.user.UserHome;
import com.example.fuelmonitoring.user.UserRegistration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class tab1_user extends Fragment {

        private EditText uname, pass;
        private Button loginBtn, forgotpassBtn, registerBtn;

        private ProgressDialog progressDialog;
        private FirebaseAuth firebaseAuth;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View rootView = inflater.inflate(R.layout.tab1_user, container, false);

                progressDialog = new ProgressDialog(this.getContext());
                firebaseAuth = FirebaseAuth.getInstance();

                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(this.getContext(), UserHome.class));
                }

                uname = (EditText) rootView.findViewById(R.id.userEmail);
                pass = (EditText) rootView.findViewById(R.id.password);

                loginBtn = (Button) rootView.findViewById(R.id.loginbtn);
                forgotpassBtn = (Button) rootView.findViewById(R.id.forgortpassbtn);
                registerBtn = (Button) rootView.findViewById(R.id.registerbtn);

                //OnClickListener for Login Button
                loginBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        validate(uname.getText().toString(), pass.getText().toString());
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

        public void validate(String uname, String pass) {

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
                        checkEmailVerification();
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
        boolean verified = firebaseUser.isEmailVerified();

        if(verified){
            Toast.makeText(tab1_user.this.getContext(), "*Login Success*", Toast.LENGTH_SHORT);
            tab1_user.this.getActivity().finish();
            startActivity(new Intent(tab1_user.this.getContext(), UserHome.class));
        } else {
            firebaseAuth.signOut();
            Toast.makeText(tab1_user.this.getContext(), "Please verify your e-mail", Toast.LENGTH_SHORT).show();
        }
    }
}
