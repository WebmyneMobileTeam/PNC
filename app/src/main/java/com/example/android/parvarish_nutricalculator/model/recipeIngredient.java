package com.example.android.parvarish_nutricalculator.model;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 07-05-2015.
 */
public class recipeIngredient {

    @SerializedName("id")
    public String id;

    @SerializedName("ingredient_id")
    public String ingredient_id;

    @SerializedName("quantity")
    public String quantity;

    @SerializedName("unit")
    public String unit;


    @SerializedName("receipe_id")
    public String receipe_id;

    @SerializedName("created")
    public String created;

    @SerializedName("modified")
    public String modified;


}
