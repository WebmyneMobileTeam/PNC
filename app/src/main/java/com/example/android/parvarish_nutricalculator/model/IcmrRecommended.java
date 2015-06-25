package com.example.android.parvarish_nutricalculator.model;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Android on 07-05-2015.
 */
public class IcmrRecommended {

    @SerializedName("id")
    public String id;

    @SerializedName("age_group")
    public String age_group;

    @SerializedName("energy")
    public String energy;

    @SerializedName("protein")
    public String protein;


    @SerializedName("calcium")
    public String calcium;

    @SerializedName("iron")
    public String iron;

    @SerializedName("zinc")
    public String zinc;

    @SerializedName("fat")
    public String fat;

    @SerializedName("carotene")
    public String carotene;

    @SerializedName("folic_acid")
    public String folic_acid;

    @SerializedName("vitaminc")
    public String vitaminc;

    @SerializedName("carbohydrates")
    public String carbohydrates;
}
