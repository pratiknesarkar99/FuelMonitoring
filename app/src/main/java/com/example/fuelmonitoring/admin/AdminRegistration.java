package com.example.fuelmonitoring.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fuelmonitoring.MainActivity;
import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.user.UserHome;
import com.example.fuelmonitoring.user.UserProfile;
import com.example.fuelmonitoring.user.UserRegistration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class AdminRegistration extends AppCompatActivity {

    private  static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{4,}" +               //at least 4 characters
            "$");

    private EditText email, passwd, confirmpass, secretcode;
    private Button registerBtn;

    String mail="", pass="", confirm="", code="";

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration);

        email = (EditText) findViewById(R.id.adminEmail);
        passwd = (EditText) findViewById(R.id.adminPassword);
        confirmpass= (EditText) findViewById(R.id.adminConfirmPasswd);
        secretcode= (EditText) findViewById(R.id.secretCode);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        registerBtn = (Button) findViewById(R.id.RegisterButton);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitForm();
            }
        });
    }

    public void onSubmitForm() {
        mail = email.getText().toString();
        pass = passwd.getText().toString();
        confirm = confirmpass.getText().toString();
        code = secretcode.getText().toString();

        if (TextUtils.isEmpty(mail)) {
            Toast.makeText(AdminRegistration.this, "e-mail cannot be empty!!!", Toast.LENGTH_SHORT).show();
            return;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            Toast.makeText(AdminRegistration.this, "Enter valid e-mail!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(AdminRegistration.this, "Enter valid password", Toast.LENGTH_SHORT).show();
            return;
        } else if(!PASSWORD_PATTERN.matcher(pass).matches()){
            Toast.makeText(this, "Password is weak!!! Try a stronger Password.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(code)) {
            Toast.makeText(AdminRegistration.this, "Enter valid Secret Code", Toast.LENGTH_SHORT).show();
            return;
        }

        if (code.equals("0000")) {
            if (pass.equals(confirm)) {
                progressDialog.setMessage("Registering You! Please wait.");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendEmailVerification();
                            finish();
                            startActivity(new Intent(AdminRegistration.this, AdminHome.class));
                            Toast.makeText(AdminRegistration.this, "*Check your e-mail address*", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(AdminRegistration.this, "*Registration Failed!!! Try Again*", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            } else {
                progressDialog.dismiss();
                Toast.makeText(AdminRegistration.this, "*Passwords don't match!!!*", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(AdminRegistration.this, "*Wrong Code entered!!! Try Again...*", Toast.LENGTH_SHORT).show();
            return;
        }
    }
        public void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        sendUserData();
                        firebaseAuth.signOut();
                        finish();
                        Toast.makeText(AdminRegistration.this, "*Registration Successful*", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminRegistration.this, MainActivity.class));
                    } else {
                        Toast.makeText(AdminRegistration.this,  "*e-mail not sent!!!*", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Admins").child(firebaseAuth.getUid()).child("Profileinfo");

        AdminProfile adminProfile = new AdminProfile(mail, pass, confirm);
        databaseReference.setValue(adminProfile);
    }
}
