package com.example.fuelmonitoring;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.MyApp;
import com.example.fuelmonitoring.tabs.SectionsPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private  String TandC = "Terms &Conditions\nWelcome to Fuel Monitoring System!\n" +
            "\n" +
            "These terms and conditions outline the rules and regulations for the use of  'The Backbencher's Organization',  site located at " +
            "\nwww.fuelmonitoringsystem.com\n" +
            "\n" +
            "By accessing this application we assume you accept these terms and conditions. Do not continue to use Fuel Monitoring System " +
            "if you do not agree to take all of the terms and conditions stated on this page. \n" +
            "\n" +
            "The following terminology applies to these Terms and Conditions, Privacy Statement and Disclaimer Notice and all Agreements:" +
            " \"Client\", \"You\" and \"Your\" refers to you, the person log on this application and compliant to the Company’s terms and conditions. " +
            "\"The Company\", \"Ourselves\", \"We\", \"Our\" and \"Us\", refers to our Company. \"Party\", \"Parties\", or \"Us\", refers to both the" +
            " Client and ourselves. All terms refer to the offer, acceptance and consideration of payment necessary to undertake the process of our " +
            "assistance to the Client in the most appropriate manner for the express purpose of meeting the Client’s needs in respect of provision of " +
            "the Company’s stated services, in accordance with and subject to, prevailing law of India. Any use of the above terminology or " +
            "other words in the singular, plural, capitalization and/or he/she or they, are taken as interchangeable and therefore as referring to same.\n" +
            "\n"+
            "LICENSE: \n" +
            "Unless otherwise stated, The Backbenchers and/or its licensors own the intellectual property rights for all material on Fuel Monitoring System. " +
            "All intellectual property rights are reserved. You may access this from Fuel Monitoring System for your own personal use subjected to " +
            "restrictions set in these terms and conditions.\n" +
            "\n" +
            "YOU MUST NOT:\n" +
            "\n" +
            "Republish material from Fuel Monitoring System\n" +
            "Sell, rent or sub-license material from Fuel Monitoring System\n" +
            "Reproduce, duplicate or copy material from Fuel Monitoring System\n" +
            "Redistribute content from Fuel Monitoring System\n" +
            "This Agreement shall begin on the date hereof.\n" +
            "\n" +
            "Parts of this application offer an opportunity for users to post and exchange opinions and information in certain areas of the application. " +
            "The Backbenchers does not filter, edit, publish or review Comments prior to their presence on the application. Comments do not reflect the views " +
            "and opinions of The Backbenchers,its agents and/or affiliates. Comments reflect the views and opinions of the person who post their views and " +
            "opinions. To the extent permitted by applicable laws, The Backbenchers shall not be liable for the Comments or for any liability, damages " +
            "or expenses caused and/or suffered as a result of any use of and/or posting of and/or appearance of the Comments on this application.\n" +
            "\n" +
            "The Backbenchers reserves the right to monitor all Comments and to remove any Comments which can be considered inappropriate, offensive " +
            "or causes breach of these Terms and Conditions.\n" +
            "\n" +
            "You warrant and represent that:\n" +
            "\n" +
            "You are entitled to post the Comments on our application and have all necessary licenses and consents to do so;\n" +
            "The Comments do not invade any intellectual property right, including without limitation copyright, patent or trademark of any third party;\n" +
            "The Comments do not contain any defamatory, libelous, offensive, indecent or otherwise unlawful material which is an invasion of privacy\n" +
            "The Comments will not be used to solicit or promote business or custom or present commercial activities or unlawful activity.\n" +
            "You hereby grant The Backbenchers a non-exclusive license to use, reproduce, edit and authorize others to use, reproduce and edit any" +
            " of your Comments in any and all forms, formats or media.\n" +
            "\n" +
            "YOUR PRIVACY\n" +
            "Please read Privacy Policy\n" +
            "\n" +
            "Reservation of Rights\n" +
            "We reserve the right to request that you remove all links or any particular link to our application. You approve to immediately remove all " +
            "links to our application upon request. We also reserve the right to amen these terms and conditions and it’s linking policy at any time. " +
            "By continuously linking to our application, you agree to be bound to and follow these linking terms and conditions.\n" +
            "\n" +
            "Removal of links from our application\n" +
            "If you find any link on our application that is offensive for any reason, you are free to contact and inform us any moment. We will consider " +
            "requests to remove links but we are not obligated to or so or to respond to you directly.\n" +
            "\n" +
            "We do not ensure that the information on this application is correct, we do not warrant its completeness or accuracy; nor do we promise to " +
            "ensure that the application remains available or that the material on the application is kept up to date.\n" +
            "\n" +
            "DISCLAIMER\n" +
            "To the maximum extent permitted by applicable law, we exclude all representations, warranties and conditions relating to our application " +
            "and the use of this application. Nothing in this disclaimer will:\n" +
            "\n" +
            "limit or exclude our or your liability for death or personal injury;\n" +
            "limit or exclude our or your liability for fraud or fraudulent misrepresentation;\n" +
            "limit any of our or your liabilities in any way that is not permitted under applicable law; or\n" +
            "exclude any of our or your liabilities that may not be excluded under applicable law.\n" +
            "The limitations and prohibitions of liability set in this Section and elsewhere in this disclaimer: (a) are subject to the preceding paragraph; " +
            "and (b) govern all liabilities arising under the disclaimer, including liabilities arising in contract, in tort and for breach of statutory duty.\n" +
            "\n" +
            "As long as the application and the information and services on the application are provided free of charge, we will not be liable for " +
            "any loss or damage of any nature.";


    private  SpannableString spannableString;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spannableString = new SpannableString(TandC);

        ForegroundColorSpan fcBlue = new ForegroundColorSpan(Color.BLUE);
        ForegroundColorSpan fcBlue2 = new ForegroundColorSpan(Color.BLUE);
        ForegroundColorSpan fcRed = new ForegroundColorSpan(Color.RED);
        spannableString.setSpan(fcRed, 0, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(fcBlue, 131, 164, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(fcBlue2, 315, 338, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        onFirstRun();

        if(Build.VERSION.SDK_INT>=21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.blackColor));
        }

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Succuessfull";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    public  void  onFirstRun(){

        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if(isFirstRun) {

            new AlertDialog.Builder(MainActivity.this)
                    .setCancelable(false)
                    .setIcon(R.drawable.gas)
                    .setTitle("Fuel Monitoring System")
                    .setMessage(spannableString)
                    .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            System.exit(0);
                        }
                    })
                    .setPositiveButton("Yes, I Agree.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit()
                                    .putBoolean("isFirstRun", false)
                                    .apply();
                        }
                    }).show();
        }
    }
}