package com.example.android.parvarish_nutricalculator.helpers;

/**
 * Created by Android on 04-05-2015.
 */
public class API {


    // Basic url for all api link
    public static String BASE_URL = "http://www.hackaholicit.com/parvarish/manager/";

    // Login webservice.
    public static String LOGIN = BASE_URL+"users/login";

    // Registration webservice.
    public static String REGISTRATION = BASE_URL+"users/register";

    public static String GET_PROFILE = BASE_URL+"users/profile/?user_id=";

    public static String FORGOT_PASSWORD = BASE_URL+"users/forgetPassword";


}
