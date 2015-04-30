package com.example.android.parvarish_nutricalculator.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.helpers.PrefUtils;

import me.drakeet.library.UIButton;


public class StartScreen extends ActionBarActivity {

    private UIButton btnSignUp;
    private UIButton btnLogin;
    private View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent i = new Intent(StartScreen.this,LoginScreen.class);
            startActivity(i);
            finish();

        }
    };
    private View.OnClickListener signupClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent i = new Intent(StartScreen.this,SignupScreen.class);
            startActivity(i);
            finish();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        setupUI();

        if(PrefUtils.getCurrentUser(StartScreen.this) != null){

            Intent homeIntent = new Intent(StartScreen.this, LoginScreen.class);
            startActivity(homeIntent);
            finish();
        }
    }

    private void setupUI() {
        btnLogin = (UIButton)findViewById(R.id.btnLoginStartScreen);
        btnSignUp = (UIButton)findViewById(R.id.btnSignUpStartScreen);
        btnLogin.setTypeface(PrefUtils.getTypeFace(StartScreen.this));
        btnSignUp.setTypeface(PrefUtils.getTypeFace(StartScreen.this));
        btnLogin.setOnClickListener(loginClick);
        btnSignUp.setOnClickListener(signupClick);

    }

}
