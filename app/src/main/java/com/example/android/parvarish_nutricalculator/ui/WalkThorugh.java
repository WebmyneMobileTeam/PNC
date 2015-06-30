package com.example.android.parvarish_nutricalculator.ui;

import android.app.ActionBar;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.helpers.CustomViewPager;
import com.example.android.parvarish_nutricalculator.ui.widgets.CirclePageIndicator;
import com.example.android.parvarish_nutricalculator.ui.widgets.PageIndicator;


public class WalkThorugh extends FragmentActivity {

    private CustomViewPager viewPager;
    private  TabsPagerAdapter mAdapter;
    protected PageIndicator mIndicator;
    public ImageView image1;
    private ImageView image2;
    private ImageView imageCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        //setting the device not to open walk thorugh
        SharedPreferences preferences = getSharedPreferences("firstTime", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirstTime", false);
        editor.commit();
        //end coding walk thorugh

        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        imageCancel = (ImageView)findViewById(R.id.image_cancel);
        viewPager = (CustomViewPager) findViewById(R.id.pager);

       mIndicator = (CirclePageIndicator)findViewById(R.id.guideIndicator);
       imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WalkThorugh.this, HomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(getItem(+1), true);
            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(getItem(-1), true);
            }
        });

        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(viewPager);


        viewPager.setOnSwipeOutListener(new CustomViewPager.OnSwipeOutListener() {


            @Override
            public void onSwipeOutAtEnd() {
                Intent intent = new Intent(WalkThorugh.this, HomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });


    }

    private int getItem(int i){

        return  viewPager.getCurrentItem() + i;
    }

    public class TabsPagerAdapter extends FragmentStatePagerAdapter {

        private WalkThorugh main;

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override


        public Fragment getItem(int index) {

            switch (index){

                case 0:

                    return SliderFragment.newInstance(R.drawable.slide1,"");

                // Top Rated fragment activity

                case 1:

                    return SliderFragment.newInstance(R.drawable.slide2,"");

                // Top Rated fragment activity

                case 2:

                    // Top Rated fragment activity

                    return SliderFragment.newInstance(R.drawable.slide3,"");

                case 3:

                    // Top Rated fragment activity

                    return SliderFragment.newInstance(R.drawable.slide4,"");

                case 4:

                    // Top Rated fragment activity

                    return SliderFragment.newInstance(R.drawable.slide5,"");

                case 5:

                    // Top Rated fragment activity

                    return SliderFragment.newInstance(R.drawable.slide6,"");

                case 6:

                    // Top Rated fragment activity

                    return SliderFragment.newInstance(R.drawable.slide7,"");

                case 7:

                    // Top Rated fragment activity

                    return SliderFragment.newInstance(R.drawable.slide88,"");

            }

            return null;
        }

        @Override
        public int getCount() {
            return 8;
        }

    }


}


