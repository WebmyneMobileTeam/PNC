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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
    EditText edEmail;
    private String UserId;
    ArrayList<String> FINAL_EMAIL=new ArrayList<String>();

    List<Model> list = new ArrayList<Model>();

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
        edEmail =  (EditText)findViewById(R.id.edEmail);
        yes = (Button) findViewById(R.id.btnSendReq);

        btnShareLink = (Button) findViewById(R.id.btnShareLink);
        btnShareLink.setOnClickListener(this);
        yes.setOnClickListener(this);


        ArrayList<String> EmailAcc = proessFetchEmailContacts();

        CustomAdapter adp = new CustomAdapter(act,getModel(EmailAcc));
        listView.setAdapter(adp);

    }


    private List<Model> getModel(ArrayList<String> EmailAcc) {

        for(int i=0;i<EmailAcc.size();i++)
            list.add(new Model(EmailAcc.get(i)));

        return list;
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

                for(int i=0;i<FINAL_EMAIL.size();i++)
                  Log.e("#### Name-",FINAL_EMAIL.get(i));



                 /*   if(isEmptyField(edEmail)){
                        showToast("Please Enter email address !!!");
                    }else if(!isEmailMatch(edEmail)){
                        showToast("Please Enter valid Email");
                    }else {
                        processFreindInvite();
                    }*/
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

    class CustomAdapter extends BaseAdapter {
        LayoutInflater layoutInflator;
        private Context ctx;

        private final List<Model> list;
        public CustomAdapter(Context ctx,List<Model> obj){
            this.ctx = ctx;
            this.list = obj;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        public class Myholder{
            TextView txtEmail;
            CheckBox chkBox;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            Myholder holder;

            if(convertView == null) {
                holder = new Myholder();

                layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflator.inflate(R.layout.freinds_email_list_item, parent, false);

                holder.txtEmail = (TextView) convertView.findViewById(R.id.email);
                holder.chkBox = (CheckBox) convertView.findViewById(R.id.chkBox);


                holder.chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                        list.get(getPosition).setSelected(buttonView.isChecked()); // Set the value of checkbox to maintain its state.



                    }
                });

                convertView.setTag(holder);

                convertView.setTag(R.id.email, holder.txtEmail);
                convertView.setTag(R.id.chkBox, holder.chkBox);

            }else {
                holder = (Myholder)convertView.getTag();
            }

            holder.chkBox.setTag(position); // This line is important.

            holder.txtEmail.setText(list.get(position).getName());
            holder.chkBox.setChecked(list.get(position).isSelected());
            if(list.get(position).isSelected()){
                FINAL_EMAIL.add(list.get(position).getName());
            }

            return convertView;
        }



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
