package com.example.android.parvarish_nutricalculator.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.custom.CustomDialog;

public class SanjeevKapoorScreen extends ActionBarActivity {

    private GridView imageList;

    public Integer[] mThumbIds = {
            R.drawable.d_one, R.drawable.d_two,
            R.drawable.d_three, R.drawable.d_four,
            R.drawable.d_one_blur, R.drawable.d_two_blur,
            R.drawable.d_three_blur, R.drawable.d_four_blur,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanjeevkapoor_screen);

        imageList = (GridView)findViewById(R.id.imageList);

        CustomImageAdapter adp = new CustomImageAdapter(SanjeevKapoorScreen.this);
        imageList.setAdapter(adp);



    }
    class CustomImageAdapter extends BaseAdapter{
        LayoutInflater layoutInflator;
        private Context ctx;
        public CustomImageAdapter(Context ctx){
            this.ctx = ctx;
        }

        @Override
        public int getCount() {
            return 8;
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
            view = layoutInflator.inflate(R.layout.grid_item_view, parent, false);

            ImageView imageView = (ImageView)view.findViewById(R.id.image);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageView.setImageResource(mThumbIds[position]);

            return view;

           /* ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(ctx);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[position]);
            return imageView;*/
        }
    }
}
