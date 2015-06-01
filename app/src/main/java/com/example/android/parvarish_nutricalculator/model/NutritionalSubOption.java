package com.example.android.parvarish_nutricalculator.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 08-05-2015.
 */
public class NutritionalSubOption {

    @SerializedName("id")
    public String id;

    @SerializedName("nutritional_guideline_id")
    public String nutritional_guideline_id;

    @SerializedName("photo_url")
    public String photo_url;

    @SerializedName("name")
    public String name;

    @SerializedName("created")
    public String created;
    @SerializedName("modified")
    public String modified;

}
