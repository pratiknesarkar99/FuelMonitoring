package com.example.fuelmonitoring.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.MyApp;
import com.example.fuelmonitoring.MainActivity;
import com.example.fuelmonitoring.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.regex.Pattern;

public class UserRegistration extends AppCompatActivity {

    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]{2,}");

    private static final Pattern MOBILE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");

    private  static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{4,}" +               //at least 4 characters
            "$");

    String [] SPINNERLIST = {"Mumbai", "Pune", "Kolhapur"};

    private EditText email, passwd, confirmpass, firstname, lastname, contact, citynm, statenm;
    private Button registerBtn;

    String fname="", lname="",  mobile="",  city="", state="", mail="", pass="", confirm="";
    String value = "0mL";

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String >(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);

        MaterialBetterSpinner materialBetterSpinner = (MaterialBetterSpinner) findViewById(R.id.city);
        materialBetterSpinner.setAdapter(stringArrayAdapter);

        firstname= (EditText) findViewById(R.id.firstname);
        lastname= (EditText) findViewById(R.id.lastname);
        contact= (EditText) findViewById(R.id.mobile);
        citynm=  findViewById(R.id.city);
        statenm= (EditText) findViewById(R.id.state);

        email = (EditText) findViewById(R.id.userEmail);
        passwd = (EditText) findViewById(R.id.password);
        confirmpass= (EditText) findViewById(R.id.confirmpassword);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        registerBtn = (Button) findViewById(R.id.RegisterBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitForm();
            }
        });

    }

    public void onSubmitForm() {
        fname= firstname.getText().toString();
        lname= lastname.getText().toString();
        mobile = contact.getText().toString();
        city= citynm.getText().toString();
        state = statenm.getText().toString();

        mail = email.getText().toString();
        pass = passwd.getText().toString();
        confirm = confirmpass.getText().toString();

        if(TextUtils.isEmpty(fname)){
            Toast.makeText(UserRegistration.this, "First Name is required!!!", Toast.LENGTH_SHORT).show();
            return;
        } else if(!NAME_PATTERN.matcher(fname).matches()){
            Toast.makeText(UserRegistration.this, "Enter valid First Name!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(lname)){
            Toast.makeText(UserRegistration.this, "Last Name is required!!!!", Toast.LENGTH_SHORT).show();
            return;
        } else if(!NAME_PATTERN.matcher(lname).matches()){
            Toast.makeText(UserRegistration.this, "Enter valid Last Name!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(mobile)){
            Toast.makeText(UserRegistration.this, "Mobile No is required!!!", Toast.LENGTH_SHORT).show();
            return;
        } else if (!MOBILE_PATTERN.matcher(mobile).matches()){
            Toast.makeText(UserRegistration.this, "Enter valid Mobile No!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(city)){
            Toast.makeText(UserRegistration.this, "Select valid City!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(state)){
            Toast.makeText(UserRegistration.this, "Enter valid State Name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(mail)){
            Toast.makeText(UserRegistration.this, "email is required!!!", Toast.LENGTH_SHORT).show();
            return;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            Toast.makeText(UserRegistration.this, "Enter valid e-mail!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pass)){
            Toast.makeText(UserRegistration.this, "Password cannot be empty!!!", Toast.LENGTH_SHORT).show();
            return;
        } else if(!PASSWORD_PATTERN.matcher(pass).matches()){
            Toast.makeText(UserRegistration.this, "Password too weak!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(pass.equals(confirm)){
            progressDialog.setMessage("Registering You! Please wait.");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendEmailVerification();
     //                           Toast.makeText(UserRegistration.this, "*Check your e-mail address*", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                showNotification("Please check your e-mail inbox to continue.");
                            } else {
       //                         Toast.makeText(UserRegistration.this, "*Registration Failed!!! Try Again*", Toast.LENGTH_SHORT).show();
                                showNotification("Registration Failed!!! Try Again");
                                progressDialog.dismiss();
                            }
                        }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(UserRegistration.this, "*Passwords don't match!!!*", Toast.LENGTH_SHORT).show();
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
                        startActivity(new Intent(UserRegistration.this, UserHome.class));
   //                     Toast.makeText(UserRegistration.this, "*Registration Successful*", Toast.LENGTH_SHORT).show();
                    } else {
//                        Toast.makeText(UserRegistration.this,  "*e-mail not sent!!!*", Toast.LENGTH_SHORT).show();
                        showNotification("e-mail not sent");
                    }
                }
            });
        }
    }

    public void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        UserProfile userProfile = new UserProfile(fname, lname, mobile, city, state, mail, pass, confirm);
        databaseReference.child("Users").child(firebaseAuth.getUid()).setValue(userProfile);

        databaseReference.child("FuelIn").child(firebaseAuth.getUid()).child("value").setValue("0.0");
        databaseReference.child("FuelOut").child(firebaseAuth.getUid()).child("value").setValue("0.0");
        databaseReference.child("UsageDetails").child("showsUsageDetails").setValue("of users");

    }

    public  void  showNotification(String message){
        Intent intent = new Intent(MyApp.getContext(), MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(MyApp.getContext(), 100,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApp.getContext(), "MyNotifications")
                .setContentTitle("Fuel Monitoring System")
                .setSmallIcon(R.drawable.gas)
                .setAutoCancel(false)
                .setPriority(999)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContentText(message);

        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MyApp.getContext());
        notificationManagerCompat.notify(1, builder.build());
    }
}
