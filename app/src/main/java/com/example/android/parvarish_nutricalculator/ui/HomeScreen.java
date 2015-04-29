package com.example.android.parvarish_nutricalculator.ui;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.custom.CustomDialog;
import com.example.android.parvarish_nutricalculator.helpers.PrefUtils;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeScreen extends ActionBarActivity {

    private RelativeLayout relTopProfile;
    private String[] names1 = {"My Recipes", "Profiles", "Add Recipe"};
    private String[] names2 = {"Diary", "Friends", "Welcome Tour"};
    private int[] icons1 = {R.drawable.main_myrecpie, R.drawable.main_profile, R.drawable.main_addrecpie};
    private int[] icons2 = {R.drawable.main_diary, R.drawable.main_friends, R.drawable.main_tour};
    private LinearLayout linearFirst;
    private LinearLayout linearSecond;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        if(PrefUtils.getCurrentUser(HomeScreen.this) != null){
            Toast.makeText(HomeScreen.this, "welcome " + PrefUtils.getCurrentUser(HomeScreen.this).name, Toast.LENGTH_LONG).show();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("HOME");
//            toolbar.setLogo(R.drawable.logo);
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);

       setupButtons();
    }

    private View.OnClickListener firstLayoutItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch ((int)v.getTag()){
                case 0:
                    Intent i = new Intent(HomeScreen.this,MyRecipeListScreen.class);
                    startActivity(i);
                    break;
                case 1:
                    Intent i1 = new Intent(HomeScreen.this,ProfileScreen.class);
                    startActivity(i1);
                    break;
                case 2:

                    CustomDialog customDialog=new CustomDialog(HomeScreen.this,"Add Recipe from Web","Enter Recipe Manually",android.R.style.Theme_Translucent_NoTitleBar);
                    customDialog.show();
                    customDialog.setResponse(new CustomDialog.CustomDialogInterface() {
                        @Override
                        public void topButton() {

                            Intent i = new Intent(HomeScreen.this,AddRecipeWebScreen.class);
                            startActivity(i);
                        }

                        @Override
                        public void bottomButton() {
                            Intent i = new Intent(HomeScreen.this,AddRecipeManualScreen.class);
                            startActivity(i);

                        }
                    });
                    break;

            }

        }
    };

    private View.OnClickListener secondLayoutItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch ((int)v.getTag()){
                case 0:
                    Intent i1 = new Intent(HomeScreen.this,DiaryScreen.class);
                    startActivity(i1);
                    break;
                case 1:
                    Intent i = new Intent(HomeScreen.this,FriendsScreen.class);
                    startActivity(i);
                    break;
                case 2:
                    break;

            }
        }
    };

    private void setupButtons() {
        relTopProfile = (RelativeLayout)findViewById(R.id.relTopProfile);

        relTopProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreen.this,ProfileScreen.class);
                startActivity(i);
            }
        });

        linearFirst = (LinearLayout)findViewById(R.id.firstRow);
        linearSecond = (LinearLayout)findViewById(R.id.secondRow);

        for(int i=0;i<linearFirst.getChildCount();i++){

            ViewGroup vg = (ViewGroup)linearFirst.getChildAt(i);
            vg.setTag(i);
            vg.setOnClickListener(firstLayoutItemClickListener);
            ImageView img = (ImageView)vg.findViewById(R.id.itemImageHome);
            TextView txt = (TextView)vg.findViewById(R.id.itemTextHome);
            img.setImageResource(icons1[i]);
            txt.setText(names1[i]);
        }

        for(int i=0;i<linearSecond.getChildCount();i++){

            ViewGroup vg = (ViewGroup)linearSecond.getChildAt(i);
            vg.setTag(i);
            vg.setOnClickListener(secondLayoutItemClickListener);
            ImageView img = (ImageView)vg.findViewById(R.id.itemImageHome);
            TextView txt = (TextView)vg.findViewById(R.id.itemTextHome);
            img.setImageResource(icons2[i]);
            txt.setText(names2[i]);
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

        ListPopupWindow popupWindow = new ListPopupWindow(HomeScreen.this);

        popupWindow.setAnchorView(menuSettings);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        popupWindow.setWidth(500);
        popupWindow.setModal(true);
        popupWindow.setAdapter(new SettingsAdapter(HomeScreen.this,arrayList,drawableImage,true));
        popupWindow.show();



    }

    private void openMore() {

        View menuItemView = findViewById(R.id.actionMore); // SAME ID AS MENU ID

        String[] names = {"Home","Profile","My Recipes","Diary","Friends","Nutritional Guidelines","Glossary of Ingredients","Welcome Tour"};

        int[] drawableImage = {R.drawable.icon_home,R.drawable.drawable_profile,R.drawable.drawable_myrecipes,R.drawable.drawable_diary,R.drawable.drawable_friends,R.drawable.icon_nutritional,R.drawable.icon_gloassary,R.drawable.drawable_tour};
        ListPopupWindow popupWindow = new ListPopupWindow(HomeScreen.this);

        popupWindow.setAnchorView(menuItemView);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));
        popupWindow.setWidth(500);
        popupWindow.setModal(true);
        popupWindow.setAdapter(new MoreAdapter(HomeScreen.this,arrayList,drawableImage,false));
        popupWindow.show();


    }

    private void logoutFromApp() {

        Log.e("click", "logout");
        PrefUtils.clearCurrentUser(HomeScreen.this);
        LoginManager.getInstance().logOut();
        Intent i= new Intent(HomeScreen.this,StartScreen.class);
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
                            Intent i = new Intent(HomeScreen.this,DisclaimerScreen.class);
                            startActivity(i);
                            break;
                        case 5:
                            Intent i2 = new Intent(HomeScreen.this,AboutusScreen.class);
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
                            break;
                        case 6:
                            Intent i = new Intent(HomeScreen.this,GlossaryScreen.class);
                            startActivity(i);

                            break;
                    }
                }
            });

            return convertView;
        }
    }

}
