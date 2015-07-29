package com.example.android.parvarish_nutricalculator.helpers;

import android.content.Context;
import android.util.Log;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.net.ConnectException;

/**
 * Created by Android on 26-06-2015.
 */
public class AGE_GROUP {

    String Baby_date;

    public AGE_GROUP(String baby_date) {
        Baby_date = baby_date;
    }

    public String getAgeGroup(){
        String age_group="";


        LocalDate birthdate = new LocalDate (Baby_date);          //Birth date
        LocalDate now = new LocalDate();                    //Today's date
        Period period = new Period(birthdate, now, PeriodType.yearMonthDay());

        int DAYS = period.getDays();
        int MONTHS = period.getMonths();
        int YEARS = period.getYears();

        Log.e("My DOB - ", "" + Baby_date);
        Log.e("Cuurent date - ", "" + now);
        //Now access the values as below
        Log.e("Days - ", "" + DAYS);
        Log.e("Months -", "" + MONTHS);
        Log.e("Years -", "" + YEARS);

        if(YEARS >= 1){
            age_group = "12-24 Months";
        }else if((YEARS==0)&&(MONTHS>=10 && MONTHS <=12)){
            age_group = "10-12 Months";
        }else if((YEARS==0)&&(MONTHS>=8 && MONTHS <=10)){
            age_group = "8-10 Months";
        }else if((YEARS==0)&&(MONTHS>=6 && MONTHS <=8)){
            age_group = "6-8 Months";
        }else{
            age_group = "0-6 Months";
        }

        return age_group;
    }

    public String getAge(){
        String age="";

        LocalDate birthdate = new LocalDate (Baby_date);          //Birth date
        LocalDate now = new LocalDate();                    //Today's date
        Period period = new Period(birthdate, now, PeriodType.yearMonthDay());

        int DAYS = period.getDays();
        int MONTHS = period.getMonths();
        int YEARS = period.getYears();

        Log.e("My DOB - ", "" + Baby_date);
        Log.e("Cuurent date - ", "" + now);
        //Now access the values as below
        Log.e("Days - ", "" + DAYS);
        Log.e("Months -", "" + MONTHS);
        Log.e("Years -", "" + YEARS);

        if((YEARS >= 1)&&(MONTHS ==0)){
            age = ""+YEARS+" Year(s)";
        }else if((YEARS >= 1)&&(MONTHS !=0)){
            age = ""+YEARS+" Year(s),"+MONTHS+" Month(s)";
        }else if((YEARS == 0)&&(MONTHS !=0)){
            age = ""+MONTHS+" Month(s)";
        }else if((YEARS == 0)&&(MONTHS ==0)){
            age = ""+DAYS+" Day(s)";
        }

        return age;
    }
}
