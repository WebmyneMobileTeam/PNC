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

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.custom.ComplexPreferences;
import com.example.android.parvarish_nutricalculator.helpers.API;
import com.example.android.parvarish_nutricalculator.helpers.ConversionTable;
import com.example.android.parvarish_nutricalculator.helpers.EnumType;
import com.example.android.parvarish_nutricalculator.helpers.GetPostClass;
import com.example.android.parvarish_nutricalculator.helpers.NutritionCalculation;
import com.example.android.parvarish_nutricalculator.helpers.PrefUtils;
import com.example.android.parvarish_nutricalculator.model.diaryModel;
import com.example.android.parvarish_nutricalculator.model.glossaryDescription;
import com.example.android.parvarish_nutricalculator.model.recipeModel;
import com.example.android.parvarish_nutricalculator.ui.widgets.MyTableView;
import com.facebook.login.LoginManager;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class DiaryResult extends ActionBarActivity {
    ProgressDialog progressDialog,progressDialog2;
    diaryModel dm;
    private LinearLayout linearTableDetails,linearRecipeNames;
    private Toolbar toolbar;
    recipeModel myrecipe;
    glossaryDescription ingdredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_result);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("PARVARISH NUTRI CALCULATOR");
//            toolbar.setLogo(R.drawable.logo);
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(DiaryResult.this, "user_pref", 0);
        dm = complexPreferences.getObject("current-diary", diaryModel.class);


        init();
        addRecipeNames();
        callAsyncTaskForWebService();

    }

    void init(){
        linearRecipeNames = (LinearLayout)findViewById(R.id.linearRecipeNames);
        linearTableDetails = (LinearLayout)findViewById(R.id.linearTableFriendRecipeDetail);
    }

    void callAsyncTaskForWebService(){

        fetchIngredientsdetails();

    }

    void addRecipeNames(){

        for(int i=0;i<dm.diarysubModel.size();i++){
            View view = getLayoutInflater().inflate(R.layout.diaryresultitem, linearRecipeNames, false);
            TextView txtrecipeName = (TextView)view.findViewById(R.id.txtrecipeName);
            txtrecipeName.setText(dm.diarysubModel.get(i).recipeMainData.name);
            linearRecipeNames.addView(view, i);


            for(int j=0;j<dm.diarysubModel.get(i).recipeMainData.RecipeIngredientList.size();j++){
                Log.e("#### ING ID ",""+dm.diarysubModel.get(i).recipeMainData.RecipeIngredientList.get(j).RecipeIngredient.ingredient_id);
            }
        }

    }

    private void addTableView() {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

      //  CalcualateNutritionResult();

        MyTableView tableView = new MyTableView(DiaryResult.this);
        tableView.setPadding(8,8,8,8);

        // setting weights
        ArrayList<Float> weights = new ArrayList<>();
        weights.add(1f);
        weights.add(0.5f);
        weights.add(1f);
        weights.add(0.5f);

        tableView.setWeights(weights);

        linearTableDetails.addView(tableView, params);

        ArrayList<String> values = new ArrayList<>();
        values.add("Nutrients");
        values.add("Recipe Values");
        values.add("ICMR Recommandation");
        values.add("% ICMR Recommandation met");

        tableView.addRow(values, "#000000");
        tableView.addDivider();

        ArrayList<String> values2 = new ArrayList<>();
        values2.add("Energy (kcal)");
        values2.add("174");
        values2.add("672(Approx)");
        values2.add("97%");

        ArrayList<String> values3 = new ArrayList<>();
        values3.add("Protein (gm)");
        values3.add("14.9");
        values3.add("14.9 (Approx)");
        values3.add("97%");

        ArrayList<String> values4 = new ArrayList<>();
        values4.add("Fat (gm)");
        values4.add("174");
        values4.add("9.8");
        values4.add("97%");

        ArrayList<String> values5 = new ArrayList<>();
        values5.add("Calcium (mg)");
        values5.add("174");
        values5.add("500");
        values5.add("97%");

        ArrayList<String> values6 = new ArrayList<>();
        values6.add("Iron (mg)");
        values6.add("174");
        values6.add("5");
        values6.add("97%");

        tableView.addRow(values2, "#ffffff");
        tableView.addRow(values3, "#ffffff");
        tableView.addRow(values4, "#ffffff");
        tableView.addRow(values5, "#ffffff");
        tableView.addRow(values6, "#ffffff");


    }

    void CalcualateNutritionResult(){

        ConversionTable cn = new ConversionTable();

        for(int i=0;i<dm.diarysubModel.size();i++){
           // cn.CalculateEnergy(myrecipe.data.get(i).RecipeIngredient.get(0).quantity,myrecipe.data.get(i).RecipeIngredient.get(0).unit);
        }
        String energy="";String protein="";String fat="";String calcium="";String iron="";


      /*  for(int i=0;i<dm.diarysubModel.size();i++){
            for(int j=0;j<dm.diarysubModel.get(i).recipeMainData.)
        }
*/


      /*  for(int a=0;a<ingdredient.data.size();a++){
            for(int b=0;b<ingdredient.data.get(a).Ingredient.size();b++){
                for(int i=0;i<myrecipe.data.size();i++){
                    for(int j=0;j<myrecipe.data.get(i).RecipeIngredient.size();j++) {
                        if (myrecipe.data.get(i).RecipeIngredient.get(j).ingredient_id.equalsIgnoreCase(ingdredient.data.get(a).Ingredient.get(b).id)) {
                            energy = ingdredient.data.get(a).Ingredient.get(b).energy;
                            protein = ingdredient.data.get(a).Ingredient.get(b).protein;
                            fat = ingdredient.data.get(a).Ingredient.get(b).fat;
                            calcium = ingdredient.data.get(a).Ingredient.get(b).calcium;
                            iron = ingdredient.data.get(a).Ingredient.get(b).iron;
                        }
                    }

                }
            }
        }*/

        Log.e("energy - ",energy);
        Log.e("protein - ",protein);
        Log.e("fat - ",fat);
        Log.e("calcium - ",calcium);
        Log.e("iorn - ",iron);

    }

    private void fetchIngredientsdetails(){

        progressDialog = new ProgressDialog(DiaryResult.this);
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
                    addTableView();

                }catch(Exception e){
                    Log.e("exc",e.toString());
                }

                //..... Call values for calculations

                NutritionCalculation executer = new NutritionCalculation(DiaryResult.this,dm.diarysubModel.get(2).recipeMainData,ingdredient);
                executer.calculate();



            }

            @Override
            public void error(String error) {
                progressDialog.dismiss();
                Toast.makeText(DiaryResult.this, error, Toast.LENGTH_SHORT).show();
            }
        }.call();
    }


    private void fetchRecipeDetails(String recipeID){




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

        ListPopupWindow popupWindow = new ListPopupWindow(DiaryResult.this);
        popupWindow.setAnchorView(menuSettings);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height =  getResources().getDisplayMetrics().heightPixels;

        popupWindow.setWidth((int)(width/1.5));
        popupWindow.setHeight((int) (height / 1.5));
        popupWindow.setModal(true);
        popupWindow.setAdapter(new SettingsAdapter(DiaryResult.this,arrayList,drawableImage,true));
        popupWindow.show();
    }

    private void openMore() {

        View menuItemView = findViewById(R.id.actionMore); // SAME ID AS MENU ID
        String[] names = {"Home","Profile","My Recipes","Diary","Friends","Nutritional Guidelines","Glossary of Ingredients","Welcome Tour"};
        int[] drawableImage = {R.drawable.icon_home,R.drawable.drawable_profile,R.drawable.drawable_myrecipes,R.drawable.drawable_diary,R.drawable.drawable_friends,R.drawable.icon_nutritional,R.drawable.icon_gloassary,R.drawable.drawable_tour};
        ListPopupWindow popupWindow = new ListPopupWindow(DiaryResult.this);

        popupWindow.setListSelector(new ColorDrawable());
        popupWindow.setAnchorView(menuItemView);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height =  getResources().getDisplayMetrics().heightPixels;

        popupWindow.setWidth((int)(width/1.5));
        popupWindow.setHeight((int)(height/1.5));
        popupWindow.setModal(true);
        popupWindow.setAdapter(new MoreAdapter(DiaryResult.this,arrayList,drawableImage,false));
        popupWindow.show();


    }

    private void logoutFromApp() {

        Log.e("click", "logout");
        PrefUtils.clearCurrentUser(DiaryResult.this);
        LoginManager.getInstance().logOut();
        Intent i= new Intent(DiaryResult.this,StartScreen.class);
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
                            Intent i = new Intent(DiaryResult.this,DisclaimerScreen.class);
                            startActivity(i);
                            break;
                        case 5:
                            Intent i2 = new Intent(DiaryResult.this,AboutusScreen.class);
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
                            Intent iGuide = new Intent(DiaryResult.this,GuideLinesMainScreen.class);
                            startActivity(iGuide);
                            break;
                        case 6:
                            Intent i = new Intent(DiaryResult.this,GlossaryScreen.class);
                            startActivity(i);
                            break;
                    }
                }
            });

            return convertView;
        }
    }


    //end of main screen
}
