package com.example.android.parvarish_nutricalculator.helpers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 22-04-2015.
 */
public class User {

    @SerializedName("name")
    public String name;
    @SerializedName("password")
    public String password;
    @SerializedName("email")
    public String email;
    @SerializedName("phone")
    public String phone;
    @SerializedName("city")
    public String city;
    @SerializedName("facebookID")
    public String facebookID;
    @SerializedName("profilePicture")
    public String profilePicture;
    @SerializedName("gender")
    public String gender;
    @SerializedName("fbEmailID")
    public String fbEmailID;

}
