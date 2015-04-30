package com.example.android.parvarish_nutricalculator.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.helpers.AdvancedSpannableString;

import java.util.ArrayList;

public class GuideLinesSubScreen extends ActionBarActivity {

    private String colors[] = {"#0A82C8", "#EA0083", "#F7760E", "#00A045", "#CF332F"};
    private String topics[] = {"To meet Energy and Protein requirements,\nincrease intake of:",
            "To meet Calcium requirements,\nincrease intake of:",
            "To meet Zinc requirements,\nincrease intake of:",
            "To meet Iron requirements,\nincrease intake of:",
            "Foods to Avoid until 1 Year of Age"};
    private int selectedTopic = 0;
    private TextView txtGuideLineSubPageHeading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_lines_sub_screen);
        Bundle extra = getIntent().getExtras();
        selectedTopic = extra.getInt("who");
        txtGuideLineSubPageHeading = (TextView) findViewById(R.id.txtGuideLineSubPageHeading);

        setupTopicHeading();
    }

    private void setupTopicHeading() {

        AdvancedSpannableString topic = new AdvancedSpannableString(topics[selectedTopic]);
        String toBold = "";

        switch (selectedTopic) {

            case 0:

                toBold = "Energy";
                topic.setBold(toBold);
                topic.setCustomSizeOf(1.5f,toBold);

                toBold = "Protein";
                topic.setBold(toBold);
                topic.setCustomSizeOf(1.5f,toBold);

                
                break;

            case 1:
                toBold = "Calcium";
                topic.setBold(toBold);
                topic.setCustomSizeOf(1.5f,toBold);
                break;

            case 2:

                toBold = "Zinc";
                topic.setBold(toBold);
                topic.setCustomSizeOf(1.5f,toBold);

                break;

            case 3:

                toBold = "Iron";
                topic.setBold(toBold);
                topic.setCustomSizeOf(1.5f,toBold);

                break;

            case 4:

                toBold = "Avoid";
                topic.setBold(toBold);
                topic.setCustomSizeOf(1.5f,toBold);

                toBold = "1 Year";
                topic.setBold(toBold);
                topic.setCustomSizeOf(1.5f,toBold);

                break;
        }


        txtGuideLineSubPageHeading.setText(topic);
        txtGuideLineSubPageHeading.setTextColor(Color.parseColor(colors[selectedTopic]));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guide_lines_sub_screen, menu);
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
}
