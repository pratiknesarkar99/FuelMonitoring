package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.VideoView;

public class SplashScreen extends AppCompatActivity {

    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        videoView = (VideoView) findViewById(R.id.videoview);
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splashvideo);
        videoView.setVideoURI(video);
        videoView.setZOrderOnTop(true);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(isFinishing())
                    return;

                Intent i=new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        videoView.start();
    }
}
