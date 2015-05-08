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

    public static String UPDATE_PROFILE = BASE_URL+"users/userEdit";


    public static String GET_BABY_DETAILS = BASE_URL+"babies/listing/?user_id=";

    public static String DELETE_BABY = BASE_URL+"babies/deletebaby?baby_id=";

    public static String ADD_BABY = BASE_URL+"babies/addBabies";

    public static String UPDATE_BABY = BASE_URL+"babies/editBabies";

    public static String ABOUT_DISCLAIMERS = BASE_URL+"admins/general";

    public static String NUTRITIONAL_GUIDELINES = BASE_URL+"nutritional_guidelines/listing";

}
