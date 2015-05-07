package com.example.android.parvarish_nutricalculator.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.helpers.API;
import com.example.android.parvarish_nutricalculator.helpers.EnumType;
import com.example.android.parvarish_nutricalculator.helpers.GetPostClass;
import com.example.android.parvarish_nutricalculator.helpers.PrefUtils;
import com.example.android.parvarish_nutricalculator.helpers.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.library.UIButton;

public class LoginScreen extends ActionBarActivity implements View.OnClickListener{

    private EditText edUserName;
    private EditText edPassword;
    private Button btnForgotPassword;
    private Button btnLogin;
    private UIButton btnFacebookLogin;
    private ProgressDialog progressDialog;

//    private ProgressDialog progressDialog;
    User user;
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        if(PrefUtils.getCurrentUser(LoginScreen.this) != null){
            Intent homeIntent = new Intent(LoginScreen.this, HomeScreen.class);
            startActivity(homeIntent);
            finish();
        }

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
        //btnLogin.setOnClickListener(loginClick);
        btnLogin.setOnClickListener(this);
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginScreen.this,ForgotPasswordActivity.class);
                startActivity(i);
            }
        });
    }

    private View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent iHome = new Intent(LoginScreen.this,HomeScreen.class);
            startActivity(iHome);
            finish();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent iHome = new Intent(LoginScreen.this,StartScreen.class);
        startActivity(iHome);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        callbackManager=CallbackManager.Factory.create();
        loginButton= (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "email","user_friends");
        btnFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                progressDialog = new ProgressDialog(LoginScreen.this);
//                progressDialog.setMessage("Loading...");
//                progressDialog.show();

                loginButton.performClick();
                loginButton.setPressed(true);
                loginButton.invalidate();
                loginButton.registerCallback(callbackManager, mCallBack);
                loginButton.setPressed(false);
                loginButton.invalidate();



            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            Log.e("response: ", response + "");
                            try {
                                user = new User();
                                user.facebookID = object.getString("id").toString();
                                user.email = object.getString("email").toString();
                                user.name = object.getString("name").toString();
                                user.gender = object.getString("gender").toString();
                                PrefUtils.setCurrentUser(user,LoginScreen.this);

                            }catch (Exception e){
                                e.printStackTrace();
                            }
//                            progressDialog.dismiss();
                            Intent intent=new Intent(LoginScreen.this,HomeScreen.class);
                            startActivity(intent);
                            finish();
//                                callRegistrationProcess(object.getString("email").toString(), object.getString("name").toString(), AppConstants.TYPE_FB);
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();

        }

        @Override
        public void onCancel() {

//            progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {

//            progressDialog.dismiss();
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnLogin:

                loginProcess();
                break;

        }
    }

    private void loginProcess() {

        if(passValidationProcess()){

            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("email",edUserName.getText().toString()));
            pairs.add(new BasicNameValuePair("fb_id",""));
            pairs.add(new BasicNameValuePair("password", edPassword.getText().toString()));

            progressDialog =new ProgressDialog(LoginScreen.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            new GetPostClass(API.LOGIN,pairs,EnumType.POST) {
                @Override
                public void response(String response) {
                    progressDialog.dismiss();
//                    Toast.makeText(LoginScreen.this,response,Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(LoginScreen.this,HomeScreen.class);
                    startActivity(i);
                }
                @Override
                public void error(String error) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginScreen.this,error,Toast.LENGTH_SHORT).show();
                }
            }.call();

        }else{
            Toast.makeText(LoginScreen.this,"Enter credentials",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean passValidationProcess(){
        boolean ispassed = false;

        if(edUserName.getText().toString().isEmpty() || edPassword.getText().toString().isEmpty()){
            ispassed = false;
        }else{
            ispassed = true;
        }
        return ispassed;

    }

}
