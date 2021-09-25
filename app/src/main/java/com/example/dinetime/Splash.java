package com.example.dinetime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.dinetime.Activity.Activity_Bottom_navigation;
import com.example.dinetime.Activity.Login;
import com.example.dinetime.Fragments.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    ImageView image;
    private static int SPLASH_TIME = 2500;
    FirebaseUser user;
    private Animation top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        image = findViewById(R.id.logo);

        top = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        user = FirebaseAuth.getInstance().getCurrentUser();

        checkLogin();
        image.setAnimation(top);

    }

    private void checkLogin(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(user != null) {
                    Intent intent = new Intent(Splash.this, Activity_Bottom_navigation.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(Splash.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_TIME);
    }
}