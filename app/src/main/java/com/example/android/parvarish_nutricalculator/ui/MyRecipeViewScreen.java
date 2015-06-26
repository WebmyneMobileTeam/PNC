package com.example.android.parvarish_nutricalculator.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.custom.ComplexPreferences;
import com.example.android.parvarish_nutricalculator.helpers.API;
import com.example.android.parvarish_nutricalculator.helpers.EnumType;
import com.example.android.parvarish_nutricalculator.helpers.GetPostClass;
import com.example.android.parvarish_nutricalculator.helpers.NutritionCalculation;
import com.example.android.parvarish_nutricalculator.helpers.NutritionCalculationSingleMyRecipe;
import com.example.android.parvarish_nutricalculator.helpers.NutritionCalculationSingleRecipe;
import com.example.android.parvarish_nutricalculator.helpers.PrefUtils;
import com.example.android.parvarish_nutricalculator.model.POJOTableRow;
import com.example.android.parvarish_nutricalculator.model.glossaryDescription;
import com.example.android.parvarish_nutricalculator.model.glossaryIngredient;
import com.example.android.parvarish_nutricalculator.model.icmrMainModel;
import com.example.android.parvarish_nutricalculator.model.myrecipeModel;
import com.example.android.parvarish_nutricalculator.model.sanjeevmainModel;
import com.example.android.parvarish_nutricalculator.ui.widgets.MyTableView;
import com.facebook.login.LoginManager;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class MyRecipeViewScreen extends ActionBarActivity {
    myrecipeModel myrecipe;
    glossaryDescription ingdredient;
    icmrMainModel icmrOBJ;
    private LinearLayout linearTableDetails;
    private Toolbar toolbar;
    private TextView txtServing,txtAgeGroup,txtTitle,txtMethod,txtMeth,txtING;
    ImageView photoImg;
    int listPos;
    ProgressDialog progressDialog;
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

        listPos = getIntent().getIntExtra("listPos", 0);
        Log.e("Pos ",""+listPos);

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(MyRecipeViewScreen.this, "user_pref", 0);
        myrecipe = complexPreferences.getObject("current-myrecipe", myrecipeModel.class);


        init();


        linearTableDetails = (LinearLayout)findViewById(R.id.linearTableFriendRecipeDetail);

        fetchIngredientsdetails();

    }



    private void fetchIngredientsdetails(){

        progressDialog = new ProgressDialog(MyRecipeViewScreen.this);
        progressDialog.setMessage("Loading Details...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new GetPostClass(API.GLOSSARY_INGREDIENTS, EnumType.GET) {
            @Override
            public void response(String response) {
                // Log.e("ingridents response", response);
                progressDialog.dismiss();
                try {
                    //  JSONObject jsonObject = new JSONObject(response.toString().trim());/*
                    ingdredient = new GsonBuilder().create().fromJson(response, glossaryDescription.class);

                    String ing="";

                    ArrayList<glossaryIngredient> arr = ingdredient.returnAllIngredients();
                    for(int i = 0; i < myrecipe.data.Recipe.get(listPos).RecipeIngredientList.size(); i++) {
                        for (int j = 0; j < arr.size(); j++) {
                            if (myrecipe.data.Recipe.get(listPos).RecipeIngredientList.get(i).RecipeIngredient.id.equalsIgnoreCase(arr.get(j).id)) {
                                ing +=arr.get(i).name+"\n";
                            }
                        }
                    }

                     txtING.setText("\n"+Html.fromHtml(ing));

                }catch(Exception e){
                    Log.e("exc",e.toString());
                }

                fetchICMRdetails();


            }

            @Override
            public void error(String error) {
                progressDialog.dismiss();
                Toast.makeText(MyRecipeViewScreen.this, error, Toast.LENGTH_SHORT).show();
            }
        }.call();
    }


    private void fetchICMRdetails(){

        progressDialog = new ProgressDialog(MyRecipeViewScreen.this);
        progressDialog.setMessage("Loading Details...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new GetPostClass(API.FETCH_IMCR, EnumType.GET) {
            @Override
            public void response(String response) {
                Log.e("icmr response", response);
                progressDialog.dismiss();
                try {
                    //  JSONObject jsonObject = new JSONObject(response.toString().trim());/*
                    icmrOBJ = new GsonBuilder().create().fromJson(response, icmrMainModel.class);


                }catch(Exception e){
                    Log.e("exc",e.toString());
                }



                NutritionCalculationSingleMyRecipe executer = new NutritionCalculationSingleMyRecipe(MyRecipeViewScreen.this,myrecipe.data.Recipe.get(listPos).RecipeIngredientList,ingdredient,myrecipe.data.Recipe.get(listPos).no_of_servings);
                executer.startCalculation();

                executer.setOnCalculationResult(new NutritionCalculationSingleMyRecipe.OnCalculationResult() {
                    @Override
                    public void onResult(float energy, float protien, float fat, float calcium, float iron) {
                        Log.e("Result Energy ",""+energy);
                        Log.e("Result protien ",""+protien);
                        Log.e("Result fat ",""+fat);
                        Log.e("Result calcium ",""+calcium);
                        Log.e("Result iron ",""+iron);


                        addTableView(energy,protien,fat,calcium,iron);
                    }
                });




            }

            @Override
            public void error(String error) {
                progressDialog.dismiss();
                Toast.makeText(MyRecipeViewScreen.this, error, Toast.LENGTH_SHORT).show();
            }
        }.call();
    }


    void init(){

        txtMeth  = (TextView)findViewById(R.id.txtMeth);
        txtING  = (TextView)findViewById(R.id.txtING);

        txtMethod = (TextView)findViewById(R.id.txtMethod);
        txtServing = (TextView)findViewById(R.id.txtServing);
        txtAgeGroup  = (TextView)findViewById(R.id.txtAgeGroup);
        txtTitle = (TextView)findViewById(R.id.txtTitle);
        photoImg = (ImageView)findViewById(R.id.photoImg);

        txtTitle.setText(myrecipe.data.Recipe.get(listPos).name);
        txtServing.setText("Servings : " + myrecipe.data.Recipe.get(listPos).no_of_servings);
        txtAgeGroup.setText("Age of Baby : " + myrecipe.data.Recipe.get(listPos).age_group);

        Glide.with(MyRecipeViewScreen.this).load(API.BASE_URL_IMAGE_FETCH +myrecipe.data.Recipe.get(listPos).photo_url)
                .into(photoImg);


        txtMeth.setText(Html.fromHtml(myrecipe.data.Recipe.get(listPos).method));

    }

    private void addTableView(float energy, float protien, float fat, float calcium, float iron){

        int ICMR_GET_POS = 0;
        for(int i=0;i<icmrOBJ.data.size();i++){
            if(icmrOBJ.data.get(i).IcmrRecommended.age_group.trim().equalsIgnoreCase(myrecipe.data.Recipe.get(listPos).age_group)){
                ICMR_GET_POS = i;
                break;
            }
        }



        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        MyTableView tableView = new MyTableView(MyRecipeViewScreen.this);
        tableView.setPadding(8,8,8,8);

        // setting weights recommanded
        ArrayList<Float> weights = new ArrayList<>();
        weights.add(1f);
        weights.add(1f);
        weights.add(0.5f);
        tableView.setWeights(weights);

        linearTableDetails.addView(tableView, params);

        ArrayList<POJOTableRow> values = new ArrayList<>();
        values.add(new POJOTableRow("Nutrients",Color.BLACK));
        values.add(new POJOTableRow("ICMR Recommandation", Color.BLACK));
        values.add(new POJOTableRow("Recipe Values", Color.BLACK));

        tableView.addRow(values);

        tableView.addDivider();

        ArrayList<POJOTableRow> values2 = new ArrayList<>();
        values2.add(new POJOTableRow("Energy (kcal)",Color.WHITE));
        values2.add(new POJOTableRow(icmrOBJ.data.get(0).IcmrRecommended.energy.equalsIgnoreCase("")?"0":icmrOBJ.data.get(ICMR_GET_POS).IcmrRecommended.energy,Color.WHITE));
        values2.add(new POJOTableRow(String.format("%.2f",energy),Color.WHITE));



        ArrayList<POJOTableRow> values3 = new ArrayList<>();
        values3.add(new POJOTableRow("Protein (gm)",Color.WHITE));
        values3.add(new POJOTableRow((icmrOBJ.data.get(0).IcmrRecommended.protein.equalsIgnoreCase("")?"0":icmrOBJ.data.get(ICMR_GET_POS).IcmrRecommended.protein),Color.WHITE));
        values3.add(new POJOTableRow(String.format("%.2f",protien),Color.WHITE));


        ArrayList<POJOTableRow> values4 = new ArrayList<>();
        values4.add(new POJOTableRow("Fat (gm)",Color.WHITE));
        values4.add(new POJOTableRow((icmrOBJ.data.get(0).IcmrRecommended.fat.equalsIgnoreCase("")?"0":icmrOBJ.data.get(ICMR_GET_POS).IcmrRecommended.fat),Color.WHITE));
        values4.add(new POJOTableRow(String.format("%.2f",fat),Color.WHITE));


        ArrayList<POJOTableRow> values5 = new ArrayList<>();
        values5.add(new POJOTableRow("Calcium (mg)",Color.WHITE));
        values5.add(new POJOTableRow((icmrOBJ.data.get(0).IcmrRecommended.calcium.equalsIgnoreCase("")?"0":icmrOBJ.data.get(ICMR_GET_POS).IcmrRecommended.calcium),Color.WHITE));
        values5.add(new POJOTableRow(String.format("%.2f",calcium),Color.WHITE));


        ArrayList<POJOTableRow> values6 = new ArrayList<>();
        values6.add(new POJOTableRow("Iron (mg)",Color.WHITE));
        values6.add(new POJOTableRow((icmrOBJ.data.get(0).IcmrRecommended.iron.equalsIgnoreCase("") ? "0" : icmrOBJ.data.get(ICMR_GET_POS).IcmrRecommended.iron),Color.WHITE));
        values6.add(new POJOTableRow(String.format("%.2f", iron),Color.WHITE));


        tableView.addRow(values2);
        tableView.addRow(values3);
        tableView.addRow(values4);
        tableView.addRow(values5);
        tableView.addRow(values6);

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

        ListPopupWindow popupWindow = new ListPopupWindow(MyRecipeViewScreen.this);
        popupWindow.setAnchorView(menuSettings);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height =  getResources().getDisplayMetrics().heightPixels;

        popupWindow.setWidth((int)(width/1.5));
        popupWindow.setHeight((int) (height / 1.5));
        popupWindow.setModal(true);
        popupWindow.setAdapter(new SettingsAdapter(MyRecipeViewScreen.this,arrayList,drawableImage,true));
        popupWindow.show();
    }

    private void openMore() {

        View menuItemView = findViewById(R.id.actionMore); // SAME ID AS MENU ID
        String[] names = {"Home","Profile","My Recipes","Diary","Friends","Nutritional Guidelines","Glossary of Ingredients","Welcome Tour"};
        int[] drawableImage = {R.drawable.icon_home,R.drawable.drawable_profile,R.drawable.drawable_myrecipes,R.drawable.drawable_diary,R.drawable.drawable_friends,R.drawable.icon_nutritional,R.drawable.icon_gloassary,R.drawable.drawable_tour};
        ListPopupWindow popupWindow = new ListPopupWindow(MyRecipeViewScreen.this);

        popupWindow.setListSelector(new ColorDrawable());
        popupWindow.setAnchorView(menuItemView);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height =  getResources().getDisplayMetrics().heightPixels;

        popupWindow.setWidth((int)(width/1.5));
        popupWindow.setHeight((int)(height/1.5));
        popupWindow.setModal(true);
        popupWindow.setAdapter(new MoreAdapter(MyRecipeViewScreen.this,arrayList,drawableImage,false));
        popupWindow.show();


    }

    private void logoutFromApp() {

        Log.e("click", "logout");
        PrefUtils.clearCurrentUser(MyRecipeViewScreen.this);
        LoginManager.getInstance().logOut();
        Intent i= new Intent(MyRecipeViewScreen.this,StartScreen.class);
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
                            Intent i = new Intent(MyRecipeViewScreen.this,DisclaimerScreen.class);
                            startActivity(i);
                            break;
                        case 5:
                            Intent i2 = new Intent(MyRecipeViewScreen.this,AboutusScreen.class);
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
                            Intent iGuide = new Intent(MyRecipeViewScreen.this,GuideLinesMainScreen.class);
                            startActivity(iGuide);
                            break;
                        case 6:
                            Intent i = new Intent(MyRecipeViewScreen.this,GlossaryScreen.class);
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
