package bj.zemidjan.dio.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import bj.zemidjan.dio.R;

/**
 * Created by Oulfath on 18/08/2017.
 */
public class SplashScreenActivity extends AppCompatActivity {
    VideoView videoView;
    //ImageView img;
    String nom, prenom, email;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        nom = preferences.getString("nom","");
        prenom = preferences.getString("prenom","");
        email = preferences.getString("email","");
        if (nom.equals("")) {

        }else{
            startActivity(new Intent(SplashScreenActivity.this, AccueilActivity.class));
            finish();
        }
        videoView = (VideoView) findViewById(R.id.videoView);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ea_video);
        videoView.setVideoURI(video);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                startNextActivity();
            }
        });

        videoView.start();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }



   /* //img=(ImageView)findViewById(R.id.imageSplash);
    ActionBar actionBar = getSupportActionBar();
    actionBar.hide();

    *//*img.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
        Intent i=new Intent(SplashScreenActivity.this,OnboardingActivity.class);
        startActivity(i);
    }
    });*//*

   new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            finish();
            Intent i=new Intent(SplashScreenActivity.this,OnboardingActivity.class);
            startActivity(i);
        }
    },2000);*/
}


    private void startNextActivity() {
        if (isFinishing())
            return;
        finish();
        Intent i=new Intent(SplashScreenActivity.this,OnboardingActivity.class);
        startActivity(i);
    }
}