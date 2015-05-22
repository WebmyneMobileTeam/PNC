package com.example.android.parvarish_nutricalculator.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.custom.ComplexPreferences;
import com.example.android.parvarish_nutricalculator.helpers.API;
import com.example.android.parvarish_nutricalculator.helpers.EnumType;
import com.example.android.parvarish_nutricalculator.helpers.GetPostClass;
import com.example.android.parvarish_nutricalculator.helpers.IngredientAdapter;
import com.example.android.parvarish_nutricalculator.helpers.PrefUtils;
import com.example.android.parvarish_nutricalculator.model.babyModel;
import com.example.android.parvarish_nutricalculator.model.myrecipeModel;
import com.example.android.parvarish_nutricalculator.model.myrecipedata;
import com.example.android.parvarish_nutricalculator.model.userModel;
import com.facebook.login.LoginManager;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MyRecipeListScreen extends ActionBarActivity {
    private ProgressDialog progressDialog;
    private Spinner forSpinner;
    ArrayList<String> spinnerList=new ArrayList<>();
    private ListView myRecipeList;
    userModel currentUser;
    private Toolbar toolbar;
    myrecipeModel myrecipe;
    List<String> recpname;
    List<String> babyAge;
    CustomAdapter adp;
    EditText etSearchRecipe;
    Button btnAddRecipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe_list_screen);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("PARVARISH NUTRI CALCULATOR");
//            toolbar.setLogo(R.drawable.logo);
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);

        init();


        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(MyRecipeListScreen.this, "user_pref", 0);
        currentUser = complexPreferences.getObject("current-user", userModel.class);

        fetchMyRecpie();



        spinnerList.add("Sort");
        spinnerList.add("Baby name Ascending");
        spinnerList.add("Baby name Descending");

        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(MyRecipeListScreen.this,spinnerList);
        forSpinner= (Spinner) findViewById(R.id.forSpinner);
        forSpinner.setAdapter(customSpinnerAdapter);


        forSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else {
                    adp.sorting(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        etSearchRecipe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etSearchRecipe.getRight() - etSearchRecipe.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        if (etSearchRecipe.getText().toString().trim().length() == 0) {
                            Toast.makeText(MyRecipeListScreen.this, "Please Enter any word for search !!!", Toast.LENGTH_SHORT).show();
                        } else {
                            adp.filter(etSearchRecipe.getText().toString().trim());
                        }

                        return true;
                    }
                }

                return false;
            }
        });


        btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyRecipeListScreen.this,AddRecipeManualScreen.class);
                startActivity(i);
            }
        });

    }




    private void init(){
        btnAddRecipe = (Button)findViewById(R.id.btnAddRecipe);
        etSearchRecipe = (EditText)findViewById(R.id.etSearchRecipe);
        myRecipeList= (ListView) findViewById(R.id.myRecipeList);
        View emptyView = getLayoutInflater().inflate(R.layout.empty_myrecipe,null, false);
        myRecipeList.setEmptyView(emptyView);


    }

    private void fetchMyRecpie(){

        progressDialog =new ProgressDialog(MyRecipeListScreen.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        new GetPostClass(API.MY_RECIPE+currentUser.data.id, EnumType.GET) {
            @Override
            public void response(String response) {
                progressDialog.dismiss();
                Log.e("my recipe response", response);

                try {
                    //  JSONObject jsonObject = new JSONObject(response.toString().trim());
                    myrecipe = new GsonBuilder().create().fromJson(response, myrecipeModel.class);

                    adp = new CustomAdapter(MyRecipeListScreen.this,myrecipe.data.Recipe);
                    myRecipeList.setAdapter(adp);


                }catch(Exception e){
                    Log.e("exc",e.toString());
                }

            }

            @Override
            public void error(String error) {
                progressDialog.dismiss();
                Toast.makeText(MyRecipeListScreen.this, error, Toast.LENGTH_SHORT).show();
            }
        }.call();


    }

    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<String> asr;

        public CustomSpinnerAdapter(Context context,ArrayList<String> asr) {
            this.asr=asr;
            activity = context;
        }

        public int getCount()
        {
            return asr.size();
        }

        public Object getItem(int i)
        {
            return asr.get(i);
        }

        public long getItemId(int i)
        {
            return (long)i;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(MyRecipeListScreen.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setSingleLine(true);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;

        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(MyRecipeListScreen.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setTextSize(18);
            txt.setSingleLine(true);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_drop_down, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }
    }




    class CustomAdapter extends BaseAdapter{
        LayoutInflater layoutInflator;
        private Context ctx;

        List<myrecipedata> ValuesSearch;
        ArrayList<myrecipedata> arraylist;

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = convertView;
            view = layoutInflator.inflate(R.layout.myrecipe_feed_item_view, parent, false);

            ImageView imgDelete = (ImageView)view.findViewById(R.id.imgDelete);
            TextView txtBabyage = (TextView)view.findViewById(R.id.txtBabyage);
            TextView txtrecipeName = (TextView)view.findViewById(R.id.txtrecipeName);

            txtrecipeName.setText(ValuesSearch.get(position).name);
            txtBabyage.setText(ValuesSearch.get(position).age_group);


            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
                for ( myrecipedata obj: arraylist) {
                    if (charText.length() != 0 && obj.name.toLowerCase(Locale.getDefault()).contains(charText)) {
                        ValuesSearch.add(obj);
                    }

                }
            }
            notifyDataSetChanged();
        }

        // Filter Class
        public void sorting(int pos) {
            int ASC = 1;
            int DSC = 2;
            //ValuesSearch.clear();
            //ValuesSearch.addAll(arraylist);
            if(pos ==1){
                Collections.sort(ValuesSearch, new Comparator<myrecipedata>() {
                    @Override
                    public int compare(myrecipedata object1, myrecipedata object2) {
                        return object1.name.compareTo(object2.name);
                    }
                });
            }else{
                Collections.sort(ValuesSearch, new Comparator<myrecipedata>() {
                    @Override
                    public int compare(myrecipedata object1, myrecipedata object2) {
                        return object2.name.compareTo(object1.name);
                    }
                });
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

        ListPopupWindow popupWindow = new ListPopupWindow(MyRecipeListScreen.this);
        popupWindow.setAnchorView(menuSettings);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height =  getResources().getDisplayMetrics().heightPixels;

        popupWindow.setWidth((int)(width/1.5));
        popupWindow.setHeight((int) (height / 1.5));
        popupWindow.setModal(true);
        popupWindow.setAdapter(new SettingsAdapter(MyRecipeListScreen.this,arrayList,drawableImage,true));
        popupWindow.show();
    }

    private void openMore() {

        View menuItemView = findViewById(R.id.actionMore); // SAME ID AS MENU ID
        String[] names = {"Home","Profile","My Recipes","Diary","Friends","Nutritional Guidelines","Glossary of Ingredients","Welcome Tour"};
        int[] drawableImage = {R.drawable.icon_home,R.drawable.drawable_profile,R.drawable.drawable_myrecipes,R.drawable.drawable_diary,R.drawable.drawable_friends,R.drawable.icon_nutritional,R.drawable.icon_gloassary,R.drawable.drawable_tour};
        ListPopupWindow popupWindow = new ListPopupWindow(MyRecipeListScreen.this);

        popupWindow.setListSelector(new ColorDrawable());
        popupWindow.setAnchorView(menuItemView);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height =  getResources().getDisplayMetrics().heightPixels;

        popupWindow.setWidth((int)(width/1.5));
        popupWindow.setHeight((int)(height/1.5));
        popupWindow.setModal(true);
        popupWindow.setAdapter(new MoreAdapter(MyRecipeListScreen.this,arrayList,drawableImage,false));
        popupWindow.show();


    }

    private void logoutFromApp() {

        Log.e("click", "logout");
        PrefUtils.clearCurrentUser(MyRecipeListScreen.this);
        LoginManager.getInstance().logOut();
        Intent i= new Intent(MyRecipeListScreen.this,StartScreen.class);
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
                            Intent i = new Intent(MyRecipeListScreen.this,DisclaimerScreen.class);
                            startActivity(i);
                            break;
                        case 5:
                            Intent i2 = new Intent(MyRecipeListScreen.this,AboutusScreen.class);
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
                            Intent iGuide = new Intent(MyRecipeListScreen.this,GuideLinesMainScreen.class);
                            startActivity(iGuide);
                            break;
                        case 6:
                            Intent i = new Intent(MyRecipeListScreen.this,GlossaryScreen.class);
                            startActivity(i);
                            break;
                        case 7:
                            Intent intent = new Intent(MyRecipeListScreen.this, WalkThorugh.class);
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
