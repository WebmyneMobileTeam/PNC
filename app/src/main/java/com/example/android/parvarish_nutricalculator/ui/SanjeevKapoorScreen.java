package com.example.android.parvarish_nutricalculator.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.custom.CustomDialog;
import com.example.android.parvarish_nutricalculator.helpers.PrefUtils;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.Arrays;

public class SanjeevKapoorScreen extends ActionBarActivity {

    private GridView imageList;
    private Toolbar toolbar;
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


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("PARVARISH NUTRI CALCULATOR");
//            toolbar.setLogo(R.drawable.logo);
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);

        imageList = (GridView)findViewById(R.id.imageList);

        CustomImageAdapter adp = new CustomImageAdapter(SanjeevKapoorScreen.this);
        imageList.setAdapter(adp);


        imageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i= new Intent(SanjeevKapoorScreen.this,SanjeevKumarEditRecipeScreen.class);
                    startActivity(i);
            }
        });



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.actionMore:
                openMore();
                break;
            case R.id.actionSettings:
                openSettings();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void openSettings() {

        View menuSettings = findViewById(R.id.actionSettings); // SAME ID AS MENU ID

        String[] names = {"Settings","Rate Us on Play Store","Join Us on Facebook","Share this App with Friends","Disclaimers","About Us","Feedback","Logout"};
        int[] drawableImage = {R.drawable.icon_home,R.drawable.drawable_profile,R.drawable.drawable_myrecipes,R.drawable.drawable_diary,R.drawable.drawable_friends,R.drawable.icon_nutritional,R.drawable.icon_gloassary,R.drawable.drawable_tour};

        ListPopupWindow popupWindow = new ListPopupWindow(SanjeevKapoorScreen.this);
        popupWindow.setAnchorView(menuSettings);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height =  getResources().getDisplayMetrics().heightPixels;

        popupWindow.setWidth((int)(width/1.5));
        popupWindow.setHeight((int) (height / 1.5));
        popupWindow.setModal(true);
        popupWindow.setAdapter(new SettingsAdapter(SanjeevKapoorScreen.this,arrayList,drawableImage,true));
        popupWindow.show();
    }

    private void openMore() {

        View menuItemView = findViewById(R.id.actionMore); // SAME ID AS MENU ID
        String[] names = {"Home","Profile","My Recipes","Diary","Friends","Nutritional Guidelines","Glossary of Ingredients","Welcome Tour"};
        int[] drawableImage = {R.drawable.icon_home,R.drawable.drawable_profile,R.drawable.drawable_myrecipes,R.drawable.drawable_diary,R.drawable.drawable_friends,R.drawable.icon_nutritional,R.drawable.icon_gloassary,R.drawable.drawable_tour};
        ListPopupWindow popupWindow = new ListPopupWindow(SanjeevKapoorScreen.this);

        popupWindow.setListSelector(new ColorDrawable());
        popupWindow.setAnchorView(menuItemView);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height =  getResources().getDisplayMetrics().heightPixels;

        popupWindow.setWidth((int)(width/1.5));
        popupWindow.setHeight((int)(height/1.5));
        popupWindow.setModal(true);
        popupWindow.setAdapter(new MoreAdapter(SanjeevKapoorScreen.this,arrayList,drawableImage,false));
        popupWindow.show();


    }

    private void logoutFromApp() {

        Log.e("click", "logout");
        PrefUtils.clearCurrentUser(SanjeevKapoorScreen.this);
        LoginManager.getInstance().logOut();
        Intent i= new Intent(SanjeevKapoorScreen.this,StartScreen.class);
        startActivity(i);
        finish();
    }

    public  class SettingsAdapter extends ArrayAdapter<String> {

        // View lookup cache
        private ArrayList<String> users;
        private int[] imgIcons;
        private boolean isSettings;
        Context ctx;
        private  class ViewHolder {
            TextView name;
            TextView home;
        }

        public SettingsAdapter(Context context, ArrayList<String> users, int[] img, boolean value) {
            super(context, R.layout.item_popup, users);
            this.users = users;
            this.ctx = context;
            this.imgIcons = img;
            this.isSettings=value;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Get the data item for this position

            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_popup,parent,false);

                TextView itemNames = (TextView)convertView.findViewById(R.id.txtItemName);
                ImageView imgIcon = (ImageView)convertView.findViewById(R.id.imgIcon);

                if(isSettings){
                    itemNames.setText(users.get(position));
                    int col = Color.parseColor("#D13B3D");
                    imgIcon.setColorFilter(col, PorterDuff.Mode.SRC_ATOP);
                    imgIcon.setImageResource(R.drawable.iconsettings);
                    if(position!=0) {
                        imgIcon.setVisibility(View.INVISIBLE);
                    }
                }else{
                    itemNames.setText(users.get(position));
                    imgIcon.setImageResource(imgIcons[position]);
                }
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (position) {
                        case 4:
                            Intent i = new Intent(SanjeevKapoorScreen.this,DisclaimerScreen.class);
                            startActivity(i);
                            break;
                        case 5:
                            Intent i2 = new Intent(SanjeevKapoorScreen.this,AboutusScreen.class);
                            startActivity(i2);
                            break;
                        case 7:
                            logoutFromApp();
                            break;
                    }
                }
            });

            // Populate the data into the template view using the data object
            // Return the completed view to render on screen
            return convertView;
        }
    }

    public  class MoreAdapter extends ArrayAdapter<String> {

        // View lookup cache
        private ArrayList<String> users;
        private int[] imgIcons;
        private boolean isSettings;
        Context ctx;

        private  class ViewHolder {
            TextView name;
            TextView home;
        }

        public MoreAdapter(Context context, ArrayList<String> users, int[] img, boolean value) {
            super(context, R.layout.item_popup, users);
            this.users = users;
            this.ctx = context;
            this.imgIcons = img;
            this.isSettings=value;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Get the data item for this position

            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_popup,parent,false);

                TextView itemNames = (TextView)convertView.findViewById(R.id.txtItemName);
                ImageView imgIcon = (ImageView)convertView.findViewById(R.id.imgIcon);

                if(isSettings){

                    itemNames.setText(users.get(position));
                    int col = Color.parseColor("#D13B3D");
                    imgIcon.setColorFilter(col, PorterDuff.Mode.SRC_ATOP);
                    imgIcon.setImageResource(R.drawable.iconsettings);
                    if(position!=0) {
                        imgIcon.setVisibility(View.INVISIBLE);
                    }
                }else{

                    itemNames.setText(users.get(position));
                    imgIcon.setImageResource(imgIcons[position]);
                }

                convertView.setTag(viewHolder);

            } else {

                viewHolder = (ViewHolder) convertView.getTag();
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (position){

                        case 5:
                            Intent iGuide = new Intent(SanjeevKapoorScreen.this,GuideLinesMainScreen.class);
                            startActivity(iGuide);
                            break;
                        case 6:
                            Intent i = new Intent(SanjeevKapoorScreen.this,GlossaryScreen.class);
                            startActivity(i);
                            break;
                    }
                }
            });

            return convertView;
        }
    }

    //end of main class
}
