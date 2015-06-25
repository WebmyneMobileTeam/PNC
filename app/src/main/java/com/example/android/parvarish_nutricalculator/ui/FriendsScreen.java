package com.example.android.parvarish_nutricalculator.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.android.parvarish_nutricalculator.model.freindMainModel;
import com.example.android.parvarish_nutricalculator.model.freindsubModel;
import com.example.android.parvarish_nutricalculator.model.myrecipeModel;
import com.example.android.parvarish_nutricalculator.model.myrecipedata;
import com.example.android.parvarish_nutricalculator.model.userModel;
import com.example.android.parvarish_nutricalculator.ui.widgets.CustomDialogBoxAddFreind;
import com.example.android.parvarish_nutricalculator.ui.widgets.CustomDialogBoxEditBaby;
import com.example.android.parvarish_nutricalculator.ui.widgets.HUD;
import com.facebook.login.LoginManager;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FriendsScreen extends ActionBarActivity {
    private HUD progressDialog;
    private ListView friendList;
    private Toolbar toolbar;
    userModel currentUser;
    CustomAdapter adp;
    freindMainModel friendobj;
    EditText etSearchFreind;
    ImageView imgAddFreind,imgPendingReq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freinds_screen);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("PARVARISH NUTRI CALCULATOR");
//            toolbar.setLogo(R.drawable.logo);
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);


        init();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(FriendsScreen.this, "user_pref", 0);
        currentUser = complexPreferences.getObject("current-user", userModel.class);

        fetchFreindsScreen();



        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final CustomDialog customDialog = new CustomDialog(FriendsScreen.this, "See Friend's Feed", "Unfriend", android.R.style.Theme_Translucent_NoTitleBar);
                customDialog.show();
                customDialog.setResponse(new CustomDialog.CustomDialogInterface() {
                    @Override
                    public void topButton() {
                        Intent i = new Intent(FriendsScreen.this, FriendsFeedsScreen.class);
                        i.putExtra("userid",friendobj.data.get(position).FriendUser.id);
                        startActivity(i);
                    }

                    @Override
                    public void bottomButton() {
                        showalert("Are you sure want to delete this request ?",friendobj.data.get(position).Friend.id);
                        customDialog.dismiss();
                    }
                });
            }
        });


        imgAddFreind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogBoxAddFreind cdbox = new CustomDialogBoxAddFreind(FriendsScreen.this,currentUser.data.id);
                cdbox.show();
            }
        });

        imgPendingReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(FriendsScreen.this,FriendsPendingRequestScreen.class);
                startActivity(i);
            }
        });


        etSearchFreind.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etSearchFreind.getRight() - etSearchFreind.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        if (etSearchFreind.getText().toString().trim().length() == 0) {
                            Toast.makeText(FriendsScreen.this, "Please Enter any word for search !!!", Toast.LENGTH_SHORT).show();
                        } else {
                            adp.filter(etSearchFreind.getText().toString().trim());
                        }

                        return true;
                    }
                }
                return false;
            }

        });

    }


    void showalert(String msg,final String idd){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FriendsScreen.this);
        // set title
        //   alertDialogBuilder.setTitle(msg);

        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                            processRejectRequest(idd);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    void processRejectRequest(final String id){

        progressDialog =new HUD(FriendsScreen.this,android.R.style.Theme_Translucent_NoTitleBar);
        progressDialog.show();
        new GetPostClass(API.FRIENDS_REQUEST_STATUS_UPDATE+id+"&status=Rejected", EnumType.GET) {
            @Override
            public void response(String response) {
                progressDialog.dismiss();
                Log.e("freinds pending res", response);

                try {
                    Toast.makeText(FriendsScreen.this,"Friend request rejected sucessfully",Toast.LENGTH_LONG).show();


                }catch(Exception e){
                    Log.e("exc",e.toString());
                }

            }

            @Override
            public void error(String error) {
                progressDialog.dismiss();
                Toast.makeText(FriendsScreen.this, error, Toast.LENGTH_SHORT).show();
            }
        }.call();
    }

    private void fetchFreindsScreen(){
        progressDialog  =new HUD(FriendsScreen.this,android.R.style.Theme_Translucent_NoTitleBar);
        progressDialog.show();
        new GetPostClass(API.FRIENDS_LISTING+currentUser.data.id, EnumType.GET) {
            @Override
            public void response(String response) {
                progressDialog.dismiss();
                Log.e("my freinds response", response);

                try {
                    //  JSONObject jsonObject = new JSONObject(response.toString().trim());
                    friendobj = new GsonBuilder().create().fromJson(response, freindMainModel.class);
                    adp = new CustomAdapter(FriendsScreen.this,friendobj.data);
                    friendList.setAdapter(adp);


                }catch(Exception e){
                    Log.e("exc",e.toString());
                }

            }

            @Override
            public void error(String error) {
                progressDialog.dismiss();
                Toast.makeText(FriendsScreen.this, error, Toast.LENGTH_SHORT).show();
            }
        }.call();

    }

    private void init(){
        friendList = (ListView)findViewById(R.id.friendList);

        etSearchFreind = (EditText)findViewById(R.id.etSearchFreind);
        imgAddFreind = (ImageView)findViewById(R.id.imgAddFreind);
        imgPendingReq = (ImageView)findViewById(R.id.imgPendingReq);

        View emptyView = getLayoutInflater().inflate(R.layout.empty_myrecipe,null, false);
        friendList.setEmptyView(emptyView);




    }


    class CustomAdapter extends BaseAdapter{
        LayoutInflater layoutInflator;
        private Context ctx;

        List<freindsubModel> ValuesSearch;
        ArrayList<freindsubModel> arraylist;


        public CustomAdapter(Context ctx,ArrayList<freindsubModel> obj){
            this.ctx = ctx;
            this.ValuesSearch = obj;
            arraylist = new ArrayList<freindsubModel>();
            arraylist.addAll(ValuesSearch);
        }

        @Override
        public int getCount() {
            return ValuesSearch.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final  int position, View convertView, ViewGroup parent) {
            layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = convertView;
            view = layoutInflator.inflate(R.layout.friend_list_item_with_delete, parent, false);

            TextView txtName = (TextView)view.findViewById(R.id.txtName);
            ImageView imgDel = (ImageView)view.findViewById(R.id.imgDel);
            ImageView imgProfile= (ImageView)view.findViewById(R.id.imgProfile);

            txtName.setText(ValuesSearch.get(position).FriendUser.name);


            imgDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showalert("Are you sure want to delete this request ?", ValuesSearch.get(position).Friend.id);
                }
            });


            return view;
        }

        // Filter Class
        public void filter(String charText) {

            charText = charText.toLowerCase(Locale.getDefault());

            ValuesSearch.clear();
            if (charText.length() == 0) {
                ValuesSearch.addAll(arraylist);

            } else {
                for ( freindsubModel obj: arraylist) {
                    if (charText.length() != 0 && obj.FriendUser.name.toLowerCase(Locale.getDefault()).contains(charText)) {
                        ValuesSearch.add(obj);
                    }

                }
            }
            notifyDataSetChanged();
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

        String[] names = {"Settings","Rate Us on Play Store","Join Us on Facebook","Share this App with Friends","Disclaimers","About Us","Feedback","Logout"};
        int[] drawableImage = {R.drawable.icon_home,R.drawable.drawable_profile,R.drawable.drawable_myrecipes,R.drawable.drawable_diary,R.drawable.drawable_friends,R.drawable.icon_nutritional,R.drawable.icon_gloassary,R.drawable.drawable_tour};

        ListPopupWindow popupWindow = new ListPopupWindow(FriendsScreen.this);
        popupWindow.setAnchorView(menuSettings);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height =  getResources().getDisplayMetrics().heightPixels;

        popupWindow.setWidth((int)(width/1.5));
        popupWindow.setHeight((int) (height / 1.5));
        popupWindow.setModal(true);
        popupWindow.setAdapter(new SettingsAdapter(FriendsScreen.this,arrayList,drawableImage,true));
        popupWindow.show();
    }

    private void openMore() {

        View menuItemView = findViewById(R.id.actionMore); // SAME ID AS MENU ID
        String[] names = {"Home","Profile","My Recipes","Diary","Friends","Nutritional Guidelines","Glossary of Ingredients","Welcome Tour"};
        int[] drawableImage = {R.drawable.icon_home,R.drawable.drawable_profile,R.drawable.drawable_myrecipes,R.drawable.drawable_diary,R.drawable.drawable_friends,R.drawable.icon_nutritional,R.drawable.icon_gloassary,R.drawable.drawable_tour};
        ListPopupWindow popupWindow = new ListPopupWindow(FriendsScreen.this);

        popupWindow.setListSelector(new ColorDrawable());
        popupWindow.setAnchorView(menuItemView);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height =  getResources().getDisplayMetrics().heightPixels;

        popupWindow.setWidth((int)(width/1.5));
        popupWindow.setHeight((int)(height/1.5));
        popupWindow.setModal(true);
        popupWindow.setAdapter(new MoreAdapter(FriendsScreen.this,arrayList,drawableImage,false));
        popupWindow.show();


    }

    private void logoutFromApp() {

        Log.e("click", "logout");
        PrefUtils.clearCurrentUser(FriendsScreen.this);
        LoginManager.getInstance().logOut();
        Intent i= new Intent(FriendsScreen.this,StartScreen.class);
        startActivity(i);
        finish();
    }

    public  class SettingsAdapter extends ArrayAdapter<String> {

        // View lookup cache
        private ArrayList<String> users;
        private int[] imgIcons;
        private boolean isSettings;
        Context ctx;
        private  class ViewHolder {
            TextView name;
            TextView home;
        }

        public SettingsAdapter(Context context, ArrayList<String> users, int[] img, boolean value) {
            super(context, R.layout.item_popup, users);
            this.users = users;
            this.ctx = context;
            this.imgIcons = img;
            this.isSettings=value;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Get the data item for this position

            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_popup,parent,false);

                TextView itemNames = (TextView)convertView.findViewById(R.id.txtItemName);
                ImageView imgIcon = (ImageView)convertView.findViewById(R.id.imgIcon);

                if(isSettings){
                    itemNames.setText(users.get(position));
                    int col = Color.parseColor("#D13B3D");
                    imgIcon.setColorFilter(col, PorterDuff.Mode.SRC_ATOP);
                    imgIcon.setImageResource(R.drawable.iconsettings);
                    if(position!=0) {
                        imgIcon.setVisibility(View.INVISIBLE);
                    }
                }else{
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
                            Intent i = new Intent(FriendsScreen.this,DisclaimerScreen.class);
                            startActivity(i);
                            break;
                        case 5:
                            Intent i2 = new Intent(FriendsScreen.this,AboutusScreen.class);
                            startActivity(i2);
                            break;
                        case 7:
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

    public  class MoreAdapter extends ArrayAdapter<String> {

        // View lookup cache
        private ArrayList<String> users;
        private int[] imgIcons;
        private boolean isSettings;
        Context ctx;

        private  class ViewHolder {
            TextView name;
            TextView home;
        }

        public MoreAdapter(Context context, ArrayList<String> users, int[] img, boolean value) {
            super(context, R.layout.item_popup, users);
            this.users = users;
            this.ctx = context;
            this.imgIcons = img;
            this.isSettings=value;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Get the data item for this position

            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_popup,parent,false);

                TextView itemNames = (TextView)convertView.findViewById(R.id.txtItemName);
                ImageView imgIcon = (ImageView)convertView.findViewById(R.id.imgIcon);

                if(isSettings){

                    itemNames.setText(users.get(position));
                    int col = Color.parseColor("#D13B3D");
                    imgIcon.setColorFilter(col, PorterDuff.Mode.SRC_ATOP);
                    imgIcon.setImageResource(R.drawable.iconsettings);
                    if(position!=0) {
                        imgIcon.setVisibility(View.INVISIBLE);
                    }
                }else{

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

                    switch (position){

                        case 5:
                            Intent iGuide = new Intent(FriendsScreen.this,GuideLinesMainScreen.class);
                            startActivity(iGuide);
                            break;
                        case 6:
                            Intent i = new Intent(FriendsScreen.this,GlossaryScreen.class);
                            startActivity(i);
                            break;
                    }
                }
            });

            return convertView;
        }
    }

    //end of main class
}
