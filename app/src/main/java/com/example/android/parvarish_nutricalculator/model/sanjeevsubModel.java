package com.example.android.parvarish_nutricalculator.model;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Android on 07-05-2015.
 */
public class sanjeevsubModel {

    @SerializedName("Recipe")
    public recipeSubData Recipe;

    @SerializedName("Baby")
    public babyData Baby;

    @SerializedName("User")
    public userData User;

    @SerializedName("RecipeIngredient")
    public ArrayList<recipeIngredient> RecipeIngredient;

}
