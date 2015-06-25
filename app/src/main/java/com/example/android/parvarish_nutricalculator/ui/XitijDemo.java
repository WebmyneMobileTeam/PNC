package com.example.android.parvarish_nutricalculator.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.ui.widgets.AnimatedView;


public class XitijDemo extends Activity {

    LinearLayout pictureLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xitij_demo);

        pictureLayout = (LinearLayout)findViewById(R.id.picLayout);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        AnimatedView view = new AnimatedView(XitijDemo.this);
        pictureLayout.addView(view,params);

    }


}
