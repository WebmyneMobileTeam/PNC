package com.example.android.parvarish_nutricalculator.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.helpers.API;
import com.example.android.parvarish_nutricalculator.helpers.IngredientAdapter;
import com.example.android.parvarish_nutricalculator.helpers.EnumType;
import com.example.android.parvarish_nutricalculator.helpers.GetPostClass;
import com.example.android.parvarish_nutricalculator.model.glossaryDescription;
import com.google.gson.GsonBuilder;
import com.tonicartos.superslim.LayoutManager;

import java.util.ArrayList;

public class GlossaryScreenTempFragment extends Fragment {
    private ProgressDialog progressDialog;
    private ListView glossaryList;
    private Toolbar toolbar;
    glossaryDescription gd;
    Button btnCategory;
    CharSequence[] catg;
    LinearLayout linearSub;
    int ipos, jpos;
    ArrayList<String> ingName;
    String[] ingName2;


    private static final String KEY_HEADER_POSITIONING = "key_header_mode";

    private static final String KEY_MARGINS_FIXED = "key_margins_fixed";

    private ViewHolder mViews;

    public static IngredientAdapter mAdapter;

    private int mHeaderDisplay;

    private boolean mAreMarginsFixed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_main, container, false);
        btnCategory =  (Button)v.findViewById(R.id.btnCategory);


        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            mHeaderDisplay = savedInstanceState
                    .getInt(KEY_HEADER_POSITIONING,
                            getResources().getInteger(R.integer.default_header_display));
            mAreMarginsFixed = savedInstanceState
                    .getBoolean(KEY_MARGINS_FIXED,
                            getResources().getBoolean(R.bool.default_margins_fixed));
        } else {
            mHeaderDisplay = getResources().getInteger(R.integer.default_header_display);
            mAreMarginsFixed = getResources().getBoolean(R.bool.default_margins_fixed);
        }

        fetchGlossaryList();

        mViews = new ViewHolder(view);
        mViews.initViews(new LayoutManager(getActivity()));

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processShowCategory();
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_HEADER_POSITIONING, mHeaderDisplay);
        outState.putBoolean(KEY_MARGINS_FIXED, mAreMarginsFixed);
    }

    private void fetchGlossaryList() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        new GetPostClass(API.GLOSSARY_INGREDIENTS, EnumType.GET) {
            @Override
            public void response(String response) {
                progressDialog.dismiss();

                gd = new GsonBuilder().create().fromJson(response, glossaryDescription.class);

                catg = new CharSequence[gd.data.size()];
                for (int i = 0; i < gd.data.size(); i++) {
                    catg[i] = gd.data.get(i).IngredientCategory.name;
                }


                String categoryTxt = btnCategory.getText().toString().toLowerCase();
                //String categoryTxt = "Cereals";
                ingName = new ArrayList<String>();
                for (int i = 0; i < gd.data.size(); i++) {
                    if (categoryTxt.equalsIgnoreCase(gd.data.get(i).IngredientCategory.name)) {

                        ingName2 = new String[gd.data.get(i).Ingredient.size()];
                        for (int j = 0; j < gd.data.get(i).Ingredient.size(); j++) {

                            ingName2[j] = gd.data.get(i).Ingredient.get(j).name;
                            ingName.add(gd.data.get(i).Ingredient.get(j).name);
                        }

                    }
                }



                mAdapter = new IngredientAdapter(getActivity(), mHeaderDisplay,ingName2);

                mAdapter.setMarginsFixed(mAreMarginsFixed);
                mAdapter.setHeaderDisplay(mHeaderDisplay);
                mViews.setAdapter(mAdapter);

            }

            @Override
            public void error(String error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        }.call();

    }

    private void processShowCategory() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Category");
        builder.setItems(catg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                btnCategory.setText(catg[item]);

                processUpdateData();

                //        glossaryList.invalidateViews();
                // mad.notifyDataSetInvalidated();*/
            }
        });
        builder.show();
    }

    private  void processUpdateData(){

        String categoryTxt = btnCategory.getText().toString().toLowerCase();
        for (int i = 0; i < gd.data.size(); i++) {
            if (categoryTxt.equalsIgnoreCase(gd.data.get(i).IngredientCategory.name)) {

                ingName2 = new String[gd.data.get(i).Ingredient.size()];
                for (int j = 0; j < gd.data.get(i).Ingredient.size(); j++) {

                    ingName2[j] = gd.data.get(i).Ingredient.get(j).name;
                }

            }
        }


        mAdapter = new IngredientAdapter(getActivity(), mHeaderDisplay,ingName2);
        mAdapter.setMarginsFixed(mAreMarginsFixed);
        mAdapter.setHeaderDisplay(mHeaderDisplay);
        mViews.setAdapter(mAdapter);
    }




    private static class ViewHolder {

        private final RecyclerView mRecyclerView;


        public ViewHolder(View view) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        }

        public void initViews(LayoutManager lm) {
            mRecyclerView.setLayoutManager(lm);
        }


        public void setAdapter(RecyclerView.Adapter<?> adapter) {
            mRecyclerView.setAdapter(adapter);
        }


    }











    //end of main class
}
