package com.example.android.parvarish_nutricalculator.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Android on 08-05-2015.
 */
public class NutritionDetailClass {

    @SerializedName("data")
    public ArrayList<NutritionData> nutritionData;

}


