package com.example.android.parvarish_nutricalculator.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Base64DataException;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.custom.ComplexPreferences;
import com.example.android.parvarish_nutricalculator.custom.CustomDialog;
import com.example.android.parvarish_nutricalculator.helpers.API;
import com.example.android.parvarish_nutricalculator.helpers.EnumType;
import com.example.android.parvarish_nutricalculator.helpers.GetPostClass;
import com.example.android.parvarish_nutricalculator.helpers.PrefUtils;
import com.example.android.parvarish_nutricalculator.model.babyModel;
import com.example.android.parvarish_nutricalculator.model.fbData;
import com.example.android.parvarish_nutricalculator.model.userModel;
import com.example.android.parvarish_nutricalculator.ui.widgets.CustomDialogBoxEditBaby;
import com.example.android.parvarish_nutricalculator.ui.widgets.CustomDialogBoxGlossary;
import com.example.android.parvarish_nutricalculator.ui.widgets.HUD;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.GsonBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import me.drakeet.library.UIButton;


public class ProfileScreen extends ActionBarActivity implements View.OnClickListener{
    private static final int CAMERA_REQUEST = 500;
    private static final int GALLERY_REQUEST = 300;
    final CharSequence[] items = { "Take Photo", "Choose from Gallery" };
    private Button btnSave,btnChangePassword;
    private EditText edSignUpCity,edSignUpMobile,edSignUpEmail,edSignUpPassword,edSignUpUserName;
    private ImageView imgProfile;
    private HUD progressDialog,progressDialog2;
    private ListView profileList;
    private Toolbar toolbar;
    userModel currentUser;
    userModel userProfile;
    UIButton btnLoginFacebook;
    String currentDate;
    babyModel cuurentBaby;
    Bitmap thumbnail,babyThumbnail;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    LinearLayout addBabyLinearMain;
    View babyView;
    int  pos=0;
    boolean isMainProfileImage = false;
    boolean isBabyProfileImage = false;
    String currBabyId,currBabyName,currBabyDOB,currUserID;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    //Facebook
    private String arr_permissions[]={"public_profile","user_friends", "email"};
    ListPopupWindow popupWindow1,popupWindow2;
    fbData fbModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("PARVARISH NUTRI CALCULATOR");
//            toolbar.setLogo(R.drawable.logo);
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);

        init();


        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ProfileScreen.this, "user_pref", 0);
        currentUser = complexPreferences.getObject("current-user", userModel.class);


        processfetchProfileDetails();

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileScreen.this);
                builder.setTitle("Upload Picture");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo")) {
                            isMainProfileImage = true;
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, CAMERA_REQUEST);
                            Log.e("Camera ", "exit");

                        } else if (items[item].equals("Choose from Gallery")) {
                            isMainProfileImage = true;
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, GALLERY_REQUEST);
                        }
                    }
                });
                builder.show();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {

                if(isMainProfileImage) {
                    isMainProfileImage = true;
                    thumbnail = (Bitmap) data.getExtras().get("data");
                    imgProfile.setImageBitmap(thumbnail);
                }else {
                           isBabyProfileImage = true;
                           final View v = addBabyLinearMain.getChildAt(cuurentBaby.data.size());
                           ImageView imgBabyProfile = (ImageView) v.findViewById(R.id.imgBabyProfile);
                           babyThumbnail = (Bitmap) data.getExtras().get("data");
                           imgBabyProfile.setImageBitmap(babyThumbnail);

                }

            }
        }else if(requestCode == GALLERY_REQUEST){



            // Get the Image from data
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgDecodableString = cursor.getString(columnIndex);
            cursor.close();


            if (resultCode == RESULT_OK) {
                if(isMainProfileImage) {
                    isMainProfileImage = true;
                    thumbnail = (Bitmap) data.getExtras().get("data");
                    imgProfile.setImageBitmap(thumbnail);
                }else {
                    isBabyProfileImage = true;
                    final View v = addBabyLinearMain.getChildAt(cuurentBaby.data.size());
                    ImageView imgBabyProfile = (ImageView) v.findViewById(R.id.imgBabyProfile);
                    babyThumbnail = (Bitmap) data.getExtras().get("data");
                    imgBabyProfile.setImageBitmap(babyThumbnail);

                }
            }
        }


    }

    private void init(){

       btnLoginFacebook = (UIButton)findViewById(R.id.btnLoginFacebook);

        imgProfile = (ImageView)findViewById(R.id.imgProfile);

        btnSave = (Button)findViewById(R.id.btnSave);
        btnChangePassword = (Button)findViewById(R.id.btnChangePassword);

        btnSave.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);

        edSignUpCity = (EditText)findViewById(R.id.edSignUpCity);
        edSignUpMobile = (EditText)findViewById(R.id.edSignUpMobile);
        edSignUpEmail = (EditText)findViewById(R.id.edSignUpEmail);
        edSignUpPassword = (EditText)findViewById(R.id.edSignUpPassword);
        edSignUpUserName = (EditText)findViewById(R.id.edSignUpUserName);

        addBabyLinearMain = (LinearLayout)findViewById(R.id.addBabyLinearMain);




        callbackManager=CallbackManager.Factory.create();

        loginButton= (LoginButton)findViewById(R.id.login_button);

        loginButton.setReadPermissions(arr_permissions);



        btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
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


    private void processfetchBabyDetails(){

        progressDialog  =new HUD(ProfileScreen.this,android.R.style.Theme_Translucent_NoTitleBar);
        progressDialog.show();
        //API.GET_BABY_DETAILS+currentUser.data.id
        new GetPostClass(API.GET_BABY_DETAILS+currentUser.data.id,EnumType.GET) {
            @Override
            public void response(String response) {
                progressDialog.dismiss();
                Log.e("baby response", response);

                try {
                  //  JSONObject jsonObject = new JSONObject(response.toString().trim());
                    cuurentBaby = new GsonBuilder().create().fromJson(response, babyModel.class);

                    Log.e("baby size",""+cuurentBaby.data.size());

                    addBabyViews();

                }catch(Exception e){
                    Log.e("exc",e.toString());
                }

            }

            @Override
            public void error(String error) {
                progressDialog.dismiss();
                Toast.makeText(ProfileScreen.this, error, Toast.LENGTH_SHORT).show();
            }
        }.call();


    }

    private void addBabyViews(){

        int babySize = cuurentBaby.data.size();
        if(babySize==0){
            View newbabyView = ((LayoutInflater) ProfileScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.baby_item, addBabyLinearMain, false);
            addBabyLinearMain.addView(newbabyView,0);
        }else{
            for(int i=0;i<babySize;i++){
              View   newbabyView = ((LayoutInflater) ProfileScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.baby_item, addBabyLinearMain, false);
              addBabyLinearMain.addView(newbabyView,i);

            }
            // add extra baby view for adding new babay
            View newbabyView = ((LayoutInflater) ProfileScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.baby_item, addBabyLinearMain, false);
            addBabyLinearMain.addView(newbabyView,babySize);


        }
        fillBabyDetails();

    }

    private void fillBabyDetails(){

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        int babySize = cuurentBaby.data.size();

        for(int i=0;i<=babySize;i++){

            final View v = addBabyLinearMain.getChildAt(i);


            final EditText edBabyName =  (EditText)v.findViewById(R.id.edBabyName);
            final EditText edBabyDOB =  (EditText)v.findViewById(R.id.edBabyDOB);
            Button btnAddBaby =  (Button)v.findViewById(R.id.btnAddBaby);
            ImageView imgBabyProfile =  (ImageView)v.findViewById(R.id.imgBabyProfile);




            // this IF for adding new baby
            if(i == babySize){

                edBabyDOB.setFocusable(false);
                edBabyDOB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v1) {
                        Calendar newCalendar = Calendar.getInstance();

                        fromDatePickerDialog = new DatePickerDialog(ProfileScreen.this, new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, monthOfYear, dayOfMonth);
                                edBabyDOB.setText(dateFormatter.format(newDate.getTime()));
                            }

                        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                        fromDatePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                        fromDatePickerDialog.show();
                    }
                });




                imgBabyProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileScreen.this);
                        builder.setTitle("Upload Picture");
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if (items[item].equals("Take Photo")) {
                                    isBabyProfileImage = true;
                                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(takePicture, CAMERA_REQUEST);
                                    Log.e("Camera ", "exit");

                                } else if (items[item].equals("Choose from Gallery")) {
                                    isBabyProfileImage = true;
                                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(pickPhoto, GALLERY_REQUEST);
                                }
                            }
                        });
                        builder.show();
                    }
                });

                btnAddBaby.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //   v.setOnClickListener(myAddBabyClick);
                        processAddbaby(edBabyName.getText().toString(), edBabyDOB.getText().toString());
                    }
                });

            }else{

                edBabyName.setText(cuurentBaby.data.get(i).Baby.baby_name);
                edBabyDOB.setText(cuurentBaby.data.get(i).Baby.baby_dob);

                edBabyName.setEnabled(false);
                edBabyDOB.setEnabled(false);
                btnAddBaby.setVisibility(View.GONE);
                v.setOnClickListener(myBabyClick);



            }
        }


    }



private View.OnClickListener myBabyClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int pos = addBabyLinearMain.indexOfChild(v);

            currBabyId = cuurentBaby.data.get(pos).Baby.id;
            currBabyName =cuurentBaby.data.get(pos).Baby.baby_name;
            currBabyDOB =cuurentBaby.data.get(pos).Baby.baby_dob;
            currUserID = cuurentBaby.data.get(pos).Baby.user_id;

            CustomDialog customDialog = new CustomDialog(ProfileScreen.this, "Edit Baby Details", "Delete Baby Details", android.R.style.Theme_Translucent_NoTitleBar);
            customDialog.show();
            customDialog.setResponse(new CustomDialog.CustomDialogInterface() {
                @Override
                public void topButton() {
                    processShowEditBabyDialog(currBabyName,currBabyDOB,currUserID,currBabyId);
                }

                @Override
                public void bottomButton() {
                    processShowDeleteBabyDialog(currBabyId);
                }
            });

        }
    };

    private void processShowEditBabyDialog(String babyName,String babyDOB,String uid,String bid){
        CustomDialogBoxEditBaby cdbox = new CustomDialogBoxEditBaby(ProfileScreen.this,babyName,babyDOB,uid,bid);
        cdbox.show();
    }

private void processShowDeleteBabyDialog(String bid){

        progressDialog =new HUD(ProfileScreen.this,android.R.style.Theme_Translucent_NoTitleBar);
        progressDialog.show();
        //API.GET_BABY_DETAILS+currentUser.data.id
        new GetPostClass(API.DELETE_BABY+bid,EnumType.GET) {
            @Override
            public void response(String response) {
                progressDialog.dismiss();
                Log.e("baby response", response);

                Toast.makeText(ProfileScreen.this,"Delete Sucessfully",Toast.LENGTH_SHORT).show();

                Intent refresh = new Intent(ProfileScreen.this, ProfileScreen.class);
                startActivity(refresh);//Start the same Activity
                finish(); //finish Activity.


            }

            @Override
            public void error(String error) {
                progressDialog.dismiss();
                Toast.makeText(ProfileScreen.this, error, Toast.LENGTH_SHORT).show();
            }
        }.call();

    }

private void processAddbaby(String edBabyName,String edBabyDOB){
    if(isEmptyField2(edBabyName)){
        Toast.makeText(ProfileScreen.this,"Please Enter baby name !!!",Toast.LENGTH_SHORT).show();
    }else if(isEmptyField2(edBabyDOB)){
        Toast.makeText(ProfileScreen.this,"Please Enter baby DOB !!!",Toast.LENGTH_SHORT).show();
    }else {
        processAdd(edBabyName,edBabyDOB);
    }

}

private void processAdd(String edBabyName,String edBabyDOB){
        List<NameValuePair> pairs = new ArrayList<>();

         Log.e("dob", edBabyDOB);

        pairs.add(new BasicNameValuePair("user_id", currentUser.data.id));
        pairs.add(new BasicNameValuePair("baby_name",edBabyName));
        pairs.add(new BasicNameValuePair("baby_dob",edBabyDOB));


    if(isBabyProfileImage) {
        String base64Image = PrefUtils.returnBas64Image(babyThumbnail);
        pairs.add(new BasicNameValuePair("photo_url", base64Image));
    }else{
        pairs.add(new BasicNameValuePair("photo_url", ""));
    }


         Log.e("request",""+ pairs.toString());

        progressDialog2 =new HUD(ProfileScreen.this,android.R.style.Theme_Translucent_NoTitleBar);
        progressDialog2.show();
        new GetPostClass(API.ADD_BABY,pairs,EnumType.POST) {
            @Override
            public void response(String response) {
                progressDialog2.dismiss();

                Log.e("baby response", response);

                Toast.makeText(ProfileScreen.this,"Baby details added Sucessfully",Toast.LENGTH_SHORT).show();

                Intent refresh = new Intent(ProfileScreen.this, ProfileScreen.class);
                startActivity(refresh);//Start the same Activity
                finish(); //finish Activity.
            }
            @Override
            public void error(String error) {
                progressDialog2.dismiss();
                Toast.makeText(ProfileScreen.this,error,Toast.LENGTH_SHORT).show();
            }
        }.call();

    }

private void processfetchProfileDetails(){


        progressDialog =new HUD(ProfileScreen.this,android.R.style.Theme_Translucent_NoTitleBar);
        progressDialog.show();

         new GetPostClass(API.GET_PROFILE+currentUser.data.id,EnumType.GET) {
                @Override
                public void response(String response) {
                    progressDialog.dismiss();
                    Log.e("profile response", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response.toString().trim());
                        userProfile = new GsonBuilder().create().fromJson(response, userModel.class);


                        Log.e("sucness",userProfile.data.city);
                        Log.e("sucness",userProfile.data.email);
                        Log.e("sucness",userProfile.data.mobile);


                        fillDetails();

                        processfetchBabyDetails();


                        Log.e("sucness","saved profile");

                    }catch(Exception e){
                        Log.e("exc",e.toString());
                    }

                }

                @Override
                public void error(String error) {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileScreen.this, error, Toast.LENGTH_SHORT).show();
                }
            }.call();


    }

    private void fillDetails(){
        edSignUpCity.setText(userProfile.data.city);
        edSignUpMobile.setText(userProfile.data.mobile);
        edSignUpEmail.setText(userProfile.data.email);
        edSignUpPassword.setText(userProfile.data.password);
        edSignUpUserName.setText(userProfile.data.name);

        try {

            byte[] decodedString = Base64.decode(userProfile.data.profile_pic, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgProfile.setImageBitmap(decodedByte);
        }catch(Exception e){
            Log.e("#### exc",e.toString());
        }
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

                            Log.e("response FB object: ", object.toString() + "");

                            try {

                                fbModel = new fbData();

                                fbModel.fb_id = object.getString("id").toString();
                                fbModel.fb_email = object.getString("email").toString();

                                Log.e("#### fb email ",object.getString("email").toString());
                                fbModel.name = object.getString("name").toString();
                                fbModel.gender = object.getString("gender").toString();


                            }catch (Exception e){
                              Log.e("#### EXC",e.toString());
                                e.printStackTrace();
                            }



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
            case R.id.btnSave:
                processValidateProfileData();
                break;
            case R.id.btnChangePassword:
                break;
        }
    }

    private void processValidateProfileData(){

        if(isEmptyField(edSignUpUserName)){
                Toast.makeText(ProfileScreen.this,"Please Enter User Name !!!",Toast.LENGTH_SHORT).show();
        }else if(isEmptyField(edSignUpPassword)){
            Toast.makeText(ProfileScreen.this,"Please Enter User Password !!!",Toast.LENGTH_SHORT).show();
        }else if(isEmptyField(edSignUpEmail)){
            Toast.makeText(ProfileScreen.this,"Please Enter User Email !!!",Toast.LENGTH_SHORT).show();
        }else if(isEmptyField(edSignUpMobile)){
            Toast.makeText(ProfileScreen.this,"Please Enter User Mobile no. !!!",Toast.LENGTH_SHORT).show();
        }else if(isEmptyField(edSignUpCity)){
            Toast.makeText(ProfileScreen.this,"Please Enter City !!!",Toast.LENGTH_SHORT).show();
        }/*else if(!isPasswordMatch(edSignUpPassword)){
            Toast.makeText(ProfileScreen.this,"Please Enter Correct Password !!!",Toast.LENGTH_SHORT).show();
        }*/else if(!isEmailMatch(edSignUpEmail)){
            Toast.makeText(ProfileScreen.this,"Invalid Email address !!!",Toast.LENGTH_SHORT).show();
        }else{
            processUpdateProfile();
        }

    }


    private void processUpdateProfile(){





        List<NameValuePair> pairs = new ArrayList<>();

        pairs.add(new BasicNameValuePair("user_id",currentUser.data.id));
        pairs.add(new BasicNameValuePair("email",edSignUpEmail.getText().toString().trim()));
        pairs.add(new BasicNameValuePair("name", edSignUpUserName.getText().toString().trim()));

        pairs.add(new BasicNameValuePair("dob",currentUser.data.dob));
        pairs.add(new BasicNameValuePair("password",edSignUpPassword.getText().toString().trim()));
        pairs.add(new BasicNameValuePair("city", edSignUpCity.getText().toString().trim()));


        pairs.add(new BasicNameValuePair("mobile", edSignUpMobile.getText().toString().trim()));
        pairs.add(new BasicNameValuePair("gender",currentUser.data.gender));

        if(isMainProfileImage) {

            //complete code to save image on server
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String  encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            Log.e("### BASE64 STRING ",encodedImage);


            String base64Image = PrefUtils.returnBas64Image(thumbnail);
            pairs.add(new BasicNameValuePair("profile_pic", encodedImage));
        }else{
            pairs.add(new BasicNameValuePair("profile_pic", currentUser.data.profile_pic));
        }



        try {
            // Setting the facebook parameters
            if (fbModel.fb_id == null) {
                pairs.add(new BasicNameValuePair("fb_id", currentUser.data.fb_id));
                pairs.add(new BasicNameValuePair("fb_email", currentUser.data.fb_email));

            } else {
                pairs.add(new BasicNameValuePair("fb_id",fbModel.fb_id));
                pairs.add(new BasicNameValuePair("fb_email", fbModel.fb_email));
            }
        }catch (Exception e){
            Log.e("~~~~Exce in FB",e.toString());
            // If the facebook parameters are null
            pairs.add(new BasicNameValuePair("fb_id", currentUser.data.fb_id));
            pairs.add(new BasicNameValuePair("fb_email", currentUser.data.fb_email));

        }



        progressDialog2 =new HUD(ProfileScreen.this,android.R.style.Theme_Translucent_NoTitleBar);
        progressDialog2.show();
        new GetPostClass(API.UPDATE_PROFILE,pairs,EnumType.POST) {
            @Override
            public void response(String response) {
                progressDialog2.dismiss();

                Log.e("login response", response);

                Toast.makeText(ProfileScreen.this,"Update Sucessfully",Toast.LENGTH_SHORT).show();

                try {
                    JSONObject jsonObject = new JSONObject(response.toString().trim());

                    userModel userUserModel = new GsonBuilder().create().fromJson(response, userModel.class);

                    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ProfileScreen.this, "user_pref", 0);
                    complexPreferences.putObject("current-user", userUserModel);
                    complexPreferences.commit();



                 //   userModel userUserModel = new GsonBuilder().create().fromJson(response, userModel.class);

                  /*  ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ProfileScreen.this, "user_pref", 0);
                    complexPreferences.putObject("current-user", userUserModel);
                    complexPreferences.commit();
*/
                    Log.e("sucness", "update profile");


                }catch(Exception e){
                    Log.e("excption s",e.toString());
                }



//                    Toast.makeText(LoginScreen.this,response,Toast.LENGTH_SHORT).show();
                Intent i=new Intent(ProfileScreen.this,HomeScreen.class);
                startActivity(i);
            }
            @Override
            public void error(String error) {
                progressDialog2.dismiss();
                Toast.makeText(ProfileScreen.this,error,Toast.LENGTH_SHORT).show();
            }
        }.call();

    }


    public boolean isEmptyField2(String param1) {

        boolean isEmpty = false;
        if (param1.toString() == null || param1.toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }
        return isEmpty;
    }


    public boolean isEmptyField(EditText param1) {

        boolean isEmpty = false;
        if (param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }
        return isEmpty;
    }

    public boolean isEmailMatch(EditText param1) {
        // boolean isMatch = false;
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(param1.getText().toString()).matches();
    }

    public boolean isPasswordMatch(EditText param1) {
        if(currentUser.data.password.equals(param1)){
            return true;
        }else{
            return false;
        }

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.actionMore:
                openMore();
                break;
            case R.id.actionSettings:
                openSettings();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void openSettings() {

        View menuSettings = findViewById(R.id.actionSettings); // SAME ID AS MENU ID
        String[] names = {"Settings", "Rate Us on Play Store", "Join Us on Facebook", "Share this App with Friends", "Disclaimers", "About Us", "Feedback", "Logout"};
        int[] drawableImage = {R.drawable.icon_home, R.drawable.drawable_profile, R.drawable.drawable_myrecipes, R.drawable.drawable_diary, R.drawable.drawable_friends, R.drawable.icon_nutritional, R.drawable.icon_gloassary, R.drawable.drawable_tour};
        popupWindow1 = new ListPopupWindow(ProfileScreen.this);
        popupWindow1.setAnchorView(menuSettings);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        popupWindow1.setWidth((int) (width / 1.5));
        popupWindow1.setHeight((int) (height / 1.5));
        popupWindow1.setModal(true);
        popupWindow1.setAdapter(new SettingsAdapter(ProfileScreen.this, arrayList, drawableImage,true));
        popupWindow1.show();
    }

    private void openMore() {

        View menuItemView = findViewById(R.id.actionMore); // SAME ID AS MENU ID
        String[] names = {"Home", "Profile", "My Recipes", "Diary", "Friends", "Nutritional Guidelines", "Glossary of Ingredients", "Welcome Tour"};
        int[] drawableImage = {R.drawable.icon_home, R.drawable.drawable_profile, R.drawable.drawable_myrecipes, R.drawable.drawable_diary, R.drawable.drawable_friends, R.drawable.icon_nutritional, R.drawable.icon_gloassary, R.drawable.drawable_tour};
        popupWindow2 = new ListPopupWindow(ProfileScreen.this);

        popupWindow2.setListSelector(new ColorDrawable());
        popupWindow2.setAnchorView(menuItemView);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        popupWindow2.setWidth((int) (width / 1.5));
        popupWindow2.setHeight((int) (height / 1.5));
        popupWindow2.setModal(true);
        popupWindow2.setAdapter(new MoreAdapter(ProfileScreen.this, arrayList, drawableImage, false));
        popupWindow2.show();


    }

    private void logoutFromApp() {

        Log.e("click", "logout");
        PrefUtils.clearCurrentUser(ProfileScreen.this);
        LoginManager.getInstance().logOut();


        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isUserLogin", false);
        editor.commit();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ProfileScreen.this, "user_pref", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
        Intent i = new Intent(ProfileScreen.this, StartScreen.class);
        startActivity(i);
        finish();
    }

    public class SettingsAdapter extends ArrayAdapter<String> {

        // View lookup cache
        private ArrayList<String> users;
        private int[] imgIcons;
        private boolean isSettings;
        Context ctx;

        private class ViewHolder {
            TextView name;
            TextView home;
        }

        public SettingsAdapter(Context context, ArrayList<String> users, int[] img, boolean value) {
            super(context, R.layout.item_popup, users);
            this.users = users;
            this.ctx = context;
            this.imgIcons = img;
            this.isSettings = value;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Get the data item for this position

            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_popup, parent, false);

                TextView itemNames = (TextView) convertView.findViewById(R.id.txtItemName);
                ImageView imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);

                if (isSettings) {
                    itemNames.setText(users.get(position));
                    int col = Color.parseColor("#D13B3D");
                    imgIcon.setColorFilter(col, PorterDuff.Mode.SRC_ATOP);
                    imgIcon.setImageResource(R.drawable.iconsettings);
                    if (position != 0) {
                        imgIcon.setVisibility(View.INVISIBLE);
                    }
                } else {
                    itemNames.setText(users.get(position));
                    imgIcon.setImageResource(imgIcons[position]);
                }
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (position) {
                        case 4:
                            popupWindow1.dismiss();
                            Intent i = new Intent(ProfileScreen.this, DisclaimerScreen.class);
                            startActivity(i);
                            break;
                        case 5:
                            popupWindow1.dismiss();
                            Intent i2 = new Intent(ProfileScreen.this, AboutusScreen.class);
                            startActivity(i2);
                            break;
                        case 7:
                            popupWindow1.dismiss();
                            logoutFromApp();
                            break;
                    }
                }
            });

            // Populate the data into the template view using the data object
            // Return the completed view to render on screen
            return convertView;
        }
    }

    public class MoreAdapter extends ArrayAdapter<String> {

        // View lookup cache
        private ArrayList<String> users;
        private int[] imgIcons;
        private boolean isSettings;
        Context ctx;

        private class ViewHolder {
            TextView name;
            TextView home;
        }

        public MoreAdapter(Context context, ArrayList<String> users, int[] img, boolean value) {
            super(context, R.layout.item_popup, users);
            this.users = users;
            this.ctx = context;
            this.imgIcons = img;
            this.isSettings = value;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Get the data item for this position

            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_popup, parent, false);

                TextView itemNames = (TextView) convertView.findViewById(R.id.txtItemName);
                ImageView imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);

                if (isSettings) {

                    itemNames.setText(users.get(position));
                    int col = Color.parseColor("#D13B3D");
                    imgIcon.setColorFilter(col, PorterDuff.Mode.SRC_ATOP);
                    imgIcon.setImageResource(R.drawable.iconsettings);
                    if (position != 0) {
                        imgIcon.setVisibility(View.INVISIBLE);
                    }
                } else {

                    itemNames.setText(users.get(position));
                    imgIcon.setImageResource(imgIcons[position]);
                }

                convertView.setTag(viewHolder);

            } else {

                viewHolder = (ViewHolder) convertView.getTag();
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (position) {

                        case 5:
                            popupWindow2.dismiss();
                            Intent iGuide = new Intent(ProfileScreen.this, GuideLinesMainScreen.class);
                            startActivity(iGuide);
                            break;
                        case 6:
                            popupWindow2.dismiss();
                            Intent i = new Intent(ProfileScreen.this, GlossaryScreenTemp.class);
                            startActivity(i);
                            break;
                        case 7:
                            Intent intent = new Intent(ProfileScreen.this, WalkThorugh.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            break;
                    }
                }
            });

            return convertView;
        }
    }


    //end of main class
}
