package com.example.fuelmonitoring.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.user.UserHome;
import com.example.fuelmonitoring.user.UserRegistration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class AdminFgtPassword extends AppCompatActivity {

    EditText adminEmail;
    Button sendMailBtn;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_fgt_password);

        firebaseAuth = FirebaseAuth.getInstance();

        adminEmail = findViewById(R.id.userEmail);
        sendMailBtn = findViewById(R.id.sendmailbtn3);

        sendMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(adminEmail.getText())){
                    Toast.makeText(AdminFgtPassword.this, "email is required!!!", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!Patterns.EMAIL_ADDRESS.matcher(adminEmail.getText()).matches()){
                    Toast.makeText(AdminFgtPassword.this, "Enter valid e-mail!!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.sendPasswordResetEmail(adminEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(AdminFgtPassword.this, "*Please check your e-mail*", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(AdminFgtPassword.this, UserHome.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(AdminFgtPassword.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}
