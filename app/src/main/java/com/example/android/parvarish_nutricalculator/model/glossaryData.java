package com.example.android.parvarish_nutricalculator.model;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Android on 07-05-2015.
 */
public class glossaryData {

    @SerializedName("IngredientCategory")
    public glossaryIngredientCategory IngredientCategory;

    @SerializedName("Ingredient")
    public ArrayList<glossaryIngredient> Ingredient;

}
