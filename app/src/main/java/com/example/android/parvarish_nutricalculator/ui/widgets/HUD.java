package com.example.android.parvarish_nutricalculator.ui.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.parvarish_nutricalculator.R;

public class HUD extends Dialog{


    private View convertView;
    private Context ctx;
    private ProgressBar pb;
    private ImageView imgStatus;
    private TextView txtTitle;
    private FrameLayout frameLayout;

    public HUD(Context context, int theme) {
        super(context, theme);
        this.ctx = context;
        init();
    }

    private void init() {

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = inflater.inflate(R.layout.ui_hud,null);
        setContentView(convertView);



    }



}
