package com.example.fuelmonitoring.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.ProgressDialog;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

public class EditProfile extends AppCompatActivity {

    private EditText email, passwd, confirmpass, firstname, lastname, contact, citynm, statenm;
    private Button registerBtn;

    String fname="", lname="",  mobile="",  city="", state="", mail="", pass="", confirm="";

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

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
            Toast.makeText(EditProfile.this, "Enter valid First Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(lname)){
            Toast.makeText(EditProfile.this, "Enter valid Last Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(mobile)){
            Toast.makeText(EditProfile.this, "Enter valid Mobile No", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(city)){
            Toast.makeText(EditProfile.this, "Enter valid City Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(state)){
            Toast.makeText(EditProfile.this, "Enter valid State Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(mail)){
            Toast.makeText(EditProfile.this, "Enter valid email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(EditProfile.this, "Enter valid password", Toast.LENGTH_SHORT).show();
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
