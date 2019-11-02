package com.example.myapplication.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.admin.AdminFgtPassword;
import com.example.myapplication.admin.AdminHome;
import com.example.myapplication.R;

public class tab2_admin extends Fragment{
    private EditText uname, pass;
    private Button loginBtn, forgotpassBtn;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_admin, container, false);
        uname = (EditText) rootView.findViewById(R.id.username);
        pass = (EditText) rootView.findViewById(R.id.password);

        loginBtn = (Button) rootView.findViewById(R.id.loginbtn);
        forgotpassBtn = (Button) rootView.findViewById(R.id.forgortpassbtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(uname.getText().toString(), pass.getText().toString());
            }
        });

        forgotpassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openResetPassForm();
            }
        });
        return rootView;
    }

    public void validate(String uname, String pass){
        if(uname.equals("admin") && pass.equals("admin")){
            Intent intent = new Intent(this.getContext(), AdminHome.class);
            Toast toast = Toast.makeText(this.getContext(), "Logging in", Toast.LENGTH_SHORT);
            toast.show();
            startActivity(intent);
        }else {
            Toast toast = Toast.makeText(this.getContext(), "Wrong credentials!!!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public  void  openResetPassForm(){
        Toast toast1 = Toast.makeText(this.getContext(), "Opening Password Reset Form", Toast.LENGTH_SHORT);
        toast1.show();
        Intent intent = new Intent(this.getContext(), AdminFgtPassword.class);
        startActivity(intent);
    }
}
