package com.example.android.parvarish_nutricalculator.custom;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
    public TextView addRecipeFromWeb, addRecipeManually;

    public CustomDialog(Activity a,int theme) {
        super(a,theme);

        // TODO Auto-generated constructor stub
        this.c = a;
    }

    public void setResponse(CustomDialogInterface customDialogInterface){
  this.customDialogInterface=customDialogInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        addRecipeFromWeb = (TextView) findViewById(R.id.addRecipeFromWeb);
        addRecipeManually = (TextView) findViewById(R.id.addRecipeManually);
        addRecipeFromWeb.setOnClickListener(this);
        addRecipeManually.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addRecipeFromWeb:
                customDialogInterface.addRecipeFromWeb();
                break;
            case R.id.addRecipeManually:
                customDialogInterface.addRecipeManually();
                break;
            default:
                break;
        }
        dismiss();
    }

    public interface CustomDialogInterface {

        public void addRecipeFromWeb();

        public void addRecipeManually();



    }
}
