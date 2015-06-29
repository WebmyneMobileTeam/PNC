package com.example.android.parvarish_nutricalculator.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Base64;
import android.util.Log;

import com.example.android.parvarish_nutricalculator.custom.ComplexPreferences;
import com.example.android.parvarish_nutricalculator.model.NutritionData;
import com.example.android.parvarish_nutricalculator.model.userModel;

import java.io.ByteArrayOutputStream;

/**
 * Created by xitij on 17-03-2015.
 */
public class PrefUtils {

/*    public static boolean isLightTheme(Context ctx){
        boolean isLight = true;
        int selectedLanguage = Prefs.with(ctx).getInt("selected_theme",0);
        if(selectedLanguage == 0){
            isLight = true;
        }else{
            isLight = false;
        }
        return isLight;
    }*/



    // For Refreshing the activity
    public static void RefreshActivity(Activity act){
        Intent intent = act.getIntent();
        act.overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        act.finish();
        act.overridePendingTransition(0, 0);
        act.startActivity(intent);
    }


    public static String returnBas64Image(Bitmap thumbnail){
        //complete code to save image on server
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String  encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return  encodedImage;
    }

    public static Bitmap returnBitmapImage(String base64){
        Bitmap decodedByte=null;
        try {

            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }catch(Exception e){
            Log.e("##### exc decode image", e.toString());
        }

        return decodedByte;
    }


    public static Typeface getTypeFace(Context ctx){
        Typeface typeface = null;
        typeface = Typeface.createFromAsset(ctx.getAssets(),"Oswald-Regular.ttf");
        return  typeface;
    }

    public static void setCurrentUser(userModel currentUser, Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        complexPreferences.putObject("current_user_value", currentUser);
        complexPreferences.commit();
    }

    public static userModel getCurrentUser(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        userModel currentUser = complexPreferences.getObject("current_user_value", userModel.class);
        return currentUser;
    }

    public static void clearCurrentUser( Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
    }


    public static void setNutritionGuide(NutritionData currentUser, Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "nutrition_prefs", 0);
        complexPreferences.putObject("nutrition_values", currentUser);
        complexPreferences.commit();
    }

    public static NutritionData getNutritionGuide(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "nutrition_prefs", 0);
        NutritionData currentUser = complexPreferences.getObject("nutrition_values", NutritionData.class);
        return currentUser;
    }


}
