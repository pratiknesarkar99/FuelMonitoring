package com.example.myapplication.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.io.DataInputStream;
import java.text.DateFormat;
import java.util.Calendar;

public class UserRegistration extends AppCompatActivity {

    private EditText firstname, lastname, mobile, email, dob, city, state, passwd, confirmpass;
    String fname="",  lname="", contact="", mail="",bdate="",cityy="",statee="",pass="",confirm="",gender = "";
    private Button datePickerBtn, registerBtn;
    private int year_x, month_x, day_x;
    private static final int DIALOG_ID =0;
    private RadioGroup genderRG;
    private RadioButton male, female, other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        dob = (EditText) findViewById(R.id.dob);

        final Calendar calendar = Calendar.getInstance();
        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        day_x = calendar.get(Calendar.DATE);

        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        mobile = (EditText) findViewById(R.id.mobile);
        email = (EditText) findViewById(R.id.email);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);

        genderRG = (RadioGroup) findViewById(R.id.rg);
        male = (RadioButton) findViewById(R.id.malerb);
        female = (RadioButton) findViewById(R.id.femalerb);
        other = (RadioButton) findViewById(R.id.otherrb);

        registerBtn = (Button) findViewById(R.id.RegisterBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitForm();
            }
        });

        showDialogOnBtnClick();
    }

    public void showDialogOnBtnClick(){
        datePickerBtn = (Button) findViewById(R.id.datepickerbtn);

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DIALOG_ID )
            return new DatePickerDialog(this, dpickerListner, year_x, month_x, day_x);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            year_x = year;
            month_x = month+1;
            day_x = day;

            dob.setText(year_x+"/"+month_x+"/"+day_x);
        }
    };

    public void onSubmitForm(){
        Toast.makeText(this, "Submitting Form",Toast.LENGTH_SHORT).show();
        if(male.isChecked()){
            gender = "Male";
        }
        if(female.isChecked()){
            gender = "Female";
        }
        if(other.isChecked()){
            gender = "Other";
        }

        fname = firstname.getText().toString();
        lname = lastname.getText().toString();
        contact = mobile.getText().toString();
        mail  = email.getText().toString();
        contact = mobile.getText().toString();
        bdate = dob.getText().toString();
        cityy = city.getText().toString();
        statee = state.getText().toString();
        pass= passwd.getText().toString();
        confirm= confirmpass.getText().toString();

        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
