package com.example.android.parvarish_nutricalculator.ui.widgets;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.helpers.API;
import com.example.android.parvarish_nutricalculator.helpers.EnumType;
import com.example.android.parvarish_nutricalculator.helpers.GetPostClass;
import com.example.android.parvarish_nutricalculator.ui.HomeScreen;
import com.example.android.parvarish_nutricalculator.ui.ProfileScreen;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Android on 27-04-2015.
 */
public class CustomDialogBoxEditBaby extends Dialog  implements
        View.OnClickListener{
    private ProgressDialog progressDialog;
    public Activity act;
    public Dialog d;
    public Button yes;

    public String babyName;
    public String babyDOB;
    EditText edBabyName;
    EditText edBabyDOB;
    String userId,babyID;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    public CustomDialogBoxEditBaby(Activity context,String name,String dob,String uid,String bid) {
        super(context);
        this.act = context;
        this.babyName = name;
        this.babyDOB = dob;
        this.userId=uid;
        this.babyID=bid;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.baby_item);

        dateFormatter = new SimpleDateFormat("yyyy-dd-MM", Locale.US);

         edBabyName =  (EditText)findViewById(R.id.edBabyName);
         edBabyDOB =  (EditText)findViewById(R.id.edBabyDOB);

        edBabyName.setText(babyName);
        edBabyDOB.setText(babyDOB);

        yes = (Button) findViewById(R.id.btnAddBaby);
        yes.setText("Edit details");

        yes.setOnClickListener(this);
        edBabyDOB.setFocusable(false);
        edBabyDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar newCalendar = Calendar.getInstance();

                fromDatePickerDialog = new DatePickerDialog(act, new DatePickerDialog.OnDateSetListener() {

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnAddBaby:
                    if(isEmptyField(edBabyName)){
                        Toast.makeText(act,"Please Enter baby name !!!",Toast.LENGTH_SHORT).show();
                    }else if(isEmptyField(edBabyDOB)){
                        Toast.makeText(act,"Please Enter baby DOB !!!",Toast.LENGTH_SHORT).show();
                    }else {
                        processEditBaby();
                    }
                break;
            default:
                break;
        }
        dismiss();

    }


    private void processEditBaby(){
        List<NameValuePair> pairs = new ArrayList<>();

        pairs.add(new BasicNameValuePair("baby_id",babyID));
        pairs.add(new BasicNameValuePair("user_id",userId));
        pairs.add(new BasicNameValuePair("baby_name", edBabyName.getText().toString().trim()));
        pairs.add(new BasicNameValuePair("baby_dob",edBabyDOB.getText().toString().trim()));
        pairs.add(new BasicNameValuePair("photo_url",""));


        progressDialog =new ProgressDialog(act);
        progressDialog.setMessage("Updating Baby details...");
        progressDialog.show();
        new GetPostClass(API.UPDATE_BABY,pairs, EnumType.POST) {
            @Override
            public void response(String response) {
                progressDialog.dismiss();

                Log.e("update baby response", response);

                Toast.makeText(act,"Update Sucessfully",Toast.LENGTH_SHORT).show();

                Intent refresh = new Intent(act, ProfileScreen.class);
                act.startActivity(refresh);//Start the same Activity
                act.finish(); //finish Activity.

                    Log.e("sucness", "update baby");

            }
            @Override
            public void error(String error) {
                progressDialog.dismiss();
                Toast.makeText(act,error,Toast.LENGTH_SHORT).show();
            }
        }.call();

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
