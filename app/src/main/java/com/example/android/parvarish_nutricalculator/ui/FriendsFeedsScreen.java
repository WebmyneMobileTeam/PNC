package com.example.android.parvarish_nutricalculator.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
import com.example.android.parvarish_nutricalculator.model.freindFeedsMainModel;
import com.example.android.parvarish_nutricalculator.model.freindMainModel;
import com.example.android.parvarish_nutricalculator.model.freindfeedssubModel;
import com.example.android.parvarish_nutricalculator.model.freindsubModel;
import com.example.android.parvarish_nutricalculator.model.myrecipedata;
import com.example.android.parvarish_nutricalculator.model.recipeSubData;
import com.example.android.parvarish_nutricalculator.model.userModel;
import com.facebook.login.LoginManager;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FriendsFeedsScreen extends ActionBarActivity {
    ListPopupWindow popupWindow1,popupWindow2;
    private ProgressDialog progressDialog;
    userModel currentUser;
    private String userID;
    private ListView listFeeds;
    private Toolbar toolbar;
    freindFeedsMainModel frndfeedObj;
    CustomAdapter adp;
    TextView txtUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_feed);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("PARVARISH NUTRI CALCULATOR");
//            toolbar.setLogo(R.drawable.logo);
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);


        init();


        userID = getIntent().getStringExtra("userid");
        Log.e("###User Id",userID);

       // txtUserName.setText(currentUser.data.name);

        fetchFriendsFeeds();

        View headerView = ((LayoutInflater) FriendsFeedsScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.friends_feed_item_view_header, null, false);
        listFeeds.addHeaderView(headerView);

        listFeeds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(FriendsFeedsScreen.this, ViewFriendsRecipe.class);
                i.putExtra("pos",position);
                startActivity(i);
            }
        });
    }


    void init(){
        txtUserName = (TextView)findViewById(R.id.txtUserName);
        listFeeds = (ListView)findViewById(R.id.listFeeds);
        View emptyView = getLayoutInflater().inflate(R.layout.empty_myrecipe,null, false);
        listFeeds.setEmptyView(emptyView);
    }

    private void fetchFriendsFeeds(){
        progressDialog =new ProgressDialog(FriendsFeedsScreen.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        new GetPostClass(API.FRIENDS_FEEDS_LISTING+userID, EnumType.GET) {
            @Override
            public void response(String response) {
                progressDialog.dismiss();
                Log.e("frn feed res", response);

               try {
                    //  JSONObject jsonObject = new JSONObject(response.toString().trim());
                   frndfeedObj = new GsonBuilder().create().fromJson(response, freindFeedsMainModel.class);

                   ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(FriendsFeedsScreen.this, "user_pref", 0);
                   complexPreferences.putObject("current-frnd-recipe", frndfeedObj);
                   complexPreferences.commit();

                   txtUserName.setText(frndfeedObj.data.User.name);

                   adp = new CustomAdapter(FriendsFeedsScreen.this,frndfeedObj.data.Recipe);
                   listFeeds.setAdapter(adp);


                }catch(Exception e){
                    Log.e("exc",e.toString());
                }

            }

            @Override
            public void error(String error) {
                progressDialog.dismiss();
                Toast.makeText(FriendsFeedsScreen.this, error, Toast.LENGTH_SHORT).show();
            }
        }.call();
    }

    class CustomAdapter extends BaseAdapter{
        List<myrecipedata> ValuesSearch;
        ArrayList<myrecipedata> arraylist;
        LayoutInflater layoutInflator;
        private Context ctx;
        public CustomAdapter(Context ctx,ArrayList<myrecipedata> obj){
            this.ctx = ctx;
            this.ValuesSearch = obj;
            arraylist = new ArrayList<myrecipedata>();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = convertView;
            view = layoutInflator.inflate(R.layout.friend_feed_item_view, parent, false);

            TextView txtrecipeName = (TextView)view.findViewById(R.id.txtrecipeName);
            TextView txtBabyage = (TextView)view.findViewById(R.id.txtBabyage);

            txtrecipeName.setText(ValuesSearch.get(position).name);

            return view;
        }

        // Filter Class
        public void filter(String charText) {

            charText = charText.toLowerCase(Locale.getDefault());

            ValuesSearch.clear();
            if (charText.length() == 0) {
                ValuesSearch.addAll(arraylist);

            } else {
                for ( myrecipedata obj: arraylist) {
                    if (charText.length() != 0 && obj.name.toLowerCase(Locale.getDefault()).contains(charText)) {
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
        String[] names = {"Settings", "Rate Us on Play Store", "Join Us on Facebook", "Share this App with Friends", "Disclaimers", "About Us", "Feedback", "Logout"};
        int[] drawableImage = {R.drawable.icon_home, R.drawable.drawable_profile, R.drawable.drawable_myrecipes, R.drawable.drawable_diary, R.drawable.drawable_friends, R.drawable.icon_nutritional, R.drawable.icon_gloassary, R.drawable.drawable_tour};
        popupWindow1 = new ListPopupWindow(FriendsFeedsScreen.this);
        popupWindow1.setAnchorView(menuSettings);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        popupWindow1.setWidth((int) (width / 1.5));
        popupWindow1.setHeight((int) (height / 1.5));
        popupWindow1.setModal(true);
        popupWindow1.setAdapter(new SettingsAdapter(FriendsFeedsScreen.this, arrayList, drawableImage,true));
        popupWindow1.show();
    }

    private void openMore() {

        View menuItemView = findViewById(R.id.actionMore); // SAME ID AS MENU ID
        String[] names = {"Home", "Profile", "My Recipes", "Diary", "Friends", "Nutritional Guidelines", "Glossary of Ingredients", "Welcome Tour"};
        int[] drawableImage = {R.drawable.icon_home, R.drawable.drawable_profile, R.drawable.drawable_myrecipes, R.drawable.drawable_diary, R.drawable.drawable_friends, R.drawable.icon_nutritional, R.drawable.icon_gloassary, R.drawable.drawable_tour};
        popupWindow2 = new ListPopupWindow(FriendsFeedsScreen.this);

        popupWindow2.setListSelector(new ColorDrawable());
        popupWindow2.setAnchorView(menuItemView);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        popupWindow2.setWidth((int) (width / 1.5));
        popupWindow2.setHeight((int) (height / 1.5));
        popupWindow2.setModal(true);
        popupWindow2.setAdapter(new MoreAdapter(FriendsFeedsScreen.this, arrayList, drawableImage, false));
        popupWindow2.show();


    }

    private void logoutFromApp() {

        Log.e("click", "logout");
        PrefUtils.clearCurrentUser(FriendsFeedsScreen.this);
        LoginManager.getInstance().logOut();


        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isUserLogin", false);
        editor.commit();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(FriendsFeedsScreen.this, "user_pref", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
        Intent i = new Intent(FriendsFeedsScreen.this, StartScreen.class);
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
                            Intent i = new Intent(FriendsFeedsScreen.this, DisclaimerScreen.class);
                            startActivity(i);
                            break;
                        case 5:
                            popupWindow1.dismiss();
                            Intent i2 = new Intent(FriendsFeedsScreen.this, AboutusScreen.class);
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
                        case 0:
                            popupWindow2.dismiss();
                            Intent pro1 = new Intent(FriendsFeedsScreen.this, HomeScreen.class);
                            startActivity(pro1);
                            break;
                        case 1:
                            popupWindow2.dismiss();
                            Intent pro = new Intent(FriendsFeedsScreen.this, ProfileScreen.class);
                            startActivity(pro);
                            break;
                        case 2:
                            popupWindow2.dismiss();
                            Intent myrecipe = new Intent(FriendsFeedsScreen.this, MyRecipeListScreen.class);
                            startActivity(myrecipe);
                            break;
                        case 3:
                            popupWindow2.dismiss();
                            Intent diary = new Intent(FriendsFeedsScreen.this, DiaryScreen.class);
                            startActivity(diary);
                            break;

                        case 4:
                            popupWindow2.dismiss();
                            Intent iGuide1 = new Intent(FriendsFeedsScreen.this, FriendsScreen.class);
                            startActivity(iGuide1);
                            break;
                        case 5:
                            popupWindow2.dismiss();
                            Intent iGuide = new Intent(FriendsFeedsScreen.this, GuideLinesMainScreen.class);
                            startActivity(iGuide);
                            break;
                        case 6:
                            popupWindow2.dismiss();
                            Intent i = new Intent(FriendsFeedsScreen.this, GlossaryScreenTemp.class);
                            startActivity(i);
                            break;
                        case 7:
                            Intent intent = new Intent(FriendsFeedsScreen.this, WalkThorugh.class);
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
