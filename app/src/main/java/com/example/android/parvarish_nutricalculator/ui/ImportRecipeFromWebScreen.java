package com.example.android.parvarish_nutricalculator.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.android.parvarish_nutricalculator.R;

import java.util.ArrayList;

public class ImportRecipeFromWebScreen extends ActionBarActivity {

    ArrayList<String> spinnerList=new ArrayList<>();
    private Spinner forSpinner;
    private Spinner spOne,spTwo;
    LinearLayout linearTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_recipe_web_screen);


        linearTable= (LinearLayout) findViewById(R.id.linearTable);
        spinnerList.add("Select Baby");
        spinnerList.add("one");
        spinnerList.add("two");
        spinnerList.add("three");
        spinnerList.add("four");
        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(ImportRecipeFromWebScreen.this,spinnerList);
        forSpinner= (Spinner) findViewById(R.id.forSpinner);
        forSpinner.setAdapter(customSpinnerAdapter);
        for(int i=0;i<5;i++) {
            View view = getLayoutInflater().inflate(R.layout.item_recipe_manual_screen, linearTable, false);
            spOne = (Spinner) view.findViewById(R.id.spOne);
            spTwo = (Spinner) view.findViewById(R.id.spTwo);
            spOne.setAdapter(customSpinnerAdapter);
            spTwo.setAdapter(customSpinnerAdapter);
            linearTable.addView(view);
        }

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
            TextView txt = new TextView(ImportRecipeFromWebScreen.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;

        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(ImportRecipeFromWebScreen.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_drop_down, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }
    }

}
