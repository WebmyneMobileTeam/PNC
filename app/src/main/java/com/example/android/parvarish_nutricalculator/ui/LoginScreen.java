package com.example.android.parvarish_nutricalculator.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.custom.ComplexPreferences;
import com.example.android.parvarish_nutricalculator.helpers.API;
import com.example.android.parvarish_nutricalculator.helpers.EnumType;
import com.example.android.parvarish_nutricalculator.helpers.GetPostClass;
import com.example.android.parvarish_nutricalculator.helpers.PrefUtils;
import com.example.android.parvarish_nutricalculator.helpers.User;
import com.example.android.parvarish_nutricalculator.model.userModel;
import com.example.android.parvarish_nutricalculator.ui.widgets.HUD;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.GsonBuilder;

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
    private HUD progressDialog;

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
/*

    private View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent iHome = new Intent(LoginScreen.this,HomeScreen.class);
            iHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(iHome);
            finish();
        }
    };
*/

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
                            userModel userUserModel = new userModel();
                            try {

                            //    registrationProcess(object.getString("id").toString(),object.getString("email").toString(), object.getString("name").toString());

                                userUserModel.data.fb_id = object.getString("id").toString();
                                userUserModel.data.email = object.getString("email").toString();
                                userUserModel.data.name = object.getString("name").toString();
                                userUserModel.data.gender = object.getString("gender").toString();
                                PrefUtils.setCurrentUser(userUserModel,LoginScreen.this);

                            }catch (Exception e){
                                Log.e("### EXC",e.toString());
                                e.printStackTrace();
                            }




                            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(LoginScreen.this, "user_pref", 0);
                            complexPreferences.putObject("current-user", userUserModel);
                            complexPreferences.commit();

                            SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("isUserLogin", true);
                            editor.commit();
//                            progressDialog.dismiss();


                            SharedPreferences preferences1 = getSharedPreferences("firstTime", MODE_PRIVATE);
                            boolean isFristTime = preferences1.getBoolean("isFirstTime", true);

                            if(isFristTime){
                                Intent intent = new Intent(LoginScreen.this, WalkThorugh.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }else {
                                Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
        //    callRegistrationProcess(object.getString("email").toString(), object.getString("name").toString(), AppConstants.TYPE_FB);
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


    private void registrationProcess(String fbID,String email,String name) {

        final ProgressDialog progressDialog = new ProgressDialog(LoginScreen.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("email", email));
        pairs.add(new BasicNameValuePair("name", name));
        pairs.add(new BasicNameValuePair("dob", "2015-07-09"));
        pairs.add(new BasicNameValuePair("password", "123"));
        pairs.add(new BasicNameValuePair("city", ""));
        pairs.add(new BasicNameValuePair("mobile", ""));
        pairs.add(new BasicNameValuePair("gender", "Male"));
        pairs.add(new BasicNameValuePair("profile_pic", ""));
        pairs.add(new BasicNameValuePair("fb_id", fbID));
        pairs.add(new BasicNameValuePair("fb_email", email));

        new GetPostClass(API.REGISTRATION, pairs, EnumType.POST) {
            @Override
            public void response(String response) {
//                    Toast.makeText(SignupScreen.this,response,Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                Log.e("##### RES--",response);

                try {

                    JSONObject jsonObject = new JSONObject(response.toString().trim());
                    userModel userUserModel = new GsonBuilder().create().fromJson(response, userModel.class);

                    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(LoginScreen.this, "user_pref", 0);
                    complexPreferences.putObject("current-user", userUserModel);
                    complexPreferences.commit();
                } catch (Exception e) {
                    Log.e("exc", "in json parsing");
                }


                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isUserLogin", true);
                editor.commit();

                SharedPreferences preferences1 = getSharedPreferences("firstTime", MODE_PRIVATE);
                boolean isFristTime = preferences1.getBoolean("isFirstTime", true);

                if (isFristTime) {
                    Intent intent = new Intent(LoginScreen.this, WalkThorugh.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void error(String error) {
                Toast.makeText(LoginScreen.this, error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }.call();

    }




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


            progressDialog =new HUD(LoginScreen.this,android.R.style.Theme_Translucent_NoTitleBar);
            progressDialog.show();

           new GetPostClass(API.LOGIN,pairs,EnumType.POST) {
                @Override
                public void response(String response) {
                    progressDialog.dismiss();

                    Log.e("login response",response);

                    try {
                        JSONObject jsonObject = new JSONObject(response.toString().trim());

                        userModel userUserModel = new GsonBuilder().create().fromJson(response, userModel.class);

                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(LoginScreen.this, "user_pref", 0);
                        complexPreferences.putObject("current-user", userUserModel);
                        complexPreferences.commit();

                        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("isUserLogin", true);
                        editor.commit();

                        Log.e("sucness", "saved current user");

                        Log.e("id",""+ userUserModel.data.id);

                    }catch(Exception e){
                        Log.e("excption s",e.toString());
                    }



                    //Toast.makeText(LoginScreen.this,response,Toast.LENGTH_SHORT).show();

                    SharedPreferences preferences1 = getSharedPreferences("firstTime", MODE_PRIVATE);
                    boolean isFristTime = preferences1.getBoolean("isFirstTime", true);

                    if(isFristTime){
                        Intent intent = new Intent(LoginScreen.this, WalkThorugh.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }

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
