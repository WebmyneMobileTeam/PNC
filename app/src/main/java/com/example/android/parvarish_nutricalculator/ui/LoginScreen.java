package com.example.android.parvarish_nutricalculator.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.helpers.PrefUtils;

import me.drakeet.library.UIButton;

public class LoginScreen extends ActionBarActivity {

    private EditText edUserName;
    private EditText edPassword;
    private Button btnForgotPassword;
    private Button btnLogin;
    private UIButton btnFacebookLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        edPassword = (EditText) findViewById(R.id.edPasswordLogin);
        edUserName = (EditText) findViewById(R.id.edUserNameLogin);
        btnForgotPassword = (Button) findViewById(R.id.btnForgotPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnFacebookLogin = (UIButton) findViewById(R.id.btnLoginFacebook);
        edPassword.setTypeface(PrefUtils.getTypeFace(LoginScreen.this));
        edUserName.setTypeface(PrefUtils.getTypeFace(LoginScreen.this));
        btnForgotPassword.setTypeface(PrefUtils.getTypeFace(LoginScreen.this));
        btnLogin.setTypeface(PrefUtils.getTypeFace(LoginScreen.this));
        btnFacebookLogin.setTypeface(PrefUtils.getTypeFace(LoginScreen.this));
        btnLogin.setOnClickListener(loginClick);

    }

    private View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent iHome = new Intent(LoginScreen.this,HomeScreen.class);
            startActivity(iHome);
        }
    };

}
