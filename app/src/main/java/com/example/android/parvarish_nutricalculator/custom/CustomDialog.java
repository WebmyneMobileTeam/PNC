package com.example.android.parvarish_nutricalculator.custom;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.android.parvarish_nutricalculator.R;

/**
 * Created by Android on 21-04-2015.
 */
public class CustomDialog extends Dialog implements
        android.view.View.OnClickListener {
    CustomDialogInterface customDialogInterface;
    public Activity c;
    public Dialog d;
    private String msgText1,msgText2;
    public TextView topButton, bottomButton;

    public CustomDialog(Activity a,String txt1,String txt2,int theme) {
        super(a,theme);

        // TODO Auto-generated constructor stub
        this.c = a;
        this.msgText1 = txt1;
        this.msgText2 = txt2;
    }

    public void setResponse(CustomDialogInterface customDialogInterface){
            this.customDialogInterface=customDialogInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        topButton = (TextView) findViewById(R.id.topButton);
        bottomButton = (TextView) findViewById(R.id.bottomButton);

        topButton.setText(msgText1);
        bottomButton.setText(msgText2);



        topButton.setOnClickListener(this);
        bottomButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topButton:
                customDialogInterface.topButton();
                break;
            case R.id.bottomButton:
                customDialogInterface.bottomButton();
                break;
            default:
                break;
        }
        dismiss();
    }

    public interface CustomDialogInterface {

        public void topButton();

        public void bottomButton();



    }
}
