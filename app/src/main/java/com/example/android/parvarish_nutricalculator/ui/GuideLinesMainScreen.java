package com.example.android.parvarish_nutricalculator.ui;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.android.parvarish_nutricalculator.R;

import java.io.File;

public class GuideLinesMainScreen extends ActionBarActivity implements View.OnClickListener {

    private ImageView img_guidelinemainpage_energy;
    private ImageView img_guidelinemainpage_calcium;
    private ImageView img_guidelinemainpage_zinc;
    private ImageView img_guidelinemainpage_iron;
    private ImageView img_guidelinemainpage_foods;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_lines_main_screen);

        img_guidelinemainpage_energy = (ImageView) findViewById(R.id.img_guidelinemainpage_energy);
        img_guidelinemainpage_calcium = (ImageView) findViewById(R.id.img_guidelinemainpage_calcium);
        img_guidelinemainpage_zinc = (ImageView) findViewById(R.id.img_guidelinemainpage_zinc);
        img_guidelinemainpage_iron = (ImageView) findViewById(R.id.img_guidelinemainpage_iron);
        img_guidelinemainpage_foods = (ImageView) findViewById(R.id.img_guidelinemainpage_foods);

        img_guidelinemainpage_energy.setOnClickListener(this);
        img_guidelinemainpage_calcium.setOnClickListener(this);
        img_guidelinemainpage_zinc.setOnClickListener(this);
        img_guidelinemainpage_iron.setOnClickListener(this);
        img_guidelinemainpage_foods.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guide_lines_main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        Intent iSublist = new Intent(GuideLinesMainScreen.this,GuideLinesSubScreen.class);

        String whomi = "";

        switch (v.getId()) {

            case R.id.img_guidelinemainpage_calcium:

                iSublist.putExtra("who",1);
                startActivity(iSublist);

                break;

            case R.id.img_guidelinemainpage_energy:

                iSublist.putExtra("who",0);
                startActivity(iSublist);

                break;

            case R.id.img_guidelinemainpage_foods:

                iSublist.putExtra("who",4);
                startActivity(iSublist);

                break;

            case R.id.img_guidelinemainpage_iron:

                iSublist.putExtra("who",3);
                startActivity(iSublist);

                break;

            case R.id.img_guidelinemainpage_zinc:

                iSublist.putExtra("who",2);
                startActivity(iSublist);

                break;



        }

    }
}
