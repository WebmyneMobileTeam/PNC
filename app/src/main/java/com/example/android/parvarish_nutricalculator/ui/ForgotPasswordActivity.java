package com.example.android.parvarish_nutricalculator.ui;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.helpers.API;
import com.example.android.parvarish_nutricalculator.helpers.EnumType;
import com.example.android.parvarish_nutricalculator.helpers.GetPostClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ForgotPasswordActivity extends ActionBarActivity {

    private EditText etEmail;
    private TextView btnSubmit;
    ProgressDialog progressDialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("PARVARISH NUTRI CALCULATOR");
//            toolbar.setLogo(R.drawable.logo);
            setSupportActionBar(toolbar);

        }

        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        etEmail= (EditText) findViewById(R.id.etEmail);
        btnSubmit= (TextView) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                passwordRecoveryProcess();

            }
        });
    }

    private void passwordRecoveryProcess() {

        progressDialog =new ProgressDialog(ForgotPasswordActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("email",etEmail.getText().toString()));

        new GetPostClass(API.FORGOT_PASSWORD,pairs, EnumType.POST) {

            @Override
            public void response(String response) {
                Toast.makeText(ForgotPasswordActivity.this, response, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void error(String error) {
                Toast.makeText(ForgotPasswordActivity.this,error,Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        }.call();
    }
}
