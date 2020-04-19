package com.example.fuelmonitoring.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.ProgressDialog;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fuelmonitoring.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.regex.Pattern;

public class EditProfile extends AppCompatActivity {

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

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String >(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);

        MaterialBetterSpinner materialBetterSpinner = (MaterialBetterSpinner) findViewById(R.id.city);
        materialBetterSpinner.setAdapter(stringArrayAdapter);

        firstname= (EditText) findViewById(R.id.firstname);
        lastname= (EditText) findViewById(R.id.lastname);
        contact= (EditText) findViewById(R.id.mobile);
        citynm= (EditText) findViewById(R.id.city);
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

           showData();
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
            Toast.makeText(EditProfile.this, "First Name is required!!!", Toast.LENGTH_SHORT).show();
            return;
        } else if(!NAME_PATTERN.matcher(fname).matches()){
            Toast.makeText(EditProfile.this, "Enter valid First Name!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(lname)){
            Toast.makeText(EditProfile.this, "Last Name is required!!!!", Toast.LENGTH_SHORT).show();
            return;
        } else if(!NAME_PATTERN.matcher(lname).matches()){
            Toast.makeText(EditProfile.this, "Enter valid Last Name!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(mobile)){
            Toast.makeText(EditProfile.this, "Mobile No is required!!!", Toast.LENGTH_SHORT).show();
            return;
        } else if (!MOBILE_PATTERN.matcher(mobile).matches()){
            Toast.makeText(EditProfile.this, "Enter valid Mobile No!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(city)){
            Toast.makeText(EditProfile.this, "Select valid City!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(state)){
            Toast.makeText(EditProfile.this, "Enter valid State Name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(mail)){
            Toast.makeText(EditProfile.this, "email is required!!!", Toast.LENGTH_SHORT).show();
            return;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            Toast.makeText(EditProfile.this, "Enter valid e-mail!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pass)){
            Toast.makeText(EditProfile.this, "Password cannot be empty!!!", Toast.LENGTH_SHORT).show();
            return;
        } else if(!PASSWORD_PATTERN.matcher(pass).matches()){
            Toast.makeText(EditProfile.this, "Password too weak!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(pass.equals(confirm)){
            progressDialog.setMessage("Updating Profile! Please wait.");
            progressDialog.show();
            sendUserData();
            Toast.makeText(EditProfile.this, "*Your Profile has been Updated*", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();

        } else {
            progressDialog.dismiss();
            Toast.makeText(EditProfile.this, "*Passwords don't match!!!*", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).child("Profileinfo");;

        UserProfile userProfile = new UserProfile(fname, lname, mobile, city, state, mail, pass, confirm);
        databaseReference.setValue(userProfile);
    }

    public void showData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).child("Profileinfo");;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                firstname.setText(userProfile.getFname());
                lastname.setText(userProfile.getLname());
                contact.setText(userProfile.getMobile());
                citynm.setText(userProfile.getCity());
                statenm.setText(userProfile.getState());
                email.setText(userProfile.getMail());
                passwd.setText(userProfile.getPass());
                confirmpass.setText(userProfile.getConfirm());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(EditProfile.this, "Values not Available!!!", Toast.LENGTH_SHORT).show();
                showNotification("Values not Available!!!");
            }
        });
    }
    public  void  showNotification(String message){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyNotifications")
                .setContentTitle("Fuel Monitoring System")
                .setSmallIcon(R.drawable.gas)
                .setAutoCancel(false)
                .setPriority(999)
                .setContentText(message);

        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, builder.build());
    }
}
