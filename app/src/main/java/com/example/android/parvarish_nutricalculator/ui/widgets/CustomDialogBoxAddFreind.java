package com.example.android.parvarish_nutricalculator.ui.widgets;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.helpers.API;
import com.example.android.parvarish_nutricalculator.helpers.EnumType;
import com.example.android.parvarish_nutricalculator.helpers.GetPostClass;
import com.example.android.parvarish_nutricalculator.helpers.JSONPost;
import com.example.android.parvarish_nutricalculator.helpers.POSTResponseListener;
import com.example.android.parvarish_nutricalculator.ui.ProfileScreen;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Android on 27-04-2015.
 */
public class CustomDialogBoxAddFreind extends Dialog  implements
        View.OnClickListener{
    private ProgressDialog progressDialog;
    public Activity act;
    public Dialog d;
    public Button yes;

    EditText edEmail;
    private String UserId;
    public CustomDialogBoxAddFreind(Activity context,String uid ) {
        super(context);
        this.act = context;
        this.UserId=uid;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_friend_item);

        edEmail =  (EditText)findViewById(R.id.edEmail);
        yes = (Button) findViewById(R.id.btnSendReq);

        yes.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSendReq:
                    if(isEmptyField(edEmail)){
                        showToast("Please Enter email address !!!");
                    }else if(!isEmailMatch(edEmail)){
                        showToast("Please Enter valid Email");
                    }else {
                        processFreindInvite();
                    }
                break;
            default:
                break;
        }
       // dismiss();

    }

     void showToast(String msg){
        Toast.makeText(act, msg, Toast.LENGTH_SHORT).show();
    }

    void processFreindInvite(){
        try{

            JSONObject userJSONObject = new JSONObject();

            userJSONObject.put("user_id", UserId);
            userJSONObject.put("type", "Test");

            JSONArray array = new JSONArray();
            array.put(edEmail.getText().toString().trim());
            userJSONObject.put("friend_email",array);

            Log.e("req",userJSONObject.toString());
            JSONPost json = new JSONPost();
            json.POST(act, API.FRIENDS_INVITE, userJSONObject.toString(),"Sending Request...");
            json.setPostResponseListener(new POSTResponseListener() {
                @Override
                public String onPost(String msg) {

                    Log.e("add freinf req", "onPost response: " + msg);

                    Toast.makeText(act,"Freind Request Sent Succesfully",Toast.LENGTH_SHORT).show();
                    dismiss();

                    return null;
                }

                @Override
                public void onPreExecute() {

                }

                @Override
                public void onBackground() {

                }
            });


        }catch (Exception e){
            Log.e("Exception",e.toString());
        }
    }


    public boolean isEmailMatch(EditText param1) {
        // boolean isMatch = false;
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(param1.getText().toString()).matches();
    }

    public boolean isEmptyField(EditText param1) {

        boolean isEmpty = false;
        if (param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }
        return isEmpty;
    }

    //end of main class
}
