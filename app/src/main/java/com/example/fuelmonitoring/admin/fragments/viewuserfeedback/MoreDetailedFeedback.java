package com.example.fuelmonitoring.admin.fragments.viewuserfeedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.admin.fragments.wrapperclasses.ViewUserFeedback;
import com.example.fuelmonitoring.user.UserProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MoreDetailedFeedback extends AppCompatActivity {

    private TextView uidTV, dateTV, timeTV, mailTV, r1TV, r2TV, r3TV, r4TV, a1, a2;
    private Button goBackBtn;
    private  static  String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_detailed_feedback);

        uid = getIntent().getStringExtra("uid");

        uidTV = findViewById(R.id.uidFeedbackTV);
        dateTV = findViewById(R.id.dateFeedbackTV);
        timeTV = findViewById(R.id.timeFeedbackTV);
        mailTV = findViewById(R.id.mailFeedbackTV);

        r1TV =  findViewById(R.id.rating1);
        r2TV =  findViewById(R.id.rating2);
        r3TV =  findViewById(R.id.rating3);
        r4TV =  findViewById(R.id.rating4);

        a1 =  findViewById(R.id.answer1);
        a2 =  findViewById(R.id.answer2);

        fetchUserData();

        goBackBtn = findViewById(R.id.goBack);
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public  void fetchUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Feedback").child(uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ViewUserFeedback viewUserFeedback = dataSnapshot.getValue(ViewUserFeedback.class);

                //System.err.println(dataSnapshot.getValue().toString());
                String s = dataSnapshot.child("currentDate").getValue().toString();
                String d = s.substring(0, 10);
                String t = s.substring(10, 19);

                uidTV.setText(uid);
                dateTV.setText(d);
                timeTV.setText(t);
                mailTV.setText(viewUserFeedback.getMail());

                r1TV.setText(viewUserFeedback.getR1());
                r2TV.setText(viewUserFeedback.getR2());
                r3TV.setText(viewUserFeedback.getR3());
                r4TV.setText(viewUserFeedback.getR4());

                a1.setText(viewUserFeedback.getAns1());
                a2.setText(viewUserFeedback.getAns2());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Err: ---- " + databaseError);
            }
        });
    }

    }
