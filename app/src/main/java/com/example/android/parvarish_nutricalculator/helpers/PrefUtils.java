package com.example.android.parvarish_nutricalculator.helpers;

import android.content.Context;
import android.graphics.Typeface;

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


}
