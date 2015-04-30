package com.example.android.parvarish_nutricalculator.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.ui.widgets.MyTableView;

import java.util.ArrayList;

public class SanjeevKumarEditRecipeScreen extends ActionBarActivity {

    private LinearLayout linearTableDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sanjeev_recipie);

        linearTableDetails = (LinearLayout)findViewById(R.id.linearTableFriendRecipeDetail);
        addTableView();


    }

    private void addTableView() {

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

        ArrayList<String> values = new ArrayList<>();
        values.add("Nutrients");
        values.add("ICMR Recommandation");
        values.add("Values");

        tableView.addRow(values, "#000000");
        tableView.addDivider();

        ArrayList<String> values2 = new ArrayList<>();
        values2.add("Energy");
        values2.add("6.733(Approx)");
        values2.add("174");


        tableView.addRow(values2, "#ffffff");
        tableView.addRow(values2, "#ffffff");
        tableView.addRow(values2, "#ffffff");
        tableView.addRow(values2, "#ffffff");
        tableView.addRow(values2, "#ffffff");
        tableView.addRow(values2, "#ffffff");

    }


}
