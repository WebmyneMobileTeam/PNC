package com.example.android.parvarish_nutricalculator.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.custom.ComplexPreferences;
import com.example.android.parvarish_nutricalculator.helpers.API;
import com.example.android.parvarish_nutricalculator.helpers.EnumType;
import com.example.android.parvarish_nutricalculator.helpers.GetPostClass;
import com.example.android.parvarish_nutricalculator.helpers.JSONPost;
import com.example.android.parvarish_nutricalculator.helpers.POSTResponseListener;
import com.example.android.parvarish_nutricalculator.helpers.PrefUtils;
import com.example.android.parvarish_nutricalculator.model.babyModel;
import com.example.android.parvarish_nutricalculator.model.glossaryDescription;
import com.example.android.parvarish_nutricalculator.model.glossaryIngredient;
import com.example.android.parvarish_nutricalculator.model.regionalmainModel;
import com.example.android.parvarish_nutricalculator.model.sanjeevmainModel;
import com.example.android.parvarish_nutricalculator.model.userModel;
import com.facebook.login.LoginManager;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class SanjeevkapoorMainEditScreen extends ActionBarActivity {
    sanjeevmainModel sajneevObj;
    private static final int CAMERA_REQUEST = 500;
    private static final int GALLERY_REQUEST = 300;
    private boolean isPictureTaken = false;
    final CharSequence[] items = { "Take Photo", "Choose from Gallery" };
    Bitmap thumbnail;
    private ProgressDialog progressDialog,progressDialog2;
    ArrayList<String> spinnerList = new ArrayList<>();
    private Spinner forSpinner;
    private Spinner spOne, spTwo;
    AutoCompleteTextView etIngname;
    ImageView imgRecipe;
    LinearLayout linearTable, linearTableAdded;
    private Toolbar toolbar;
    userModel currentUser;
    babyModel cuurentBaby;
    Button btnAddIng;
    int posi = 0, posj = 0;
    String nameIng;
    Button btnSubmit;
    EditText etRecpieName,etIngDetails,etNoofServings;
    ArrayList<glossaryIngredient> ingHashMap;
    ArrayList<String> IngredientNames;
    int Objpos;
    ListPopupWindow popupWindow1,popupWindow2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_regional_recipe_screen);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("PARVARISH NUTRI CALCULATOR");
//            toolbar.setLogo(R.drawable.logo);
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);

        init();
        processfetchBabyDetails();



        btnAddIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout mainLiner = (LinearLayout) linearTable.getChildAt(0);
                LinearLayout subLiner = (LinearLayout) mainLiner.getChildAt(0);

                Spinner tempspOne = (Spinner) subLiner.getChildAt(0);
                Spinner tempspTwo = (Spinner) subLiner.getChildAt(1);




                AutoCompleteTextView etIngr = (AutoCompleteTextView) mainLiner.getChildAt(1);
                nameIng = etIngr.getText().toString().trim();


                if (spOne.getSelectedItemPosition() == 0 || spTwo.getSelectedItemPosition() == 0) {
                    Toast.makeText(SanjeevkapoorMainEditScreen.this, "Please select Quantity and Unit first !!!", Toast.LENGTH_LONG).show();
                } else if (nameIng.toString().trim().length() == 0) {
                    Toast.makeText(SanjeevkapoorMainEditScreen.this, "Please enter ingredient name !!!", Toast.LENGTH_LONG).show();
                } else {
                    posi = tempspOne.getSelectedItemPosition();
                    posj = tempspTwo.getSelectedItemPosition();


                    processAddIng();

                    tempspOne.setSelection(0);
                    tempspTwo.setSelection(0);
                    etIngr.setText("");
                }
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int totalIng = linearTableAdded.getChildCount();

                if (etRecpieName.toString().trim().length() == 0) {
                    Toast.makeText(SanjeevkapoorMainEditScreen.this, "Please enter Recipe name !!!", Toast.LENGTH_LONG).show();
                }else if(forSpinner.getSelectedItemPosition()==0){
                   Toast.makeText(SanjeevkapoorMainEditScreen.this, "Please select Baby first !!!", Toast.LENGTH_LONG).show();
               }else if(etNoofServings.toString().trim().length() == 0){
                    Toast.makeText(SanjeevkapoorMainEditScreen.this, "Please enter No. of servings !!!", Toast.LENGTH_LONG).show();
                }else if(totalIng == 0){
                    Toast.makeText(SanjeevkapoorMainEditScreen.this, "Please add Ingredients !!!", Toast.LENGTH_LONG).show();
                }else {
                    processSubmitRecipeToServer();
                }
            }
        });

    }


    private void init(){

        imgRecipe = (ImageView)findViewById(R.id.imgRecipe);
        etNoofServings = (EditText)findViewById(R.id.etNoofServings);
        etIngDetails = (EditText)findViewById(R.id.etIngDetails);
        forSpinner = (Spinner) findViewById(R.id.forSpinner);
        etRecpieName = (EditText)findViewById(R.id.etRecpieName);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        linearTable = (LinearLayout) findViewById(R.id.linearTable);
        linearTableAdded = (LinearLayout) findViewById(R.id.linearTableAdded);
        btnAddIng = (Button) findViewById(R.id.btnAddIng);

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(SanjeevkapoorMainEditScreen.this, "user_pref", 0);
        currentUser = complexPreferences.getObject("current-user", userModel.class);

        Log.e("Pos ",""+Objpos);
        Objpos = getIntent().getIntExtra("pos", 0);

        ComplexPreferences complexPreferences2 = ComplexPreferences.getComplexPreferences(SanjeevkapoorMainEditScreen.this, "user_pref", 0);
        sajneevObj = complexPreferences2.getObject("sanjeev-recipe", sanjeevmainModel.class);


    }


    private void fillupRecipeDetails(){

        Glide.with(SanjeevkapoorMainEditScreen.this).load(API.BASE_URL_IMAGE_FETCH + sajneevObj.data.get(Objpos).Recipe.photo_url)
                .into(imgRecipe);

        etRecpieName.setText(sajneevObj.data.get(Objpos).Recipe.name);
        etNoofServings.setText(sajneevObj.data.get(Objpos).Recipe.no_of_servings);
        etIngDetails.setText(Html.fromHtml(sajneevObj.data.get(Objpos).Recipe.method).toString());

        for(int i=0;i<sajneevObj.data.get(Objpos).RecipeIngredient.size();i++){
            for(int j=0;j<ingHashMap.size();j++){
                if(ingHashMap.get(j).id.equalsIgnoreCase(sajneevObj.data.get(Objpos).RecipeIngredient.get(i).ingredient_id)){
                    processAddIngFromRegional(ingHashMap.get(j).name, sajneevObj.data.get(Objpos).RecipeIngredient.get(i).unit, sajneevObj.data.get(Objpos).RecipeIngredient.get(i).quantity);
                }
            }



        }



    }

    private void processAddIngFromRegional(String ingName,String unit,String qty){
        View view = getLayoutInflater().inflate(R.layout.item_recipe_manual_screen, linearTableAdded, false);

        spOne = (Spinner) view.findViewById(R.id.spOne);
        spTwo = (Spinner) view.findViewById(R.id.spTwo);

        AutoCompleteTextView etIngredient = (AutoCompleteTextView) view.findViewById(R.id.etIngredient);

        ArrayList<String> firstColumn = new ArrayList<String>();
        ArrayList<String> secondColumn = new ArrayList<String>();

       /* firstColumn.add("Quantity");
        firstColumn.add("1/4");
        firstColumn.add("1/2");
        for (int i = 1; i <= 10; i++)
            firstColumn.add("" + i);*/

        /*secondColumn.add("Unit");
        secondColumn.add("ML");
        secondColumn.add("GM");
        secondColumn.add("Pinch");
        secondColumn.add("Handful");
        secondColumn.add("Cup");
        secondColumn.add("Teaspoon");
        secondColumn.add("Tablespoon");*/

        firstColumn.add(qty);
        secondColumn.add(unit);

        CustomSpinnerAdapter spinnerAdapterFirst = new CustomSpinnerAdapter(SanjeevkapoorMainEditScreen.this, firstColumn);
        spOne.setAdapter(spinnerAdapterFirst);
        spOne.setSelection(posi);

        CustomSpinnerAdapter spinnerAdapterSecond = new CustomSpinnerAdapter(SanjeevkapoorMainEditScreen.this, secondColumn);
        spTwo.setAdapter(spinnerAdapterSecond);
        spTwo.setSelection(posj);

        etIngredient.setText(ingName);
        etIngredient.setFocusable(false);
        etIngredient.setOnTouchListener(myAutoEditTextListener);


        linearTableAdded.addView(view);
    }

    private void processSubmitRecipeToServer(){
        JSONObject userJSONObject = new JSONObject();
       try {

           int babyspinnerPos = forSpinner.getSelectedItemPosition();


           userJSONObject.put("name", etRecpieName.getText().toString().trim());
           userJSONObject.put("user_id", currentUser.data.id);
           userJSONObject.put("method", etIngDetails.getText().toString().trim());
           userJSONObject.put("ingredients_details","" );
           userJSONObject.put("sanjeev_kapoor_receipe", "No");
           userJSONObject.put("regional_food_receipe", "No");
           userJSONObject.put("age_group", "6-8 months");
           userJSONObject.put("no_of_servings", etNoofServings.getText().toString().trim());
           userJSONObject.put("photo_url", "");
           userJSONObject.put("baby_id", cuurentBaby.data.get(babyspinnerPos-1).Baby.id);

           JSONArray array = new JSONArray();
           for (int k = 0; k < linearTableAdded.getChildCount(); k++) {
               LinearLayout mainLiner = (LinearLayout) linearTableAdded.getChildAt(k);
               LinearLayout subLiner = (LinearLayout) mainLiner.getChildAt(0);
               AutoCompleteTextView tempetIngredient = (AutoCompleteTextView) mainLiner.getChildAt(1);


               Spinner tempspOne = (Spinner) subLiner.getChildAt(0);
               Spinner tempspTwo = (Spinner) subLiner.getChildAt(1);

               Log.e("spinner1 Value", tempspOne.getSelectedItem().toString());
               Log.e("spinner2 Value", tempspTwo.getSelectedItem().toString());
               Log.e("Ingrediitent Name Value", tempetIngredient.getText().toString().trim());

               JSONObject ingreditent = new JSONObject();

               for (int i = 0; i < ingHashMap.size(); i++) {
                   if (ingHashMap.get(i).name.equalsIgnoreCase(tempetIngredient.getText().toString().trim())) {
                       ingreditent.put("ingredient_id", ingHashMap.get(i).id);
                   }
               }
               ingreditent.put("quantity", tempspOne.getSelectedItem().toString());
               ingreditent.put("unit", tempspTwo.getSelectedItem().toString());

               array.put(ingreditent);
               // end of main for loop
           }

           userJSONObject.put("recipe_ingredient", array);

            JSONPost json = new JSONPost();
           json.POST(SanjeevkapoorMainEditScreen.this, API.ADD_RECIPE, userJSONObject.toString(),"Saving Recipe...");
           json.setPostResponseListener(new POSTResponseListener() {
               @Override
               public String onPost(String msg) {

                   Log.e("add recipe", "onPost response: " + msg);
                    Toast.makeText(SanjeevkapoorMainEditScreen.this,"Recipe added Succesfully",Toast.LENGTH_SHORT).show();
                    finish();

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

    private void processAddIng() {

        View view = getLayoutInflater().inflate(R.layout.item_recipe_manual_screen, linearTableAdded, false);

        spOne = (Spinner) view.findViewById(R.id.spOne);
        spTwo = (Spinner) view.findViewById(R.id.spTwo);

        AutoCompleteTextView etIngredient = (AutoCompleteTextView) view.findViewById(R.id.etIngredient);

        ArrayList<String> firstColumn = new ArrayList<String>();
        ArrayList<String> secondColumn = new ArrayList<String>();


        firstColumn.addAll(Arrays.asList(getResources().getStringArray(R.array.Qunatity)));
        secondColumn.addAll(Arrays.asList(getResources().getStringArray(R.array.Unit)));


        CustomSpinnerAdapter spinnerAdapterFirst = new CustomSpinnerAdapter(SanjeevkapoorMainEditScreen.this, firstColumn);
        spOne.setAdapter(spinnerAdapterFirst);
        spOne.setSelection(posi);

        CustomSpinnerAdapter spinnerAdapterSecond = new CustomSpinnerAdapter(SanjeevkapoorMainEditScreen.this, secondColumn);
        spTwo.setAdapter(spinnerAdapterSecond);
        spTwo.setSelection(posj);

        etIngredient.setText(nameIng);
        etIngredient.setFocusable(false);
        etIngredient.setOnTouchListener(myAutoEditTextListener);


        linearTableAdded.addView(view);

    }

    View.OnTouchListener myAutoEditTextListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            AutoCompleteTextView txt = (AutoCompleteTextView)v;
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == 0) {
                if (event.getRawX() >= (txt.getRight() - txt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    LinearLayout parent = (LinearLayout)txt.getParent();
                    //  Toast.makeText(DiaryScreen.this,"clicked "+,Toast.LENGTH_SHORT).show();
                    linearTableAdded.removeViewAt(linearTableAdded.indexOfChild(parent));
                    linearTableAdded.invalidate();
                }

            }



            return false;
        }
    };



    private void processfetchBabyDetails() {

        progressDialog = new ProgressDialog(SanjeevkapoorMainEditScreen.this);
        progressDialog.setMessage("Loading Details...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //API.GET_BABY_DETAILS+currentUser.data.id
        new GetPostClass(API.GET_BABY_DETAILS + currentUser.data.id, EnumType.GET) {
            @Override
            public void response(String response) {
                progressDialog.dismiss();
                Log.e("baby response", response);

                try {
                    //  JSONObject jsonObject = new JSONObject(response.toString().trim());
                    cuurentBaby = new GsonBuilder().create().fromJson(response, babyModel.class);
                    Log.e("baby size", "" + cuurentBaby.data.size());

                    fetchGlossaryList();


                } catch (Exception e) {
                    Log.e("exc", e.toString());
                }

            }

            @Override
            public void error(String error) {
                progressDialog.dismiss();
                Toast.makeText(SanjeevkapoorMainEditScreen.this, error, Toast.LENGTH_SHORT).show();
            }
        }.call();


    }
    private void fetchGlossaryList() {
        progressDialog = new ProgressDialog(SanjeevkapoorMainEditScreen.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        new GetPostClass(API.GLOSSARY_INGREDIENTS, EnumType.GET) {
            @Override
            public void response(String response) {
                progressDialog.dismiss();

                glossaryDescription gd = new GsonBuilder().create().fromJson(response, glossaryDescription.class);

                ingHashMap = gd.returnAllIngredients();

                fillupRecipeDetails();
                addBabyAdapter();


            }

            @Override
            public void error(String error) {
                progressDialog.dismiss();
                Toast.makeText(SanjeevkapoorMainEditScreen.this, error, Toast.LENGTH_SHORT).show();
            }
        }.call();

    }

    private void addBabyAdapter() {


        int babySize = cuurentBaby.data.size();
        if (babySize == 0) {
            spinnerList.add("No Baby added");
        } else {
            spinnerList.add("Select Baby");
            for (int i = 0; i < babySize; i++) {
                spinnerList.add(cuurentBaby.data.get(i).Baby.baby_name);
            }


        }


        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(SanjeevkapoorMainEditScreen.this, spinnerList);

        forSpinner.setAdapter(customSpinnerAdapter);

        IngredientNames = new ArrayList<String>();

        for (int i=0;i<ingHashMap.size();i++) {
            IngredientNames.add(ingHashMap.get(i).name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, IngredientNames);


        View view = getLayoutInflater().inflate(R.layout.item_recipe_manual_screen, linearTable, false);

        spOne = (Spinner) view.findViewById(R.id.spOne);
        spTwo = (Spinner) view.findViewById(R.id.spTwo);
        etIngname = (AutoCompleteTextView) view.findViewById(R.id.etIngredient);


       // AutoCompleteAdapter autocomlpeteadapter = new AutoCompleteAdapter(AddRecipeManualScreen.this,android.R.layout.simple_dropdown_item_1line, IngredientNames);
        etIngname.setAdapter(adapter);
       // etIngredient.setThreshold(1);


        ArrayList<String> firstColumn = new ArrayList<String>();
        ArrayList<String> secondColumn = new ArrayList<String>();


        firstColumn.addAll(Arrays.asList(getResources().getStringArray(R.array.Qunatity)));
        secondColumn.addAll(Arrays.asList(getResources().getStringArray(R.array.Unit)));


        CustomSpinnerAdapter spinnerAdapterFirst = new CustomSpinnerAdapter(SanjeevkapoorMainEditScreen.this, firstColumn);
        spOne.setAdapter(spinnerAdapterFirst);


        spOne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posi = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spTwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posj = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        CustomSpinnerAdapter spinnerAdapterSecond = new CustomSpinnerAdapter(SanjeevkapoorMainEditScreen.this, secondColumn);
        spTwo.setAdapter(spinnerAdapterSecond);

        linearTable.addView(view);

    }



    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<String> asr;

        public CustomSpinnerAdapter(Context context, ArrayList<String> asr) {
            this.asr = asr;
            activity = context;
        }

        public int getCount() {
            return asr.size();
        }

        public Object getItem(int i) {
            return asr.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(SanjeevkapoorMainEditScreen.this);
            txt.setPadding(12, 12, 12, 12);
            txt.setTextSize(getResources().getDimension(R.dimen.spinner_text));
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position).trim());
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {

            TextView txt = new TextView(SanjeevkapoorMainEditScreen.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(12, 12, 12, 12);
            txt.setTextSize(getResources().getDimension(R.dimen.spinner_text));
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_drop_down, 0);
            txt.setText(asr.get(i).trim());
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
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

        switch (id) {
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
        popupWindow1 = new ListPopupWindow(SanjeevkapoorMainEditScreen.this);
        popupWindow1.setAnchorView(menuSettings);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        popupWindow1.setWidth((int) (width / 1.5));
        popupWindow1.setHeight((int) (height / 1.5));
        popupWindow1.setModal(true);
        popupWindow1.setAdapter(new SettingsAdapter(SanjeevkapoorMainEditScreen.this, arrayList, drawableImage,true));
        popupWindow1.show();
    }

    private void openMore() {

        View menuItemView = findViewById(R.id.actionMore); // SAME ID AS MENU ID
        String[] names = {"Home", "Profile", "My Recipes", "Diary", "Friends", "Nutritional Guidelines", "Glossary of Ingredients", "Welcome Tour"};
        int[] drawableImage = {R.drawable.icon_home, R.drawable.drawable_profile, R.drawable.drawable_myrecipes, R.drawable.drawable_diary, R.drawable.drawable_friends, R.drawable.icon_nutritional, R.drawable.icon_gloassary, R.drawable.drawable_tour};
        popupWindow2 = new ListPopupWindow(SanjeevkapoorMainEditScreen.this);

        popupWindow2.setListSelector(new ColorDrawable());
        popupWindow2.setAnchorView(menuItemView);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        popupWindow2.setWidth((int) (width / 1.5));
        popupWindow2.setHeight((int) (height / 1.5));
        popupWindow2.setModal(true);
        popupWindow2.setAdapter(new MoreAdapter(SanjeevkapoorMainEditScreen.this, arrayList, drawableImage, false));
        popupWindow2.show();


    }

    private void logoutFromApp() {

        Log.e("click", "logout");
        PrefUtils.clearCurrentUser(SanjeevkapoorMainEditScreen.this);
        LoginManager.getInstance().logOut();


        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isUserLogin", false);
        editor.commit();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(SanjeevkapoorMainEditScreen.this, "user_pref", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
        Intent i = new Intent(SanjeevkapoorMainEditScreen.this, StartScreen.class);
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
                            Intent i = new Intent(SanjeevkapoorMainEditScreen.this, DisclaimerScreen.class);
                            startActivity(i);
                            break;
                        case 5:
                            popupWindow1.dismiss();
                            Intent i2 = new Intent(SanjeevkapoorMainEditScreen.this, AboutusScreen.class);
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
                            Intent pro1 = new Intent(SanjeevkapoorMainEditScreen.this, HomeScreen.class);
                            startActivity(pro1);
                            break;
                        case 1:
                            popupWindow2.dismiss();
                            Intent pro = new Intent(SanjeevkapoorMainEditScreen.this, ProfileScreen.class);
                            startActivity(pro);
                            break;
                        case 2:
                            popupWindow2.dismiss();
                            Intent myrecipe = new Intent(SanjeevkapoorMainEditScreen.this, MyRecipeListScreen.class);
                            startActivity(myrecipe);
                            break;
                        case 3:
                            popupWindow2.dismiss();
                            Intent diary = new Intent(SanjeevkapoorMainEditScreen.this, DiaryScreen.class);
                            startActivity(diary);
                            break;

                        case 4:
                            popupWindow2.dismiss();
                            Intent iGuide1 = new Intent(SanjeevkapoorMainEditScreen.this, FriendsScreen.class);
                            startActivity(iGuide1);
                            break;
                        case 5:
                            popupWindow2.dismiss();
                            Intent iGuide = new Intent(SanjeevkapoorMainEditScreen.this, GuideLinesMainScreen.class);
                            startActivity(iGuide);
                            break;
                        case 6:
                            popupWindow2.dismiss();
                            Intent i = new Intent(SanjeevkapoorMainEditScreen.this, GlossaryScreenTemp.class);
                            startActivity(i);
                            break;
                        case 7:
                            Intent intent = new Intent(SanjeevkapoorMainEditScreen.this, WalkThorugh.class);
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



}
