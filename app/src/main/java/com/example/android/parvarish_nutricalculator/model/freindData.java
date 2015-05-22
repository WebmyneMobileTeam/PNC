package com.example.android.parvarish_nutricalculator.model;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Android on 07-05-2015.
 */
public class freindData {

    @SerializedName("id")
    public String  id;

    @SerializedName("user_id")
    public String  user_id;


    @SerializedName("friend_id")
    public String  friend_id;

    @SerializedName("type")
    public String  type;

    @SerializedName("email")
    public String  email;


    @SerializedName("status")
    public String  status;

    @SerializedName("created")
    public String  created;

    @SerializedName("modified")
    public String  modified;
}
