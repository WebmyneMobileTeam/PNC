package com.example.android.parvarish_nutricalculator.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.custom.ComplexPreferences;
import com.example.android.parvarish_nutricalculator.custom.CustomDialog;
import com.example.android.parvarish_nutricalculator.helpers.API;
import com.example.android.parvarish_nutricalculator.helpers.EnumType;
import com.example.android.parvarish_nutricalculator.helpers.GetPostClass;
import com.example.android.parvarish_nutricalculator.helpers.PrefUtils;
import com.example.android.parvarish_nutricalculator.model.babyModel;
import com.example.android.parvarish_nutricalculator.model.diaryModel;
import com.example.android.parvarish_nutricalculator.model.diarySubModel;
import com.example.android.parvarish_nutricalculator.model.myrecipeModel;
import com.example.android.parvarish_nutricalculator.model.userModel;
import com.example.android.parvarish_nutricalculator.ui.widgets.CustomDialogBox;
import com.example.android.parvarish_nutricalculator.ui.widgets.HUD;
import com.facebook.login.LoginManager;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class DiaryScreen extends ActionBarActivity {
    ListPopupWindow popupWindow1,popupWindow2;
    private HUD progressDialog,progressDialog2;
    userModel currentUser;
    babyModel cuurentBaby;
    ArrayList<String> spinnerList = new ArrayList<>();
    ArrayList<String> spinnerListServings = new ArrayList<>();
    private Spinner SpBaby;
    private ListView listdiary;
    private Button btnCalculate,btnAddMeal;
    private Toolbar toolbar;
    LinearLayout linearTable, linearTableAdded;
    CharSequence[] myRecipes;
    myrecipeModel myrecipe;
    int lastTouchedPos = 0;
    ImageView imgDiaryReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_screen);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("PARVARISH NUTRI CALCULATOR");
//            toolbar.setLogo(R.drawable.logo);
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);

        init();
        callAsyncTaskForWebService();
        processAddTopTable();
        processAddSavedDiaryData();


        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(DiaryScreen.this, "user_pref", 0);
                diaryModel dmobj = complexPreferences.getObject("current-diary", diaryModel.class);

                if(dmobj == null ||dmobj.diarysubModel.size()==0){
                 Toast.makeText(DiaryScreen.this,"Please add recipe in diary !!!",Toast.LENGTH_SHORT).show();
                }else {
                    Intent i = new Intent(DiaryScreen.this, DiaryResult.class);
                    startActivity(i);
                }
            }
        });

        btnAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner tempServing = (Spinner) linearTable.findViewById(R.id.spServings);
                TextView txtrecipeName = (TextView) linearTable.findViewById(R.id.txtrecipeName);
                TextView txtDishNo = (TextView) linearTable.findViewById(R.id.txtDishNo);


                if (tempServing.getSelectedItemPosition() == 0) {
                    Toast.makeText(DiaryScreen.this, "Please select No. of Servings !!!", Toast.LENGTH_LONG).show();
                } else if (txtrecipeName.getText().toString().equalsIgnoreCase("Recipe name")) {
                    Toast.makeText(DiaryScreen.this, "Please select recipe !!!", Toast.LENGTH_LONG).show();
                } else {
                    processAddBottomTable(txtrecipeName.getText().toString().trim(),tempServing.getSelectedItemPosition());
                    tempServing.setSelection(0);
                    txtrecipeName.setText("Recipe name");
                    txtDishNo.setText("Dish no.");
                }


            }
        });


        //Deleting the library
        imgDiaryReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(DiaryScreen.this, "user_pref", 0);
                complexPreferences.removeObject("current-diary");
                complexPreferences.commit();

                PrefUtils.RefreshActivity(DiaryScreen.this);

            }
        });




    }

    void init(){

        spinnerListServings.add("Servings");
        spinnerListServings.add("1");
        spinnerListServings.add("2");
        spinnerListServings.add("3");
        spinnerListServings.add("4");
        spinnerListServings.add("5");


        imgDiaryReset = (ImageView)findViewById(R.id.imgDiaryReset);
        linearTable = (LinearLayout) findViewById(R.id.linearTable);
        linearTableAdded = (LinearLayout) findViewById(R.id.linearTableAdded);

        SpBaby = (Spinner) findViewById(R.id.SpBaby);
        btnAddMeal= (Button) findViewById(R.id.btnAddMeal);
        btnCalculate = (Button) findViewById(R.id.btnCalculate);


        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(DiaryScreen.this, "user_pref", 0);
        currentUser = complexPreferences.getObject("current-user", userModel.class);





    }

    void callAsyncTaskForWebService(){
        processFetchBabydetails();
        fetchMyRecipe();
    }


    void processAddTopTable(){
        View view = getLayoutInflater().inflate(R.layout.diary_list_item_view, linearTable, false);
        TextView txtDishNo = (TextView)view.findViewById(R.id.txtDishNo);
        final TextView txtrecipeName = (TextView)view.findViewById(R.id.txtrecipeName);
        Spinner spServings = (Spinner)view.findViewById(R.id.spServings);

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(DiaryScreen.this, spinnerListServings);
        spServings.setAdapter(customSpinnerAdapter);

        txtrecipeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyRecipe(txtrecipeName);
            }
        });

        linearTable.addView(view);
    }

    void processAddBottomTable(String recipeName,int pos){

        View view = getLayoutInflater().inflate(R.layout.diary_list_item_view, linearTableAdded, false);


        TextView txtDishNo = (TextView)view.findViewById(R.id.txtDishNo);
        final TextView txtrecipeName = (TextView)view.findViewById(R.id.txtrecipeName);
        Spinner spServings = (Spinner)view.findViewById(R.id.spServings);

        int counter = linearTableAdded.getChildCount();
        counter+=1;

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(DiaryScreen.this, spinnerListServings);
        spServings.setAdapter(customSpinnerAdapter);

        spServings.setSelection(pos);
        txtDishNo.setText("Dish " + counter);
        txtrecipeName.setFocusable(false);
        txtrecipeName.setText(recipeName);
        txtrecipeName.setOnTouchListener(myTextListener);

        linearTableAdded.addView(view);

        saveDiaryData();
    }

    private void saveDiaryData(){

        ArrayList<diarySubModel> arrayDSM = new ArrayList<diarySubModel>();;
        diaryModel dm = new diaryModel();

        for(int i=0;i<linearTableAdded.getChildCount(); i++){

            View vg= linearTableAdded.getChildAt(i);

            diarySubModel dsm = new diarySubModel();
            Spinner tempServing = (Spinner) vg.findViewById(R.id.spServings);
            TextView txtrecipeName = (TextView) vg.findViewById(R.id.txtrecipeName);
            TextView txtDishNo = (TextView) vg.findViewById(R.id.txtDishNo);

            dsm.diaryNo=txtDishNo.getText().toString().trim();
            dsm.noServings=tempServing.getSelectedItemPosition();


            for(int k=0;k<myrecipe.data.Recipe.size();k++){
                if(txtrecipeName.getText().toString().equalsIgnoreCase(myrecipe.data.Recipe.get(k).name)){
                    dsm.recipeID = myrecipe.data.Recipe.get(k).id;
                    dsm.recipeMainData = myrecipe.data.Recipe.get(k);
                }
            }

            arrayDSM.add(dsm);
        }

        dm.diarysubModel = arrayDSM;

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(DiaryScreen.this, "user_pref", 0);
        complexPreferences.putObject("current-diary", dm);
        complexPreferences.commit();
    }

    void processAddSavedDiaryData(){

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(DiaryScreen.this, "user_pref", 0);
        diaryModel dm = complexPreferences.getObject("current-diary", diaryModel.class);

        try {
            if (dm.diarysubModel.size() != 0) {
                for (int i = 0; i < dm.diarysubModel.size(); i++) {

                    View view = getLayoutInflater().inflate(R.layout.diary_list_item_view, linearTableAdded, false);

                    TextView txtDishNo = (TextView) view.findViewById(R.id.txtDishNo);
                    final TextView txtrecipeName = (TextView) view.findViewById(R.id.txtrecipeName);
                    Spinner spServings = (Spinner) view.findViewById(R.id.spServings);

                    CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(DiaryScreen.this, spinnerListServings);
                    spServings.setAdapter(customSpinnerAdapter);

                    Log.e("rec id - ", "" + dm.diarysubModel.get(i).recipeID);

                    spServings.setSelection(dm.diarysubModel.get(i).noServings);
                    txtDishNo.setText("Dish " + (i + 1));
                    txtrecipeName.setFocusable(false);

                    txtrecipeName.setText(dm.diarysubModel.get(i).recipeMainData.name);
                    txtrecipeName.setOnTouchListener(myTextListener);
                    linearTableAdded.addView(view, i);
                }
            }
        }catch (Exception e){
            Log.e("exc in diary",e.toString());
        }

    }


    View.OnTouchListener myTextListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
        //    Toast.makeText(DiaryScreen.this,"clicked ",Toast.LENGTH_SHORT).show();
            TextView txt = (TextView)v;
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == 0) {
                if (event.getRawX() >= (txt.getRight() - txt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    LinearLayout parent = (LinearLayout)txt.getParent();
                    //  Toast.makeText(DiaryScreen.this,"clicked "+,Toast.LENGTH_SHORT).show();
                    linearTableAdded.removeViewAt(linearTableAdded.indexOfChild(parent));
                    linearTableAdded.invalidate();
                }

            }

            saveDiaryData();
            return false;
        }
    };



    private void processFetchBabydetails(){
        progressDialog =new HUD(DiaryScreen.this,android.R.style.Theme_Translucent_NoTitleBar);
        progressDialog.setCancelable(false);
        progressDialog.show();
        //API.GET_BABY_DETAILS+currentUser.data.id
        new GetPostClass(API.GET_BABY_DETAILS + currentUser.data.id, EnumType.GET) {
            @Override
            public void response(String response) {
                Log.e("baby response", response);
                progressDialog.dismiss();

                try {
                    //  JSONObject jsonObject = new JSONObject(response.toString().trim());
                    cuurentBaby = new GsonBuilder().create().fromJson(response, babyModel.class);
                    Log.e("baby size", "" + cuurentBaby.data.size());

                    addBabyInSpinner();

                } catch (Exception e) {
                    Log.e("exc", e.toString());
                }

            }

            @Override
            public void error(String error) {
                progressDialog.dismiss();
                Toast.makeText(DiaryScreen.this, error, Toast.LENGTH_SHORT).show();
            }
        }.call();

    }

    void addBabyInSpinner(){

        int babySize = cuurentBaby.data.size();
        if (babySize == 0) {
            spinnerList.add("No Baby added");
        } else {
            spinnerList.add("Select Baby");
            for (int i = 0; i < babySize; i++) {
                spinnerList.add(cuurentBaby.data.get(i).Baby.baby_name);
            }

        }


        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(DiaryScreen.this, spinnerList);
        SpBaby.setAdapter(customSpinnerAdapter);

    }

    private void fetchMyRecipe(){
        progressDialog2 =new HUD(DiaryScreen.this,android.R.style.Theme_Translucent_NoTitleBar);
        progressDialog2.setCancelable(false);
        progressDialog2.show();
        new GetPostClass(API.MY_RECIPE+currentUser.data.id, EnumType.GET) {
            @Override
            public void response(String response) {
                Log.e("my recipe response", response);
                progressDialog2.dismiss();
                try {
                    //  JSONObject jsonObject = new JSONObject(response.toString().trim());
                    myrecipe = new GsonBuilder().create().fromJson(response, myrecipeModel.class);

                    myRecipes = new CharSequence[myrecipe.data.Recipe.size()];
                    for (int i = 0; i < myrecipe.data.Recipe.size(); i++) {
                        myRecipes[i] = myrecipe.data.Recipe.get(i).name;
                    }


                }catch(Exception e){
                    Log.e("exc",e.toString());
                }

            }

            @Override
            public void error(String error) {
                progressDialog2.dismiss();
                Toast.makeText(DiaryScreen.this, error, Toast.LENGTH_SHORT).show();
            }
        }.call();
    }

    private void showMyRecipe(final TextView txtrecipeName){
        AlertDialog.Builder builder = new AlertDialog.Builder(DiaryScreen.this);
        builder.setTitle("Select Recipe");
        builder.setItems(myRecipes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                txtrecipeName.setText(myRecipes[item]);
              //  processUpdateData();

            }
        });
        builder.show();
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
            TextView txt = new TextView(DiaryScreen.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setSingleLine(true);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;

        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(DiaryScreen.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setSingleLine(true);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_drop_down, 0);
            txt.setText(asr.get(i));
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
        popupWindow1 = new ListPopupWindow(DiaryScreen.this);
        popupWindow1.setAnchorView(menuSettings);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        popupWindow1.setWidth((int) (width / 1.5));
        popupWindow1.setHeight((int) (height / 1.5));
        popupWindow1.setModal(true);
        popupWindow1.setAdapter(new SettingsAdapter(DiaryScreen.this, arrayList, drawableImage,true));
        popupWindow1.show();
    }

    private void openMore() {

        View menuItemView = findViewById(R.id.actionMore); // SAME ID AS MENU ID
        String[] names = {"Home", "Profile", "My Recipes", "Diary", "Friends", "Nutritional Guidelines", "Glossary of Ingredients", "Welcome Tour"};
        int[] drawableImage = {R.drawable.icon_home, R.drawable.drawable_profile, R.drawable.drawable_myrecipes, R.drawable.drawable_diary, R.drawable.drawable_friends, R.drawable.icon_nutritional, R.drawable.icon_gloassary, R.drawable.drawable_tour};
        popupWindow2 = new ListPopupWindow(DiaryScreen.this);

        popupWindow2.setListSelector(new ColorDrawable());
        popupWindow2.setAnchorView(menuItemView);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        popupWindow2.setWidth((int) (width / 1.5));
        popupWindow2.setHeight((int) (height / 1.5));
        popupWindow2.setModal(true);
        popupWindow2.setAdapter(new MoreAdapter(DiaryScreen.this, arrayList, drawableImage, false));
        popupWindow2.show();


    }

    private void logoutFromApp() {

        Log.e("click", "logout");
        PrefUtils.clearCurrentUser(DiaryScreen.this);
        LoginManager.getInstance().logOut();


        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isUserLogin", false);
        editor.commit();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(DiaryScreen.this, "user_pref", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
        Intent i = new Intent(DiaryScreen.this, StartScreen.class);
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
                            Intent i = new Intent(DiaryScreen.this, DisclaimerScreen.class);
                            startActivity(i);
                            break;
                        case 5:
                            popupWindow1.dismiss();
                            Intent i2 = new Intent(DiaryScreen.this, AboutusScreen.class);
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
                            Intent pro1 = new Intent(DiaryScreen.this, HomeScreen.class);
                            startActivity(pro1);
                            break;
                        case 1:
                            popupWindow2.dismiss();
                            Intent pro = new Intent(DiaryScreen.this, ProfileScreen.class);
                            startActivity(pro);
                            break;
                        case 2:
                            popupWindow2.dismiss();
                            Intent myrecipe = new Intent(DiaryScreen.this, MyRecipeListScreen.class);
                            startActivity(myrecipe);
                            break;
                        case 3:
                            popupWindow2.dismiss();
                            Intent diary = new Intent(DiaryScreen.this, DiaryScreen.class);
                            startActivity(diary);
                            break;

                        case 4:
                            popupWindow2.dismiss();
                            Intent iGuide1 = new Intent(DiaryScreen.this, FriendsScreen.class);
                            startActivity(iGuide1);
                            break;
                        case 5:
                            popupWindow2.dismiss();
                            Intent iGuide = new Intent(DiaryScreen.this, GuideLinesMainScreen.class);
                            startActivity(iGuide);
                            break;
                        case 6:
                            popupWindow2.dismiss();
                            Intent i = new Intent(DiaryScreen.this, GlossaryScreenTemp.class);
                            startActivity(i);
                            break;
                        case 7:
                            Intent intent = new Intent(DiaryScreen.this, WalkThorugh.class);
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
