package com.example.android.parvarish_nutricalculator.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.custom.CustomDialog;
import com.example.android.parvarish_nutricalculator.ui.widgets.CustomDialogBox;

import java.util.ArrayList;

public class DiaryScreen extends ActionBarActivity {

    ArrayList<String> spinnerList = new ArrayList<>();
    private Spinner SpBaby;
    private ListView listdiary;
    private Button btnCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_screen);

        spinnerList.add("Select Baby");
        spinnerList.add("Add New Baby");
        spinnerList.add("one");
        spinnerList.add("two");
        spinnerList.add("three");
        spinnerList.add("four");

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(DiaryScreen.this, spinnerList);
        SpBaby = (Spinner) findViewById(R.id.SpBaby);
        listdiary = (ListView) findViewById(R.id.listdiary);

        SpBaby.setAdapter(customSpinnerAdapter);

        btnCalculate = (Button) findViewById(R.id.btnCalculate);


        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DiaryScreen.this, DiaryResult.class);
                startActivity(i);
            }
        });

        SpBaby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    CustomDialogBox cdbox = new CustomDialogBox(DiaryScreen.this);
                    cdbox.show();

                } else {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        View headerView = ((LayoutInflater) DiaryScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.diary_list_header_item, null, false);
        View footerView = ((LayoutInflater) DiaryScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.diary_list_footer_item, null, false);
        listdiary.addHeaderView(headerView);
        listdiary.addFooterView(footerView);
        CustomListAdapter adp = new CustomListAdapter(DiaryScreen.this);
        listdiary.setAdapter(adp);

    }

    class CustomListAdapter extends BaseAdapter {
        LayoutInflater layoutInflator;
        private Context ctx;

        public CustomListAdapter(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        public int getCount() {
            return 10;
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
            view = layoutInflator.inflate(R.layout.diary_list_item_view, parent, false);
            return view;
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
            TextView txt = new TextView(DiaryScreen.this);
            txt.setPadding(16, 16, 16, 16);
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
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_drop_down, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }
    }


    //end of main screen
}
