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

import com.example.myapplication.R;
import com.example.myapplication.user.UserFgtPassword;
import com.example.myapplication.user.UserRegistration;

public class tab1_user extends Fragment {

        private EditText uname, pass;
        private Button loginBtn, forgotpassBtn, registerBtn;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View rootView = inflater.inflate(R.layout.tab1_user, container, false);

                uname = (EditText) rootView.findViewById(R.id.username);
                pass = (EditText) rootView.findViewById(R.id.password);

                loginBtn = (Button) rootView.findViewById(R.id.loginbtn);
                forgotpassBtn = (Button) rootView.findViewById(R.id.forgortpassbtn);
                registerBtn = (Button) rootView.findViewById(R.id.registerbtn);

                //OnClickListener for Login Button
                loginBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        validate(uname.getText().toString(), pass.getText().toString());
                    }
                });

                //OnClickListener for Forgot Password Button
                forgotpassBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openResetPassForm();
                    }
                });

                //OnClickListener for Register Button
                registerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openRegistrationForm();
                    }
                });
            return rootView;
        }

        public void validate(String uname, String pass){
            String type = "userlogin";

            UserBackgroundWorker userBackgroundWorker = new UserBackgroundWorker(this.getContext());
            userBackgroundWorker.execute(type, uname, pass);
        }

        public  void  openRegistrationForm(){
            Toast toast1 = Toast.makeText(this.getContext(), "Opening Registration Form", Toast.LENGTH_SHORT);
            toast1.show();
            Intent intent = new Intent(this.getContext(), UserRegistration.class);
            startActivity(intent);
        }

    public  void  openResetPassForm(){
        Toast toast1 = Toast.makeText(this.getContext(), "Opening Password Reset Form", Toast.LENGTH_SHORT);
        toast1.show();
        Intent intent = new Intent(this.getContext(), UserFgtPassword.class);
        startActivity(intent);
    }
}
