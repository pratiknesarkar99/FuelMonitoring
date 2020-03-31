package com.example.fuelmonitoring.user.fragments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.example.fuelmonitoring.R;

public class webActivity_PetrolPrice extends AppCompatActivity {

    private WebView mWebView;
    private Button backbutton;
    String url = "https://www.bankbazaar.com/fuel/petrol-price-maharashtra.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web__petrol_price);

        mWebView = findViewById(R.id.myWebView1);
        mWebView.loadUrl(url);
    }
}
