package com.example.android.parvarish_nutricalculator.ui;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.custom.ComplexPreferences;
import com.example.android.parvarish_nutricalculator.helpers.API;
import com.example.android.parvarish_nutricalculator.helpers.AdvancedSpannableString;
import com.example.android.parvarish_nutricalculator.helpers.PrefUtils;
import com.example.android.parvarish_nutricalculator.model.NutritionData;
import com.example.android.parvarish_nutricalculator.model.NutritionalSubOption;
import com.facebook.login.LoginManager;
import com.vipul.hp_hp.library.Layout_to_Image;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class GuideLinesSubScreen extends ActionBarActivity {
    ListPopupWindow popupWindow1,popupWindow2;
    private Button btnSaveImageNutritionGuidelines;
    private GridView imageList;
    private Toolbar toolbar;
    private NutritionData nutritionData;
    private String colors[] = {"#0A82C8", "#EA0083", "#F7760E", "#00A045", "#CF332F"};
    private String topics[] = {"To meet Energy and Protein requirements,\nincrease intake of:",
            "To meet Calcium requirements,\nincrease intake of:",
            "To meet Zinc requirements,\nincrease intake of:",
            "To meet Iron requirements,\nincrease intake of:",
            "Foods to Avoid until 1 Year of Age"};
    private int selectedTopic = 0;
    private TextView txtGuideLineSubPageHeading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_lines_sub_screen);
        nutritionData=PrefUtils.getNutritionGuide(GuideLinesSubScreen.this);
        Bundle extra = getIntent().getExtras();
        selectedTopic = extra.getInt("who");
        txtGuideLineSubPageHeading = (TextView) findViewById(R.id.txtGuideLineSubPageHeading);
        btnSaveImageNutritionGuidelines = (Button)findViewById(R.id.btnSaveImageNutritionGuidelines);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("PARVARISH NUTRI CALCULATOR");
//            toolbar.setLogo(R.drawable.logo);
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);

        imageList = (GridView)findViewById(R.id.imageList);

        setupTopicHeading();

        CustomImageAdapter adp = new CustomImageAdapter(GuideLinesSubScreen.this,nutritionData.nutritionalSubOptionArrayList);
        imageList.setAdapter(adp);



        btnSaveImageNutritionGuidelines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSaveImageTOSDCard();
            }
        });
    }

    private void processSaveImageTOSDCard(){

        RelativeLayout r1 = (RelativeLayout)findViewById(R.id.mainRelative);
        View v1 = r1.getRootView();


        v1.setDrawingCacheEnabled(true);
        Bitmap b = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);


// storing the image to sd card
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        // File directory = cw.getDir("Krishna", Context.MODE_PRIVATE);
        // Create imageDir
        String root = Environment.getExternalStorageDirectory().toString();
        String filepath = root+"/Parvarish App/";


        File directory = new File(filepath);
        directory.mkdirs();
        File mypath=new File(directory,"Nutrition Guidelines2"+".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Toast.makeText(GuideLinesSubScreen.this,"Image saved to SD card.",Toast.LENGTH_LONG).show();
            fos.close();

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        } catch (Exception e) {
            Log.e("exc",toString());
            e.printStackTrace();
        }



    }

    private void setupTopicHeading() {

        AdvancedSpannableString topic = new AdvancedSpannableString(topics[selectedTopic]);
        String toBold = "";

        switch (selectedTopic) {

            case 0:

                toBold = "Energy";
                topic.setBold(toBold);
                topic.setCustomSizeOf(1.5f,toBold);

                toBold = "Protein";
                topic.setBold(toBold);
                topic.setCustomSizeOf(1.5f,toBold);

                
                break;

            case 1:
                toBold = "Calcium";
                topic.setBold(toBold);
                topic.setCustomSizeOf(1.5f,toBold);
                break;

            case 2:

                toBold = "Zinc";
                topic.setBold(toBold);
                topic.setCustomSizeOf(1.5f,toBold);

                break;

            case 3:

                toBold = "Iron";
                topic.setBold(toBold);
                topic.setCustomSizeOf(1.5f,toBold);

                break;

            case 4:

                toBold = "Avoid";
                topic.setBold(toBold);
                topic.setCustomSizeOf(1.5f,toBold);

                toBold = "1 Year";
                topic.setBold(toBold);
                topic.setCustomSizeOf(1.5f,toBold);

                break;
        }


        txtGuideLineSubPageHeading.setText(topic);
        txtGuideLineSubPageHeading.setTextColor(Color.parseColor(colors[selectedTopic]));


    }

    class CustomImageAdapter extends BaseAdapter {
        LayoutInflater layoutInflator;
        private Context ctx;
        ArrayList<NutritionalSubOption> nutritionData;
        public CustomImageAdapter(Context ctx,ArrayList<NutritionalSubOption> nutritionData){
            this.ctx = ctx;
            this.nutritionData=nutritionData;
        }

        @Override
        public int getCount() {
            return nutritionData.size();
        }

        @Override
        public Object getItem(int position) {
            return nutritionData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = convertView;
            view = layoutInflator.inflate(R.layout.nutrition_grid_item_view, parent, false);

            ImageView imageView = (ImageView)view.findViewById(R.id.image);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            TextView txtSubtitle= (TextView) view.findViewById(R.id.txtSubtitle);


            txtSubtitle.setText(nutritionData.get(position).name);

           // Log.e("id",nutritionData.get(position).nutritional_guideline_id);
            Log.e("photi url",nutritionData.get(position).photo_url);

            Glide.with(GuideLinesSubScreen.this)
                    .load(API.BASE_URL_IMAGE_FETCH+nutritionData.get(position).photo_url)
                    .into(imageView);
            //imageView.setImageResource(R.mipmap.ic_launcher);

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
        String[] names = {"Settings", "Rate Us on Play Store", "Join Us on Facebook", "Share this App with Friends", "Disclaimers", "About Us", "Feedback", "Logout"};
        int[] drawableImage = {R.drawable.icon_home, R.drawable.drawable_profile, R.drawable.drawable_myrecipes, R.drawable.drawable_diary, R.drawable.drawable_friends, R.drawable.icon_nutritional, R.drawable.icon_gloassary, R.drawable.drawable_tour};
        popupWindow1 = new ListPopupWindow(GuideLinesSubScreen.this);
        popupWindow1.setAnchorView(menuSettings);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        popupWindow1.setWidth((int) (width / 1.5));
        popupWindow1.setHeight((int) (height / 1.5));
        popupWindow1.setModal(true);
        popupWindow1.setAdapter(new SettingsAdapter(GuideLinesSubScreen.this, arrayList, drawableImage,true));
        popupWindow1.show();
    }

    private void openMore() {

        View menuItemView = findViewById(R.id.actionMore); // SAME ID AS MENU ID
        String[] names = {"Home", "Profile", "My Recipes", "Diary", "Friends", "Nutritional Guidelines", "Glossary of Ingredients", "Welcome Tour"};
        int[] drawableImage = {R.drawable.icon_home, R.drawable.drawable_profile, R.drawable.drawable_myrecipes, R.drawable.drawable_diary, R.drawable.drawable_friends, R.drawable.icon_nutritional, R.drawable.icon_gloassary, R.drawable.drawable_tour};
        popupWindow2 = new ListPopupWindow(GuideLinesSubScreen.this);

        popupWindow2.setListSelector(new ColorDrawable());
        popupWindow2.setAnchorView(menuItemView);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        popupWindow2.setWidth((int) (width / 1.5));
        popupWindow2.setHeight((int) (height / 1.5));
        popupWindow2.setModal(true);
        popupWindow2.setAdapter(new MoreAdapter(GuideLinesSubScreen.this, arrayList, drawableImage, false));
        popupWindow2.show();


    }

    private void logoutFromApp() {

        Log.e("click", "logout");
        PrefUtils.clearCurrentUser(GuideLinesSubScreen.this);
        LoginManager.getInstance().logOut();


        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isUserLogin", false);
        editor.commit();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(GuideLinesSubScreen.this, "user_pref", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
        Intent i = new Intent(GuideLinesSubScreen.this, StartScreen.class);
        startActivity(i);
        finish();
    }

    public class SettingsAdapter extends ArrayAdapter<String> {

        // View lookup cache
        private ArrayList<String> users;
        private int[] imgIcons;
        private boolean isSettings;
        Context ctx;

        private class ViewHolder {
            TextView name;
            TextView home;
        }

        public SettingsAdapter(Context context, ArrayList<String> users, int[] img, boolean value) {
            super(context, R.layout.item_popup, users);
            this.users = users;
            this.ctx = context;
            this.imgIcons = img;
            this.isSettings = value;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Get the data item for this position

            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_popup, parent, false);

                TextView itemNames = (TextView) convertView.findViewById(R.id.txtItemName);
                ImageView imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);

                if (isSettings) {
                    itemNames.setText(users.get(position));
                    int col = Color.parseColor("#D13B3D");
                    imgIcon.setColorFilter(col, PorterDuff.Mode.SRC_ATOP);
                    imgIcon.setImageResource(R.drawable.iconsettings);
                    if (position != 0) {
                        imgIcon.setVisibility(View.INVISIBLE);
                    }
                } else {
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
                            popupWindow1.dismiss();
                            Intent i = new Intent(GuideLinesSubScreen.this, DisclaimerScreen.class);
                            startActivity(i);
                            break;
                        case 5:
                            popupWindow1.dismiss();
                            Intent i2 = new Intent(GuideLinesSubScreen.this, AboutusScreen.class);
                            startActivity(i2);
                            break;
                        case 7:
                            popupWindow1.dismiss();
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

    public class MoreAdapter extends ArrayAdapter<String> {

        // View lookup cache
        private ArrayList<String> users;
        private int[] imgIcons;
        private boolean isSettings;
        Context ctx;

        private class ViewHolder {
            TextView name;
            TextView home;
        }

        public MoreAdapter(Context context, ArrayList<String> users, int[] img, boolean value) {
            super(context, R.layout.item_popup, users);
            this.users = users;
            this.ctx = context;
            this.imgIcons = img;
            this.isSettings = value;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Get the data item for this position

            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_popup, parent, false);

                TextView itemNames = (TextView) convertView.findViewById(R.id.txtItemName);
                ImageView imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);

                if (isSettings) {

                    itemNames.setText(users.get(position));
                    int col = Color.parseColor("#D13B3D");
                    imgIcon.setColorFilter(col, PorterDuff.Mode.SRC_ATOP);
                    imgIcon.setImageResource(R.drawable.iconsettings);
                    if (position != 0) {
                        imgIcon.setVisibility(View.INVISIBLE);
                    }
                } else {

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
                        case 0:
                            popupWindow2.dismiss();
                            Intent pro1 = new Intent(GuideLinesSubScreen.this, HomeScreen.class);
                            startActivity(pro1);
                            break;
                        case 1:
                            popupWindow2.dismiss();
                            Intent pro = new Intent(GuideLinesSubScreen.this, ProfileScreen.class);
                            startActivity(pro);
                            break;
                        case 2:
                            popupWindow2.dismiss();
                            Intent myrecipe = new Intent(GuideLinesSubScreen.this, MyRecipeListScreen.class);
                            startActivity(myrecipe);
                            break;
                        case 3:
                            popupWindow2.dismiss();
                            Intent diary = new Intent(GuideLinesSubScreen.this, DiaryScreen.class);
                            startActivity(diary);
                            break;

                        case 4:
                            popupWindow2.dismiss();
                            Intent iGuide1 = new Intent(GuideLinesSubScreen.this, FriendsScreen.class);
                            startActivity(iGuide1);
                            break;
                        case 5:
                            popupWindow2.dismiss();
                            Intent iGuide = new Intent(GuideLinesSubScreen.this, GuideLinesMainScreen.class);
                            startActivity(iGuide);
                            break;
                        case 6:
                            popupWindow2.dismiss();
                            Intent i = new Intent(GuideLinesSubScreen.this, GlossaryScreenTemp.class);
                            startActivity(i);
                            break;
                        case 7:
                            Intent intent = new Intent(GuideLinesSubScreen.this, WalkThorugh.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            break;
                    }
                }
            });

            return convertView;
        }
    }


   //end of main class
}
