package com.example.android.parvarish_nutricalculator.ui;

import android.content.Context;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.custom.ComplexPreferences;
import com.example.android.parvarish_nutricalculator.helpers.API;
import com.example.android.parvarish_nutricalculator.helpers.PrefUtils;
import com.example.android.parvarish_nutricalculator.model.sanjeevmainModel;
import com.example.android.parvarish_nutricalculator.ui.widgets.MyTableView;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.Arrays;

public class SanjeevKumarEditRecipeScreen extends ActionBarActivity {
    sanjeevmainModel sajneevObj;
    private LinearLayout linearTableDetails;
    private Toolbar toolbar;
    private TextView txtServing,txtAgeGroup,txtTitle,txtMethod;
    ImageView photoImg;
    int listPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sanjeev_recipie);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("PARVARISH NUTRI CALCULATOR");
//            toolbar.setLogo(R.drawable.logo);
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);


        Log.e("Pos ",""+listPos);
        listPos = getIntent().getIntExtra("pos",0);

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(SanjeevKumarEditRecipeScreen.this, "user_pref", 0);
        sajneevObj = complexPreferences.getObject("sanjeev-recipe", sanjeevmainModel.class);


        init();


        linearTableDetails = (LinearLayout)findViewById(R.id.linearTableFriendRecipeDetail);
        addTableView();


    }

    void init(){
        txtMethod = (TextView)findViewById(R.id.txtMethod);
        txtServing = (TextView)findViewById(R.id.txtServing);
        txtAgeGroup = (TextView)findViewById(R.id.txtAgeGroup);
        txtTitle = (TextView)findViewById(R.id.txtTitle);
        photoImg = (ImageView)findViewById(R.id.photoImg);

        txtTitle.setText(sajneevObj.data.get(listPos).Recipe.name);
        txtServing.setText("Servings : " + sajneevObj.data.get(listPos).Recipe.no_of_servings);
        txtAgeGroup.setText("Age of Baby : " + sajneevObj.data.get(listPos).Recipe.age_group);

        Glide.with(SanjeevKumarEditRecipeScreen.this).load(API.BASE_URL_IMAGE_FETCH +sajneevObj.data.get(listPos).Recipe.photo_url)
                .into(photoImg);

    }

    private void addTableView() {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        MyTableView tableView = new MyTableView(SanjeevKumarEditRecipeScreen.this);
        tableView.setPadding(8,8,8,8);

        // setting weights recommanded
        ArrayList<Float> weights = new ArrayList<>();
        weights.add(1f);
        weights.add(1f);
        weights.add(0.5f);
        tableView.setWeights(weights);

        linearTableDetails.addView(tableView, params);

        ArrayList<String> values = new ArrayList<>();
        values.add("Nutrients");
        values.add("ICMR Recommandation");
        values.add("Recipe Values");

        tableView.addRow(values, "#000000");
        tableView.addDivider();

        ArrayList<String> values2 = new ArrayList<>();
        values2.add("Energy");
        values2.add("6.733(Approx)");
        values2.add("174");


        tableView.addRow(values2, "#ffffff");
        tableView.addRow(values2, "#ffffff");
        tableView.addRow(values2, "#ffffff");
        tableView.addRow(values2, "#ffffff");
        tableView.addRow(values2, "#ffffff");
        tableView.addRow(values2, "#ffffff");

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

        ListPopupWindow popupWindow = new ListPopupWindow(SanjeevKumarEditRecipeScreen.this);
        popupWindow.setAnchorView(menuSettings);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height =  getResources().getDisplayMetrics().heightPixels;

        popupWindow.setWidth((int)(width/1.5));
        popupWindow.setHeight((int) (height / 1.5));
        popupWindow.setModal(true);
        popupWindow.setAdapter(new SettingsAdapter(SanjeevKumarEditRecipeScreen.this,arrayList,drawableImage,true));
        popupWindow.show();
    }

    private void openMore() {

        View menuItemView = findViewById(R.id.actionMore); // SAME ID AS MENU ID
        String[] names = {"Home","Profile","My Recipes","Diary","Friends","Nutritional Guidelines","Glossary of Ingredients","Welcome Tour"};
        int[] drawableImage = {R.drawable.icon_home,R.drawable.drawable_profile,R.drawable.drawable_myrecipes,R.drawable.drawable_diary,R.drawable.drawable_friends,R.drawable.icon_nutritional,R.drawable.icon_gloassary,R.drawable.drawable_tour};
        ListPopupWindow popupWindow = new ListPopupWindow(SanjeevKumarEditRecipeScreen.this);

        popupWindow.setListSelector(new ColorDrawable());
        popupWindow.setAnchorView(menuItemView);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height =  getResources().getDisplayMetrics().heightPixels;

        popupWindow.setWidth((int)(width/1.5));
        popupWindow.setHeight((int)(height/1.5));
        popupWindow.setModal(true);
        popupWindow.setAdapter(new MoreAdapter(SanjeevKumarEditRecipeScreen.this,arrayList,drawableImage,false));
        popupWindow.show();


    }

    private void logoutFromApp() {

        Log.e("click", "logout");
        PrefUtils.clearCurrentUser(SanjeevKumarEditRecipeScreen.this);
        LoginManager.getInstance().logOut();
        Intent i= new Intent(SanjeevKumarEditRecipeScreen.this,StartScreen.class);
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
                            Intent i = new Intent(SanjeevKumarEditRecipeScreen.this,DisclaimerScreen.class);
                            startActivity(i);
                            break;
                        case 5:
                            Intent i2 = new Intent(SanjeevKumarEditRecipeScreen.this,AboutusScreen.class);
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
                            Intent iGuide = new Intent(SanjeevKumarEditRecipeScreen.this,GuideLinesMainScreen.class);
                            startActivity(iGuide);
                            break;
                        case 6:
                            Intent i = new Intent(SanjeevKumarEditRecipeScreen.this,GlossaryScreen.class);
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
