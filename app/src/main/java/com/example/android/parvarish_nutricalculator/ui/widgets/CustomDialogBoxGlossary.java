package com.example.android.parvarish_nutricalculator.ui.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.parvarish_nutricalculator.R;

/**
 * Created by Android on 27-04-2015.
 */
public class CustomDialogBoxGlossary extends Dialog  {

    public Activity act;
    public Dialog d;
    public Button yes;
    TextView ingDesc,ingName,ingbenifits,txtNODATA,title;
    String ING_NAME,ING_DESC,ING_BENIFITS;

    public CustomDialogBoxGlossary(Activity context,String name,String desc,String benifits) {
        super(context);
        this.act = context;
        this.ING_NAME = name;
        this.ING_DESC = desc;
        this.ING_BENIFITS = benifits;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_alert_dialog_glossary);

        title= (TextView)findViewById(R.id.title);
        txtNODATA= (TextView)findViewById(R.id.txtNODATA);
        ingbenifits = (TextView)findViewById(R.id.ingbenifits);
        ingName = (TextView)findViewById(R.id.ingName);
        ingDesc = (TextView)findViewById(R.id.ingDesc);

        if(ING_DESC.length()==0 && ING_BENIFITS.length()==0){
            txtNODATA.setVisibility(View.VISIBLE);
            title.setVisibility(View.GONE);
        }else if(ING_BENIFITS.length()==0){
            txtNODATA.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
        }else {
            txtNODATA.setVisibility(View.GONE);
            ingName.setText(ING_NAME);
            ingDesc.setText(ING_DESC);
            ingbenifits.setText(ING_BENIFITS);
        }
        setCancelable(true);

    }


}
