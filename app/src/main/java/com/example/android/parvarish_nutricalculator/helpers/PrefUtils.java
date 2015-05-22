package com.example.android.parvarish_nutricalculator.helpers;

import android.content.Context;
import android.graphics.Typeface;

import com.example.android.parvarish_nutricalculator.custom.ComplexPreferences;
import com.example.android.parvarish_nutricalculator.model.NutritionData;
import com.example.android.parvarish_nutricalculator.model.userModel;

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
