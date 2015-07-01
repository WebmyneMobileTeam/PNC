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
import com.example.android.parvarish_nutricalculator.model.POJOTableRow;
import com.example.android.parvarish_nutricalculator.model.diaryModel;
import com.example.android.parvarish_nutricalculator.model.diarySubModel;
import com.example.android.parvarish_nutricalculator.model.glossaryDescription;
import com.example.android.parvarish_nutricalculator.model.icmrMainModel;
import com.example.android.parvarish_nutricalculator.model.myrecipedata;
import com.example.android.parvarish_nutricalculator.model.recipeModel;
import com.example.android.parvarish_nutricalculator.ui.widgets.HUD;
import com.example.android.parvarish_nutricalculator.ui.widgets.MyTableView;
import com.facebook.login.LoginManager;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class DiaryResult extends ActionBarActivity {
    ListPopupWindow popupWindow1,popupWindow2;
    HUD progressDialog,progressDialog2;
    diaryModel dm;
    private LinearLayout linearTableDetails,linearRecipeNames;
    private Toolbar toolbar;
    recipeModel myrecipe;
    glossaryDescription ingdredient;
    icmrMainModel icmrOBJ;

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

    private void addTableView(float energy, float protien, float fat, float calcium, float iron,float ICMR_RATIO_ENERGY,float ICMR_RATIO_PROTEIN,float ICMR_RATIO_CALCIUM,float ICMR_RATIO_FAT,float ICMR_RATIO_IRON) {

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

        ArrayList<POJOTableRow> values = new ArrayList<>();
        values.add(new POJOTableRow("Nutrients",Color.BLACK));
        values.add(new POJOTableRow("Recipe Values",Color.BLACK));
        values.add(new POJOTableRow("ICMR Recommandation", Color.BLACK));
        values.add(new POJOTableRow("% ICMR Recommandation met", Color.BLACK));

        tableView.addRow(values);
        tableView.addDivider();

        ArrayList<POJOTableRow> values2 = new ArrayList<>();
        values2.add(new POJOTableRow("Energy (kcal)",Color.WHITE));
        values2.add(new POJOTableRow(String.format("%.2f",energy),Color.WHITE));
        values2.add(new POJOTableRow(icmrOBJ.data.get(0).IcmrRecommended.energy.equalsIgnoreCase("")?"0":icmrOBJ.data.get(0).IcmrRecommended.energy,Color.WHITE));


        // Checking the ICMR RAtio For ENERGY
        if(ICMR_RATIO_ENERGY >=85 && ICMR_RATIO_ENERGY<=105)
        values2.add(new POJOTableRow(String.format("%.2f",ICMR_RATIO_ENERGY)+"%",Color.WHITE));
        else values2.add(new POJOTableRow(String.format("%.2f",ICMR_RATIO_ENERGY)+"%",Color.RED));
        //END

        ArrayList<POJOTableRow> values3 = new ArrayList<>();
        values3.add(new POJOTableRow("Protein (gm)",Color.WHITE));
        values3.add(new POJOTableRow(String.format("%.2f",protien),Color.WHITE));
        values3.add(new POJOTableRow((icmrOBJ.data.get(0).IcmrRecommended.protein.equalsIgnoreCase("")?"0":icmrOBJ.data.get(0).IcmrRecommended.protein),Color.WHITE));

        // Checking the ICMR RAtio For PROTEIN
        if(ICMR_RATIO_PROTEIN >=85 && ICMR_RATIO_PROTEIN<=105)
            values3.add(new POJOTableRow(String.format("%.2f",ICMR_RATIO_PROTEIN)+"%",Color.WHITE));
        else values3.add(new POJOTableRow(String.format("%.2f",ICMR_RATIO_PROTEIN)+"%",Color.RED));
        //END

        ArrayList<POJOTableRow> values4 = new ArrayList<>();
        values4.add(new POJOTableRow("Fat (gm)",Color.WHITE));
        values4.add(new POJOTableRow(String.format("%.2f",fat),Color.WHITE));
        values4.add(new POJOTableRow((icmrOBJ.data.get(0).IcmrRecommended.fat.equalsIgnoreCase("")?"0":icmrOBJ.data.get(0).IcmrRecommended.fat),Color.WHITE));

        // Checking the ICMR RAtio For FAT
        if(ICMR_RATIO_FAT >=85 && ICMR_RATIO_FAT<=105)
            values4.add(new POJOTableRow(String.format("%.2f",ICMR_RATIO_FAT)+"%",Color.WHITE));
        else values4.add(new POJOTableRow(String.format("%.2f",ICMR_RATIO_FAT)+"%",Color.RED));
        //END

        ArrayList<POJOTableRow> values5 = new ArrayList<>();
        values5.add(new POJOTableRow("Calcium (mg)",Color.WHITE));
        values5.add(new POJOTableRow(String.format("%.2f",calcium),Color.WHITE));
        values5.add(new POJOTableRow((icmrOBJ.data.get(0).IcmrRecommended.calcium.equalsIgnoreCase("")?"0":icmrOBJ.data.get(0).IcmrRecommended.calcium),Color.WHITE));

        // Checking the ICMR RAtio For CALCIUM
        if(ICMR_RATIO_CALCIUM >=85 && ICMR_RATIO_CALCIUM<=105)
            values5.add(new POJOTableRow(String.format("%.2f",ICMR_RATIO_CALCIUM)+"%",Color.WHITE));
        else values5.add(new POJOTableRow(String.format("%.2f",ICMR_RATIO_CALCIUM)+"%",Color.RED));
        // END

        ArrayList<POJOTableRow> values6 = new ArrayList<>();
        values6.add(new POJOTableRow("Iron (mg)",Color.WHITE));
        values6.add(new POJOTableRow(String.format("%.2f", iron),Color.WHITE));
        values6.add(new POJOTableRow((icmrOBJ.data.get(0).IcmrRecommended.iron.equalsIgnoreCase("") ? "0" : icmrOBJ.data.get(0).IcmrRecommended.iron),Color.WHITE));

        // Checking the ICMR RAtio For IRON
        if(ICMR_RATIO_IRON >=85 && ICMR_RATIO_IRON<=105)
            values6.add(new POJOTableRow(String.format("%.2f",ICMR_RATIO_IRON)+"%",Color.WHITE));
        else values6.add(new POJOTableRow(String.format("%.2f",ICMR_RATIO_IRON)+"%",Color.RED));
        // END

        tableView.addRow(values2);
        tableView.addRow(values3);
        tableView.addRow(values4);
        tableView.addRow(values5);
        tableView.addRow(values6);



    }



    private void fetchIngredientsdetails(){

        progressDialog =new HUD(DiaryResult.this,android.R.style.Theme_Translucent_NoTitleBar);
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


                }catch(Exception e){
                    Log.e("exc",e.toString());
                }

                fetchICMRdetails();




            }

            @Override
            public void error(String error) {
                progressDialog.dismiss();
                Toast.makeText(DiaryResult.this, error, Toast.LENGTH_SHORT).show();
            }
        }.call();
    }


    private void fetchICMRdetails(){

        progressDialog  =new HUD(DiaryResult.this,android.R.style.Theme_Translucent_NoTitleBar);
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

                //..... Call values for calculations
                ArrayList<myrecipedata> arr = new ArrayList<myrecipedata>();

                for(diarySubModel diaryM : dm.diarysubModel ){
                    arr.add(diaryM.recipeMainData);
                }


                NutritionCalculation executer = new NutritionCalculation(DiaryResult.this,arr,ingdredient);
                executer.startCalculation();
                executer.setOnCalculationResult(new NutritionCalculation.OnCalculationResult() {
                    @Override
                    public void onResult(float energy, float protien, float fat, float calcium, float iron) {
                        Log.e("Result Energy ",""+energy);
                        Log.e("Result protien ",""+protien);
                        Log.e("Result fat ",""+fat);
                        Log.e("Result calcium ",""+calcium);
                        Log.e("Result iron ",""+iron);

                        float finalTOTAL_ENERGY = energy;
                        float finalTOTAL_CALCIUM = calcium;
                        float finalTOTAL_PROTEIN = protien;
                        float finalTOTAL_IRON = iron;
                        float finalTOTAL_FAT = fat;


                        float icmrEnrgy = Float.parseFloat((icmrOBJ.data.get(0).IcmrRecommended.energy.equalsIgnoreCase("")?"100":icmrOBJ.data.get(0).IcmrRecommended.energy));
                        float icmrProtein =  Float.parseFloat((icmrOBJ.data.get(0).IcmrRecommended.protein.equalsIgnoreCase("")?"100":icmrOBJ.data.get(0).IcmrRecommended.protein));
                        float icmrCalcium =   Float.parseFloat((icmrOBJ.data.get(0).IcmrRecommended.calcium.equalsIgnoreCase("")?"100":icmrOBJ.data.get(0).IcmrRecommended.calcium));
                        float icmrFat =   Float.parseFloat((icmrOBJ.data.get(0).IcmrRecommended.fat.equalsIgnoreCase("")?"100":icmrOBJ.data.get(0).IcmrRecommended.fat));
                        float icmrIron =  Float.parseFloat((icmrOBJ.data.get(0).IcmrRecommended.iron.equalsIgnoreCase("")?"100":icmrOBJ.data.get(0).IcmrRecommended.iron));

                        float ICMR_RATIO_ENERGY = (finalTOTAL_ENERGY / icmrEnrgy) * 100;
                        float ICMR_RATIO_PROTEIN = (finalTOTAL_PROTEIN / icmrProtein) * 100;
                        float ICMR_RATIO_CALCIUM = (finalTOTAL_CALCIUM / icmrCalcium) * 100;
                        float ICMR_RATIO_FAT = (finalTOTAL_FAT / icmrFat) * 100;
                        float ICMR_RATIO_IRON = (finalTOTAL_IRON / icmrIron) * 100;

                        addTableView(energy,protien,fat,calcium,iron,ICMR_RATIO_ENERGY,ICMR_RATIO_PROTEIN,ICMR_RATIO_CALCIUM,ICMR_RATIO_FAT,ICMR_RATIO_IRON);
                    }
                });




            }

            @Override
            public void error(String error) {
                progressDialog.dismiss();
                Toast.makeText(DiaryResult.this, error, Toast.LENGTH_SHORT).show();
            }
        }.call();
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
        popupWindow1 = new ListPopupWindow(DiaryResult.this);
        popupWindow1.setAnchorView(menuSettings);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        popupWindow1.setWidth((int) (width / 1.5));
        popupWindow1.setHeight((int) (height / 1.5));
        popupWindow1.setModal(true);
        popupWindow1.setAdapter(new SettingsAdapter(DiaryResult.this, arrayList, drawableImage,true));
        popupWindow1.show();
    }

    private void openMore() {

        View menuItemView = findViewById(R.id.actionMore); // SAME ID AS MENU ID
        String[] names = {"Home", "Profile", "My Recipes", "Diary", "Friends", "Nutritional Guidelines", "Glossary of Ingredients", "Welcome Tour"};
        int[] drawableImage = {R.drawable.icon_home, R.drawable.drawable_profile, R.drawable.drawable_myrecipes, R.drawable.drawable_diary, R.drawable.drawable_friends, R.drawable.icon_nutritional, R.drawable.icon_gloassary, R.drawable.drawable_tour};
        popupWindow2 = new ListPopupWindow(DiaryResult.this);

        popupWindow2.setListSelector(new ColorDrawable());
        popupWindow2.setAnchorView(menuItemView);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        popupWindow2.setWidth((int) (width / 1.5));
        popupWindow2.setHeight((int) (height / 1.5));
        popupWindow2.setModal(true);
        popupWindow2.setAdapter(new MoreAdapter(DiaryResult.this, arrayList, drawableImage, false));
        popupWindow2.show();


    }

    private void logoutFromApp() {

        Log.e("click", "logout");
        PrefUtils.clearCurrentUser(DiaryResult.this);
        LoginManager.getInstance().logOut();


        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isUserLogin", false);
        editor.commit();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(DiaryResult.this, "user_pref", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
        Intent i = new Intent(DiaryResult.this, StartScreen.class);
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
                            Intent i = new Intent(DiaryResult.this, DisclaimerScreen.class);
                            startActivity(i);
                            break;
                        case 5:
                            popupWindow1.dismiss();
                            Intent i2 = new Intent(DiaryResult.this, AboutusScreen.class);
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
                            Intent pro1 = new Intent(DiaryResult.this, HomeScreen.class);
                            startActivity(pro1);
                            break;
                        case 1:
                            popupWindow2.dismiss();
                            Intent pro = new Intent(DiaryResult.this, ProfileScreen.class);
                            startActivity(pro);
                            break;
                        case 2:
                            popupWindow2.dismiss();
                            Intent myrecipe = new Intent(DiaryResult.this, MyRecipeListScreen.class);
                            startActivity(myrecipe);
                            break;
                        case 3:
                            popupWindow2.dismiss();
                            Intent diary = new Intent(DiaryResult.this, DiaryScreen.class);
                            startActivity(diary);
                            break;

                        case 4:
                            popupWindow2.dismiss();
                            Intent iGuide1 = new Intent(DiaryResult.this, FriendsScreen.class);
                            startActivity(iGuide1);
                            break;
                        case 5:
                            popupWindow2.dismiss();
                            Intent iGuide = new Intent(DiaryResult.this, GuideLinesMainScreen.class);
                            startActivity(iGuide);
                            break;
                        case 6:
                            popupWindow2.dismiss();
                            Intent i = new Intent(DiaryResult.this, GlossaryScreenTemp.class);
                            startActivity(i);
                            break;
                        case 7:
                            Intent intent = new Intent(DiaryResult.this, WalkThorugh.class);
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


    //end of main screen
}
