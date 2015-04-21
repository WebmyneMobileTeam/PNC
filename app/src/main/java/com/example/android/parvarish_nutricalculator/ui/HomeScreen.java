package com.example.android.parvarish_nutricalculator.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.ui.StartScreen;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class HomeScreen extends ActionBarActivity {


    private String[] names1 = {"My Recipes", "Profiles", "Add Recipe"};
    private String[] names2 = {"Diary", "Friends", "Welcome Tour"};
    private int[] icons1 = {R.drawable.myrecipes, R.drawable.profile, R.drawable.addrecipe};
    private int[] icons2 = {R.drawable.diary, R.drawable.friends, R.drawable.help};
    private LinearLayout linearFirst;
    private LinearLayout linearSecond;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("HOME");
//            toolbar.setLogo(R.drawable.logo);
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);

       setupButtons();
    }

    private void setupButtons() {

        linearFirst = (LinearLayout)findViewById(R.id.firstRow);
        linearSecond = (LinearLayout)findViewById(R.id.secondRow);

        for(int i=0;i<linearFirst.getChildCount();i++){

            ViewGroup vg = (ViewGroup)linearFirst.getChildAt(i);
            ImageView img = (ImageView)vg.findViewById(R.id.itemImageHome);
            TextView txt = (TextView)vg.findViewById(R.id.itemTextHome);
            img.setImageResource(icons1[i]);
            txt.setText(names1[i]);

        }

        for(int i=0;i<linearSecond.getChildCount();i++){

            ViewGroup vg = (ViewGroup)linearSecond.getChildAt(i);
            ImageView img = (ImageView)vg.findViewById(R.id.itemImageHome);
            TextView txt = (TextView)vg.findViewById(R.id.itemTextHome);
            img.setImageResource(icons2[i]);
            txt.setText(names2[i]);
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void openMore() {

        View menuItemView = findViewById(R.id.actionMore); // SAME ID AS MENU ID
        PopupMenu popup = new PopupMenu(this,menuItemView);

        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper
                            .getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        popup.inflate(R.menu.menu_more_home);
        popup.show();
    }

}
