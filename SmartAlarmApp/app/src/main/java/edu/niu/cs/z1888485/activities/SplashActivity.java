package edu.niu.cs.z1888485.activities;
/***************************************************************
 *                                                             *
 * CSCI 524      Graduate Semester Project         Spring 2021 *
 *                                                             *
 *                                                             *
 * Programmer:    Shardul Deepak Arjunwadkar(z1888485)         *
 *                Ashwanth Kalaiselvi anandhan(z1886742),      *
 *                                                             *
 *                                                             *
 * Due Date & Time:   04/22/2021 11:59PM                       *
 *                                                             *
 * Class name: SplashActivity                                  *
 * Purpose: This class is used to display the splash screen.   *
 *                                                             *
 ***************************************************************/
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import edu.niu.cs.z1888485.R;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    Animation topAnim , bottomAnim;
    ImageView logo ;
    TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,WindowManager.LayoutParams.FLAG_FULLSCREEN  );

        //setup Animation
        topAnim= AnimationUtils.loadAnimation(this , R.anim.top_anim);
        bottomAnim= AnimationUtils.loadAnimation(this , R.anim.bottom_anim);

        logo = findViewById(R.id.logo);
        appName = findViewById(R.id.tv_app_name);

        logo.setAnimation(topAnim);
        appName.setAnimation(bottomAnim);


        new Handler().postDelayed(() -> {

            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 10000);// 10 sec
    }
}