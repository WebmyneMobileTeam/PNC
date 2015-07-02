package com.example.android.parvarish_nutricalculator.model;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 07-05-2015.
 */
public class glossaryIngredient {

    @SerializedName("id")
    public String id;

    @SerializedName("ingredient_category_id")
    public String ingredient_category_id;

    @SerializedName("name")
    public String name;

    @SerializedName("details")
    public String details;


    @SerializedName("nutients_benefits")
    public String nutients_benefits;

    @SerializedName("energy")
    public String energy;

    @SerializedName("carbohydrates")
    public String carbohydrates;

    @SerializedName("protein")
    public String protein;

    @SerializedName("fat")
    public String fat;

    @SerializedName("perunit_wise_grams")
    public String perunit_wise_grams;

    @SerializedName("calcium")
    public String calcium;

    @SerializedName("iron")
    public String iron;

    @SerializedName("carotene")
    public String carotene;

    @SerializedName("folic_acid")
    public String folic_acid;

    @SerializedName("vitaminc")
    public String vitaminc;

    @SerializedName("zinc")
    public String zinc;

    @SerializedName("created")
    public String created;

    @SerializedName("modified")
    public String modified;

}
