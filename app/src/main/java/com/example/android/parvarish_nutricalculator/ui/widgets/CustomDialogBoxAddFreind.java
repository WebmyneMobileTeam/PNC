package com.example.android.parvarish_nutricalculator.ui.widgets;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Patterns;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.helpers.API;
import com.example.android.parvarish_nutricalculator.helpers.EnumType;
import com.example.android.parvarish_nutricalculator.helpers.GetPostClass;
import com.example.android.parvarish_nutricalculator.helpers.JSONPost;
import com.example.android.parvarish_nutricalculator.helpers.POSTResponseListener;
import com.example.android.parvarish_nutricalculator.model.Model;
import com.example.android.parvarish_nutricalculator.model.myrecipedata;
import com.example.android.parvarish_nutricalculator.ui.ProfileScreen;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
    public Button yes,btnShareLink;
    ListView listView;
    private String UserId;

    ArrayAdapter<String> adapter;
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

        listView = (ListView)findViewById(R.id.listView);
        yes = (Button) findViewById(R.id.btnSendReq);

        btnShareLink = (Button) findViewById(R.id.btnShareLink);
        btnShareLink.setOnClickListener(this);
        yes.setOnClickListener(this);


        final ArrayList<String> EmailAcc = proessFetchEmailContacts();

         adapter = new ArrayAdapter<String>(act,android.R.layout.simple_list_item_multiple_choice,EmailAcc);

        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);




    }



    public ArrayList<String> proessFetchEmailContacts(){
        ArrayList<String> names = new ArrayList<String>();
        ContentResolver cr = act.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cur1 = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (cur1.moveToNext()) {
                    //to get the contact names
                    String name=cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                   // Log.e("Name :", name);
                    String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    //Log.e("Email", email);
                    if(email!=null){
                        names.add(email);
                    }
                }
                cur1.close();
            }
        }
        return names;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSendReq:

                SparseBooleanArray checked = listView.getCheckedItemPositions();

                ArrayList<String> FINAL_EMAIL = new ArrayList<String>();
                for (int i = 0; i < checked.size(); i++) {
                    // Item position in adapter
                    int position = checked.keyAt(i);
                    // Add sport if it is checked i.e.) == TRUE!
                    if (checked.valueAt(i))
                        FINAL_EMAIL.add(adapter.getItem(position));
                }

                Log.e("@@@@ List size -",""+FINAL_EMAIL.size());
                for(int i=0;i<FINAL_EMAIL.size();i++) {
                    Log.e("#### Name-", FINAL_EMAIL.get(i));

                }

                    if( FINAL_EMAIL.size()==0){
                        showToast("Please Select at least one email address !!!");
                    }else {
                        processFreindInvite(FINAL_EMAIL);
                    }


                break;

            case R.id.btnShareLink:

                String text = "Please Check out this Parvairsh Nutri Calculator app for Baby Food , \n https://play.google.com/store/apps/details?id=com.ibee.parvarish";

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
                act.startActivity(Intent.createChooser(sharingIntent, "Share using"));

                break;
            default:
                break;
        }
       // dismiss();

    }












     void showToast(String msg){
        Toast.makeText(act, msg, Toast.LENGTH_SHORT).show();
    }

    void processFreindInvite(final ArrayList<String> EMAIL_ADDRESS){
        try{

            JSONObject userJSONObject = new JSONObject();

            userJSONObject.put("user_id", UserId);
            userJSONObject.put("type", "Test");

            JSONArray array = new JSONArray();

            for(int i=0;i<EMAIL_ADDRESS.size();i++)
            array.put(EMAIL_ADDRESS.get(i));

            userJSONObject.put("friend_email",array);

            Log.e("@@@# freind req",userJSONObject.toString());
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
