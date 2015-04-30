package com.example.android.parvarish_nutricalculator.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.android.parvarish_nutricalculator.R;

import java.util.ArrayList;

public class MyRecipeListScreen extends ActionBarActivity {

    private Spinner forSpinner;
    ArrayList<String> spinnerList=new ArrayList<>();
    private ListView myRecipeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe_list_screen);
        myRecipeList= (ListView) findViewById(R.id.myRecipeList);

        CustomAdapter adp = new CustomAdapter(MyRecipeListScreen.this);
        myRecipeList.setAdapter(adp);
        spinnerList.add("Sort");
        spinnerList.add("one");
        spinnerList.add("two");
        spinnerList.add("three");
        spinnerList.add("four");
        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(MyRecipeListScreen.this,spinnerList);
        forSpinner= (Spinner) findViewById(R.id.forSpinner);
        forSpinner.setAdapter(customSpinnerAdapter);

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
            txt.setPadding(16,16,16,16);
            txt.setTextSize(18);
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
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_drop_down, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }
    }

    class CustomAdapter extends BaseAdapter{
        LayoutInflater layoutInflator;
        private Context ctx;
        public CustomAdapter(Context ctx){
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
            view = layoutInflator.inflate(R.layout.friend_feed_item_view, parent, false);
            return view;
        }
    }
}
