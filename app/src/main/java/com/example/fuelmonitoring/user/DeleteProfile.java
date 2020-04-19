package com.example.fuelmonitoring.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fuelmonitoring.MainActivity;
import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.admin.fragments.viewusers.MoreUserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteProfile extends AppCompatActivity {

    private Button dltProBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_profile);

        dltProBtn = findViewById(R.id.deleteMyProfile);
        dltProBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMyPro();
            }
        });
    }

    public  void deleteMyPro(){
        AlertDialog.Builder dialog  =new AlertDialog.Builder(this);
        dialog.setTitle("Are you sure?");
        dialog.setMessage("Deleting account will result into completely removing user info from the app!");
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {
                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

               DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(firebaseAuth.getUid());
                databaseReference.removeValue();

                final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference()
                        .child("UsageDetails").child(firebaseAuth.getUid());
                if(databaseReference1.getKey().equals(firebaseAuth.getUid())){
                    databaseReference1.removeValue();
                }

               FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(DeleteProfile.this, "Profile has been successfully deleted.",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(DeleteProfile.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            startActivity(intent);
                        } else {
                            Toast.makeText(DeleteProfile.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }
}
