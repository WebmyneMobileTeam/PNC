package com.example.android.parvarish_nutricalculator.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.ui.widgets.CustomDialogBox;
import com.example.android.parvarish_nutricalculator.ui.widgets.MyTableView;

import java.util.ArrayList;

public class DiaryResult extends ActionBarActivity {


    private LinearLayout linearTableDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_result);

        linearTableDetails = (LinearLayout)findViewById(R.id.linearTableFriendRecipeDetail);
        addTableView();


    }
    private void addTableView() {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

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
        values2.add("Energy");
        values2.add("174");
        values2.add("6.733(Approx)");
        values2.add("97%");

        tableView.addRow(values2, "#ffffff");
        tableView.addRow(values2, "#ffffff");
        tableView.addRow(values2, "#ffffff");
        tableView.addRow(values2, "#ffffff");
        tableView.addRow(values2, "#ffffff");
        tableView.addRow(values2, "#ffffff");

    }




    //end of main screen
}
