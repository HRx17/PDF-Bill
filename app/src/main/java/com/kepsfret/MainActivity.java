package com.kepsfret;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ProgressBar progressBar;
    FirebaseAuth auth;

    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progess);
        progressBar.setVisibility(View.VISIBLE);
        auth = FirebaseAuth.getInstance();
        textView = findViewById(R.id.splash);
        Animation animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        Animation animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);

        animFadeIn.reset();
        textView.clearAnimation();
        textView.startAnimation(animFadeIn);
        animFadeOut.reset();
        textView.clearAnimation();
        textView.startAnimation(animFadeOut);
        animFadeIn.reset();
        textView.clearAnimation();
        textView.startAnimation(animFadeIn);


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                if (auth.getCurrentUser() != null) {
                    Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                    startActivity(intent);
                    finish();

                } else {
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        },5500);
    }
}