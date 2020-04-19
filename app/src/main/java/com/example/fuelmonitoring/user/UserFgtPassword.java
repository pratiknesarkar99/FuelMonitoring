package com.example.fuelmonitoring.user;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class UserFgtPassword extends AppCompatActivity {

    EditText userEmail;
    Button sendMailBtn;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_fgt_password);

        firebaseAuth = FirebaseAuth.getInstance();

        userEmail = findViewById(R.id.userEmail);
        sendMailBtn = findViewById(R.id.sendmailbtn);

        sendMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(userEmail.getText())){
                    Toast.makeText(UserFgtPassword.this, "email is required!!!", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!Patterns.EMAIL_ADDRESS.matcher(userEmail.getText()).matches()){
                    Toast.makeText(UserFgtPassword.this, "Enter valid e-mail!!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(UserFgtPassword.this, "*Please check your e-mail*", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(UserFgtPassword.this, UserHome.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(UserFgtPassword.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
