package com.example.android.parvarish_nutricalculator.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.custom.ComplexPreferences;
import com.example.android.parvarish_nutricalculator.helpers.API;
import com.example.android.parvarish_nutricalculator.helpers.EnumType;
import com.example.android.parvarish_nutricalculator.helpers.GetPostClass;
import com.example.android.parvarish_nutricalculator.helpers.NutritionCalculation;
import com.example.android.parvarish_nutricalculator.helpers.NutritionCalculationSingleRecipe;
import com.example.android.parvarish_nutricalculator.helpers.PrefUtils;
import com.example.android.parvarish_nutricalculator.model.POJOTableRow;
import com.example.android.parvarish_nutricalculator.model.diarySubModel;
import com.example.android.parvarish_nutricalculator.model.glossaryDescription;
import com.example.android.parvarish_nutricalculator.model.glossaryIngredient;
import com.example.android.parvarish_nutricalculator.model.icmrMainModel;
import com.example.android.parvarish_nutricalculator.model.myrecipedata;
import com.example.android.parvarish_nutricalculator.model.sanjeevmainModel;
import com.example.android.parvarish_nutricalculator.ui.widgets.MyTableView;
import com.facebook.login.LoginManager;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class SanjeevKumarEditRecipeScreen extends ActionBarActivity {
    glossaryDescription ingdredient;
    icmrMainModel icmrOBJ;
    sanjeevmainModel sajneevObj;
    private LinearLayout linearTableDetails;
    private Toolbar toolbar;
    private TextView txtSave,txtEdit,txtServing,txtAgeGroup,txtTitle,txtMethod,txtMeth,txtING;
    ImageView photoImg;
    int listPos;
    ProgressDialog progressDialog;
    ListPopupWindow popupWindow1,popupWindow2;
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
        listPos = getIntent().getIntExtra("pos", 0);

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(SanjeevKumarEditRecipeScreen.this, "user_pref", 0);
        sajneevObj = complexPreferences.getObject("sanjeev-recipe", sanjeevmainModel.class);


        init();


        linearTableDetails = (LinearLayout)findViewById(R.id.linearTableFriendRecipeDetail);

        fetchIngredientsdetails();



        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SanjeevKumarEditRecipeScreen.this, SanjeevkapoorMainEditScreen.class);
                i.putExtra("pos", listPos);
                startActivity(i);
            }
        });

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSaveImageTOSDCard();
            }
        });

    }

    private void processSaveImageTOSDCard(){

        RelativeLayout r1 = (RelativeLayout)findViewById(R.id.mainLayout);
        View v1 = r1.getRootView();


        v1.setDrawingCacheEnabled(true);
        Bitmap b = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);


// storing the image to sd card
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        // File directory = cw.getDir("Krishna", Context.MODE_PRIVATE);
        // Create imageDir
        String root = Environment.getExternalStorageDirectory().toString();
        String filepath = root+"/Parvarish App/";

        long imgName =  System.currentTimeMillis();

        File directory = new File(filepath);
        directory.mkdirs();
        File mypath=new File(directory,""+imgName+".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Toast.makeText(SanjeevKumarEditRecipeScreen.this,"Image saved to SD card.",Toast.LENGTH_LONG).show();
            fos.close();
        } catch (Exception e) {
            Log.e("exc",toString());
            e.printStackTrace();
        }



    }

    private void fetchIngredientsdetails(){

        progressDialog = new ProgressDialog(SanjeevKumarEditRecipeScreen.this);
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
                    for(int i = 0; i < sajneevObj.data.size(); i++) {
                        for (int j = 0; j < arr.size(); j++) {
                            if (sajneevObj.data.get(i).Recipe.id.equalsIgnoreCase(arr.get(j).id)) {
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
                Toast.makeText(SanjeevKumarEditRecipeScreen.this, error, Toast.LENGTH_SHORT).show();
            }
        }.call();
    }


    private void fetchICMRdetails(){

        progressDialog = new ProgressDialog(SanjeevKumarEditRecipeScreen.this);
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



                NutritionCalculationSingleRecipe executer = new NutritionCalculationSingleRecipe(SanjeevKumarEditRecipeScreen.this,sajneevObj.data.get(listPos).RecipeIngredient,ingdredient);
                executer.startCalculation();

                executer.setOnCalculationResult(new NutritionCalculationSingleRecipe.OnCalculationResult() {
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
                Toast.makeText(SanjeevKumarEditRecipeScreen.this, error, Toast.LENGTH_SHORT).show();
            }
        }.call();
    }


    void init(){
        txtSave= (TextView)findViewById(R.id.txtSave);
        txtMeth  = (TextView)findViewById(R.id.txtMeth);
        txtING  = (TextView)findViewById(R.id.txtING);
        txtEdit = (TextView)findViewById(R.id.txtEdit);
        txtMethod = (TextView)findViewById(R.id.txtMethod);
        txtServing = (TextView)findViewById(R.id.txtServing);
        txtAgeGroup  = (TextView)findViewById(R.id.txtAgeGroup);
        txtTitle = (TextView)findViewById(R.id.txtTitle);
        photoImg = (ImageView)findViewById(R.id.photoImg);

        txtTitle.setText(sajneevObj.data.get(listPos).Recipe.name);
        txtServing.setText("Servings : " + sajneevObj.data.get(listPos).Recipe.no_of_servings);
        txtAgeGroup.setText("Age of Baby : " + sajneevObj.data.get(listPos).Recipe.age_group);

        Glide.with(SanjeevKumarEditRecipeScreen.this).load(API.BASE_URL_IMAGE_FETCH +sajneevObj.data.get(listPos).Recipe.photo_url)
                .into(photoImg);


        txtMeth.setText(Html.fromHtml(sajneevObj.data.get(listPos).Recipe.method));

    }

    private void addTableView(float energy, float protien, float fat, float calcium, float iron){

        int ICMR_GET_POS = 0;
        for(int i=0;i<icmrOBJ.data.size();i++){
            if(icmrOBJ.data.get(i).IcmrRecommended.age_group.trim().equalsIgnoreCase(sajneevObj.data.get(listPos).Recipe.age_group)){
                ICMR_GET_POS = i;
                break;
            }
        }


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

        ArrayList<POJOTableRow> values = new ArrayList<>();
        values.add(new POJOTableRow("Nutrients",Color.BLACK));
        values.add(new POJOTableRow("ICMR Recommandation", Color.BLACK));
        values.add(new POJOTableRow("Recipe Values", Color.BLACK));

        tableView.addRow(values);

        tableView.addDivider();

        ArrayList<POJOTableRow> values2 = new ArrayList<>();
        values2.add(new POJOTableRow("Energy (kcal)",Color.WHITE));
        values2.add(new POJOTableRow(icmrOBJ.data.get(ICMR_GET_POS).IcmrRecommended.energy.equalsIgnoreCase("")?"0":icmrOBJ.data.get(ICMR_GET_POS).IcmrRecommended.energy,Color.WHITE));
        values2.add(new POJOTableRow(String.format("%.2f",energy),Color.WHITE));



        ArrayList<POJOTableRow> values3 = new ArrayList<>();
        values3.add(new POJOTableRow("Protein (g)",Color.WHITE));

        values3.add(new POJOTableRow((icmrOBJ.data.get(ICMR_GET_POS).IcmrRecommended.protein.equalsIgnoreCase("")?"0":icmrOBJ.data.get(ICMR_GET_POS).IcmrRecommended.protein),Color.WHITE));
        values3.add(new POJOTableRow(String.format("%.2f",protien),Color.WHITE));


        ArrayList<POJOTableRow> values4 = new ArrayList<>();
        values4.add(new POJOTableRow("Calcium (mg)",Color.WHITE));
        values4.add(new POJOTableRow((icmrOBJ.data.get(ICMR_GET_POS).IcmrRecommended.calcium.equalsIgnoreCase("")?"0":icmrOBJ.data.get(ICMR_GET_POS).IcmrRecommended.calcium),Color.WHITE));
        values4.add(new POJOTableRow(String.format("%.2f",calcium),Color.WHITE));


        ArrayList<POJOTableRow> values5 = new ArrayList<>();
        values5.add(new POJOTableRow("Iron (mg)",Color.WHITE));
        values5.add(new POJOTableRow((icmrOBJ.data.get(ICMR_GET_POS).IcmrRecommended.iron.equalsIgnoreCase("") ? "0" : icmrOBJ.data.get(ICMR_GET_POS).IcmrRecommended.iron),Color.WHITE));
        values5.add(new POJOTableRow(String.format("%.2f", iron),Color.WHITE));

        ArrayList<POJOTableRow> values6 = new ArrayList<>();
        values6.add(new POJOTableRow("Zinc (mg)",Color.WHITE));
        values6.add(new POJOTableRow((icmrOBJ.data.get(ICMR_GET_POS).IcmrRecommended.zinc.equalsIgnoreCase("")?"0":icmrOBJ.data.get(ICMR_GET_POS).IcmrRecommended.zinc),Color.WHITE));
        values6.add(new POJOTableRow(String.format("%.2f",fat),Color.WHITE));



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
        String[] names = {"Settings", "Rate Us on Play Store", "Join Us on Facebook", "Share this App with Friends", "Disclaimers", "About Us", "Feedback", "Logout"};
        int[] drawableImage = {R.drawable.icon_home, R.drawable.drawable_profile, R.drawable.drawable_myrecipes, R.drawable.drawable_diary, R.drawable.drawable_friends, R.drawable.icon_nutritional, R.drawable.icon_gloassary, R.drawable.drawable_tour};
        popupWindow1 = new ListPopupWindow(SanjeevKumarEditRecipeScreen.this);
        popupWindow1.setAnchorView(menuSettings);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        popupWindow1.setWidth((int) (width / 1.5));
        popupWindow1.setHeight((int) (height / 1.5));
        popupWindow1.setModal(true);
        popupWindow1.setAdapter(new SettingsAdapter(SanjeevKumarEditRecipeScreen.this, arrayList, drawableImage,true));
        popupWindow1.show();
    }

    private void openMore() {

        View menuItemView = findViewById(R.id.actionMore); // SAME ID AS MENU ID
        String[] names = {"Home", "Profile", "My Recipes", "Diary", "Friends", "Nutritional Guidelines", "Glossary of Ingredients", "Welcome Tour"};
        int[] drawableImage = {R.drawable.icon_home, R.drawable.drawable_profile, R.drawable.drawable_myrecipes, R.drawable.drawable_diary, R.drawable.drawable_friends, R.drawable.icon_nutritional, R.drawable.icon_gloassary, R.drawable.drawable_tour};
        popupWindow2 = new ListPopupWindow(SanjeevKumarEditRecipeScreen.this);

        popupWindow2.setListSelector(new ColorDrawable());
        popupWindow2.setAnchorView(menuItemView);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        popupWindow2.setWidth((int) (width / 1.5));
        popupWindow2.setHeight((int) (height / 1.5));
        popupWindow2.setModal(true);
        popupWindow2.setAdapter(new MoreAdapter(SanjeevKumarEditRecipeScreen.this, arrayList, drawableImage, false));
        popupWindow2.show();


    }

    private void logoutFromApp() {

        Log.e("click", "logout");
        PrefUtils.clearCurrentUser(SanjeevKumarEditRecipeScreen.this);
        LoginManager.getInstance().logOut();


        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isUserLogin", false);
        editor.commit();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(SanjeevKumarEditRecipeScreen.this, "user_pref", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
        Intent i = new Intent(SanjeevKumarEditRecipeScreen.this, StartScreen.class);
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
                            Intent i = new Intent(SanjeevKumarEditRecipeScreen.this, DisclaimerScreen.class);
                            startActivity(i);
                            break;
                        case 5:
                            popupWindow1.dismiss();
                            Intent i2 = new Intent(SanjeevKumarEditRecipeScreen.this, AboutusScreen.class);
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
                            Intent pro1 = new Intent(SanjeevKumarEditRecipeScreen.this, HomeScreen.class);
                            startActivity(pro1);
                            break;
                        case 1:
                            popupWindow2.dismiss();
                            Intent pro = new Intent(SanjeevKumarEditRecipeScreen.this, ProfileScreen.class);
                            startActivity(pro);
                            break;
                        case 2:
                            popupWindow2.dismiss();
                            Intent myrecipe = new Intent(SanjeevKumarEditRecipeScreen.this, MyRecipeListScreen.class);
                            startActivity(myrecipe);
                            break;
                        case 3:
                            popupWindow2.dismiss();
                            Intent diary = new Intent(SanjeevKumarEditRecipeScreen.this, DiaryScreen.class);
                            startActivity(diary);
                            break;

                        case 4:
                            popupWindow2.dismiss();
                            Intent iGuide1 = new Intent(SanjeevKumarEditRecipeScreen.this, FriendsScreen.class);
                            startActivity(iGuide1);
                            break;
                        case 5:
                            popupWindow2.dismiss();
                            Intent iGuide = new Intent(SanjeevKumarEditRecipeScreen.this, GuideLinesMainScreen.class);
                            startActivity(iGuide);
                            break;
                        case 6:
                            popupWindow2.dismiss();
                            Intent i = new Intent(SanjeevKumarEditRecipeScreen.this, GlossaryScreenTemp.class);
                            startActivity(i);
                            break;
                        case 7:
                            Intent intent = new Intent(SanjeevKumarEditRecipeScreen.this, WalkThorugh.class);
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
