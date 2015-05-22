package com.example.android.parvarish_nutricalculator.model;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Android on 07-05-2015.
 */
public class freindsubModel {

    @SerializedName("Friend")
    public freindData  Friend;


    @SerializedName("User")
    public userData  User;


    @SerializedName("FriendUser")
    public userData  FriendUser;

}
