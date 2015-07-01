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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.custom.ComplexPreferences;
import com.example.android.parvarish_nutricalculator.helpers.API;
import com.example.android.parvarish_nutricalculator.helpers.EnumType;
import com.example.android.parvarish_nutricalculator.helpers.GetPostClass;
import com.example.android.parvarish_nutricalculator.helpers.PrefUtils;
import com.example.android.parvarish_nutricalculator.model.glossaryDescription;
import com.example.android.parvarish_nutricalculator.ui.widgets.CustomDialogBoxGlossary;
import com.facebook.login.LoginManager;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GlossaryScreen extends ActionBarActivity {
    private ProgressDialog progressDialog;
    private ListView glossaryList;
    private Toolbar toolbar;
    glossaryDescription gd;
    Button btnCategory;
    CustomAdapter adp;
    CharSequence[] catg;
    LinearLayout linearSub;
    ListPopupWindow popupWindow1,popupWindow2;
    int ipos, jpos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glossary);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("PARVARISH NUTRI CALCULATOR");
//            toolbar.setLogo(R.drawable.logo);
            setSupportActionBar(toolbar);
        }

        toolbar.setNavigationIcon(R.mipmap.ic_launcher);


        btnCategory = (Button) findViewById(R.id.btnCategory);
        glossaryList = (ListView) findViewById(R.id.glossaryList);

        fetchGlossaryList();


        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processShowCategory();
            }
        });


    }

    private void fetchGlossaryList() {
        progressDialog = new ProgressDialog(GlossaryScreen.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        new GetPostClass(API.GLOSSARY_INGREDIENTS, EnumType.GET) {
            @Override
            public void response(String response) {
                progressDialog.dismiss();

                gd = new GsonBuilder().create().fromJson(response, glossaryDescription.class);

                catg = new CharSequence[gd.data.size()];
                for (int i = 0; i < gd.data.size(); i++) {
                    catg[i] = gd.data.get(i).IngredientCategory.name;
                }


                adp = new CustomAdapter(GlossaryScreen.this, gd);
                glossaryList.setAdapter(adp);
            }

            @Override
            public void error(String error) {
                progressDialog.dismiss();
                Toast.makeText(GlossaryScreen.this, error, Toast.LENGTH_SHORT).show();
            }
        }.call();

    }


    private void processShowCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GlossaryScreen.this);
        builder.setTitle("Select Category");
        builder.setItems(catg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                btnCategory.setText(catg[item]);
                glossaryList.invalidateViews();
                adp.notifyDataSetInvalidated();
            }
        });
        builder.show();
    }


    class CustomAdapter extends BaseAdapter {
        LayoutInflater layoutInflator;
        private Context ctx;
        private glossaryDescription gdObject;
        private HashMap<String, String> values;
        Map<String, String> tempList;

        public CustomAdapter(Context ctx, glossaryDescription tempgd) {
            this.ctx = ctx;
            this.gdObject = tempgd;
        }

        @Override
        public int getCount() {


            values = new HashMap<String, String>();
            int Size = 0;
            String categoryTxt = btnCategory.getText().toString().toLowerCase();
            Log.e("button naem", categoryTxt);
            for (int i = 0; i < gdObject.data.size(); i++) {
                if (categoryTxt.equalsIgnoreCase(gdObject.data.get(i).IngredientCategory.name)) {


                    for (int j = 0; j < gdObject.data.get(i).Ingredient.size(); j++) {
                        // tempList = new HashMap<String,String>();
                        //  tempList.put("values",gdObject.data.get(i).Ingredient.get(j).name);
                        values.put(String.valueOf(gdObject.data.get(i).Ingredient.get(j).name.charAt(0)).toUpperCase(), gdObject.data.get(i).Ingredient.get(j).name);
                    }
                    //values.put(String.valueOf(gdObject.data.get(i).Ingredient.get(j).name.charAt(0)).toUpperCase(),tempList);
                }
            }


            Size = values.size();
            return Size;
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
            view = layoutInflator.inflate(R.layout.glossary_list_item_view, parent, false);

            TextView txtLetter = (TextView) view.findViewById(R.id.txtLetter);
            linearSub = (LinearLayout) view.findViewById(R.id.linearSub);


            Set<String> keys = values.keySet();
            List<String> siteIdList = new ArrayList<>(keys);
            Collections.sort(siteIdList);
            txtLetter.setText(siteIdList.get(position));

            String categoryTxt = btnCategory.getText().toString().toLowerCase();


            for (int i = 0; i < gdObject.data.size(); i++) {
                if (categoryTxt.equalsIgnoreCase(gdObject.data.get(i).IngredientCategory.name)) {

                    for (int j = 0; j < gdObject.data.get(i).Ingredient.size(); j++) {
                        String charchter = String.valueOf(gdObject.data.get(i).Ingredient.get(j).name.charAt(0)).toUpperCase();

                        if (siteIdList.get(position).equalsIgnoreCase(charchter)) {

                            ipos = i;
                            jpos = position;
                            View newIngrident = ((LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item_glossary, linearSub, false);
                            TextView txtIngName = (TextView) newIngrident.findViewById(R.id.txtIngName);
                            txtIngName.setText(gdObject.data.get(i).Ingredient.get(j).name);

                            //   txtIngName.setOnClickListener(myClickListener);


                            txtIngName.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(GlossaryScreen.this, "clicked " + gdObject.data.get(ipos).Ingredient.get(jpos).name, Toast.LENGTH_SHORT).show();
                                }
                            });

                            linearSub.addView(newIngrident);


                        }

                    }
                }
            }





/*
            String categoryTxt =  btnCategory.getText().toString().toLowerCase();
            for(int i=0;i<gdObject.data.size();i++) {
                if(categoryTxt.equalsIgnoreCase(gdObject.data.get(i).IngredientCategory.name)){
                    txtLetter.setText(String.valueOf(gdObject.data.get(i).Ingredient.get(position).name.charAt(0)));

                    View newIngrident = ((LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item_glossary, linearSub, false);
                    TextView txtIngName = (TextView)newIngrident.findViewById(R.id.txtIngName);
                    txtIngName.setText(gdObject.data.get(i).Ingredient.get(position).name);
                    linearSub.addView(newIngrident,i);
                }
            }
*/


            return view;
        }
    }

    public View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = linearSub.indexOfChild(v);


            Toast.makeText(GlossaryScreen.this, "clicked " + pos, Toast.LENGTH_SHORT).show();
        }
    };


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
        popupWindow1 = new ListPopupWindow(GlossaryScreen.this);
        popupWindow1.setAnchorView(menuSettings);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        popupWindow1.setWidth((int) (width / 1.5));
        popupWindow1.setHeight((int) (height / 1.5));
        popupWindow1.setModal(true);
        popupWindow1.setAdapter(new SettingsAdapter(GlossaryScreen.this, arrayList, drawableImage,true));
        popupWindow1.show();
    }

    private void openMore() {

        View menuItemView = findViewById(R.id.actionMore); // SAME ID AS MENU ID
        String[] names = {"Home", "Profile", "My Recipes", "Diary", "Friends", "Nutritional Guidelines", "Glossary of Ingredients", "Welcome Tour"};
        int[] drawableImage = {R.drawable.icon_home, R.drawable.drawable_profile, R.drawable.drawable_myrecipes, R.drawable.drawable_diary, R.drawable.drawable_friends, R.drawable.icon_nutritional, R.drawable.icon_gloassary, R.drawable.drawable_tour};
        popupWindow2 = new ListPopupWindow(GlossaryScreen.this);

        popupWindow2.setListSelector(new ColorDrawable());
        popupWindow2.setAnchorView(menuItemView);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        popupWindow2.setWidth((int) (width / 1.5));
        popupWindow2.setHeight((int) (height / 1.5));
        popupWindow2.setModal(true);
        popupWindow2.setAdapter(new MoreAdapter(GlossaryScreen.this, arrayList, drawableImage, false));
        popupWindow2.show();


    }

    private void logoutFromApp() {

        Log.e("click", "logout");
        PrefUtils.clearCurrentUser(GlossaryScreen.this);
        LoginManager.getInstance().logOut();


        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isUserLogin", false);
        editor.commit();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(GlossaryScreen.this, "user_pref", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
        Intent i = new Intent(GlossaryScreen.this, StartScreen.class);
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
                            Intent i = new Intent(GlossaryScreen.this, DisclaimerScreen.class);
                            startActivity(i);
                            break;
                        case 5:
                            popupWindow1.dismiss();
                            Intent i2 = new Intent(GlossaryScreen.this, AboutusScreen.class);
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

                        case 5:
                            popupWindow2.dismiss();
                            Intent iGuide = new Intent(GlossaryScreen.this, GuideLinesMainScreen.class);
                            startActivity(iGuide);
                            break;
                        case 6:
                            popupWindow2.dismiss();
                            Intent i = new Intent(GlossaryScreen.this, GlossaryScreenTemp.class);
                            startActivity(i);
                            break;
                        case 7:
                            Intent intent = new Intent(GlossaryScreen.this, WalkThorugh.class);
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
