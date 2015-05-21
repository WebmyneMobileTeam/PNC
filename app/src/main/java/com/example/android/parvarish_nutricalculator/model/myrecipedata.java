package com.example.android.parvarish_nutricalculator.model;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Android on 07-05-2015.
 */
public class myrecipedata {


    @SerializedName("Babies")
    public ArrayList<babyData> Babies;

    @SerializedName("id")
    public String id;

    @SerializedName("baby_id")
    public String baby_id;

    @SerializedName("user_id")
    public String user_id;

    @SerializedName("name")
    public String name;


    @SerializedName("method")
    public String method;

    @SerializedName("ingredients_details")
    public String ingredients_details;

    @SerializedName("sanjeev_kapoor_receipe")
    public String sanjeev_kapoor_receipe;

    @SerializedName("regional_food_receipe")
    public String regional_food_receipe;

    @SerializedName("age_group")
    public String age_group;

    @SerializedName("no_of_servings")
    public String no_of_servings;

    @SerializedName("photo_url")
    public String photo_url;

    @SerializedName("created")
    public String created;

    @SerializedName("modified")
    public String modified;
}
