package com.example.android.parvarish_nutricalculator.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.custom.ComplexPreferences;
import com.example.android.parvarish_nutricalculator.model.userModel;

public class LaunchScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);

        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {

                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                boolean isLogin = preferences.getBoolean("isUserLogin", false);

              if(isLogin){
                    Intent iStartScreen = new Intent(LaunchScreen.this,HomeScreen.class);
                    startActivity(iStartScreen);
                    finish();
                }else{
                    Intent iStartScreen = new Intent(LaunchScreen.this,StartScreen.class);
                    startActivity(iStartScreen);
                    finish();
                }


            }
        }.start();

    }
}
