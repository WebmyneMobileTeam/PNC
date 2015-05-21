package com.example.android.parvarish_nutricalculator.helpers;

/**
 * Created by Android on 03-04-2015.
 */
public interface POSTResponseListener {

    public String onPost(String msg);
    public void onPreExecute();
    public void onBackground();
}
