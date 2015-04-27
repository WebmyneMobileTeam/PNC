package com.example.android.parvarish_nutricalculator.ui.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.parvarish_nutricalculator.R;

/**
 * Created by Android on 27-04-2015.
 */
public class CustomDialogBox extends Dialog  implements
        android.view.View.OnClickListener{

    public Activity act;
    public Dialog d;
    public Button yes;


    public CustomDialogBox(Activity context) {
        super(context);
        this.act = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_alert_dialog);

        yes = (Button) findViewById(R.id.btnAddBaby);
        yes.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnAddBaby:
                Toast.makeText(act,"Clicked",Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        dismiss();

    }
}
