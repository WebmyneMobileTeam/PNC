package com.example.android.parvarish_nutricalculator.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.helpers.PrefUtils;

import me.drakeet.library.UIButton;

public class SignupScreen extends ActionBarActivity {

    private UIButton btnSignUpWithFacebook;
    private TextView txtStaticFacebookText;
    private EditText edUserName;
    private EditText edPassword;
    private EditText edEmail;
    private EditText edMobile;
    private EditText edCity;
    private UIButton btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);
        setupUI();
    }

    private void setupUI() {

         btnSignUpWithFacebook = (UIButton)findViewById(R.id.btnSignUpFacebook);
         txtStaticFacebookText = (TextView)findViewById(R.id.staticText);
         edUserName = (EditText)findViewById(R.id.edSignUpUserName);
         edPassword = (EditText)findViewById(R.id.edSignUpPassword);
         edEmail = (EditText)findViewById(R.id.edSignUpEmail);
         edMobile = (EditText)findViewById(R.id.edSignUpMobile);
         edCity = (EditText)findViewById(R.id.edSignUpCity);
         btnSignUp = (UIButton)findViewById(R.id.btnSignUp);

        btnSignUpWithFacebook.setTypeface(PrefUtils.getTypeFace(SignupScreen.this));
        txtStaticFacebookText.setTypeface(PrefUtils.getTypeFace(SignupScreen.this));
        edUserName.setTypeface(PrefUtils.getTypeFace(SignupScreen.this));
        edPassword.setTypeface(PrefUtils.getTypeFace(SignupScreen.this));
        edEmail.setTypeface(PrefUtils.getTypeFace(SignupScreen.this));
        edMobile.setTypeface(PrefUtils.getTypeFace(SignupScreen.this));
        edCity.setTypeface(PrefUtils.getTypeFace(SignupScreen.this));
        btnSignUp.setTypeface(PrefUtils.getTypeFace(SignupScreen.this));




    }

}
