package com.example.android.parvarish_nutricalculator.helpers;

/**
 * Created by Android on 29-05-2015.
 */
public class ConversionTable {
    public ConversionTable(){
    }

    public static double CalculateEnergy(String qty,String unit,double energy){

        double finalQuantity = returnQuantity(qty);
        double finalUnit = returnUnit(unit);

        double energyFor1 = energy / 100;

        double finalEnergy = 0.0;
        finalEnergy = finalQuantity * finalUnit * energyFor1;


        return finalEnergy;
    }


    private static double returnQuantity(String qty){
        double qty1=0.0;

        if(qty.equalsIgnoreCase("Unit")){

        }else if(qty.equalsIgnoreCase("ML")){

        }else if(qty.equalsIgnoreCase("GM")){

        }else if(qty.equalsIgnoreCase("Pinch")){

        }else if(qty.equalsIgnoreCase("Handful")){

        }else if(qty.equalsIgnoreCase("Cup")){
            qty1 = 250;
        }else if(qty.equalsIgnoreCase("Teaspoon")){

        }else if(qty.equalsIgnoreCase("Tablespoon")){

        }

        return qty1;
    }

    private static double returnUnit(String unit){
        double unitinDouble=0.0;
        if(unit.equalsIgnoreCase("1/2")){
            unitinDouble = 0.50;
        }else if(unit.equalsIgnoreCase("1/4")){
            unitinDouble = 0.75;
        }else if(unit.equalsIgnoreCase("1")){
            unitinDouble = 1;
        }else if(unit.equalsIgnoreCase("2")){
            unitinDouble = 2;
        }else if(unit.equalsIgnoreCase("3")){
            unitinDouble = 3;
        }else if(unit.equalsIgnoreCase("4")){
            unitinDouble = 4;
        }else if(unit.equalsIgnoreCase("5")){
            unitinDouble = 5;
        }else if(unit.equalsIgnoreCase("6")){
            unitinDouble = 6;
        }else if(unit.equalsIgnoreCase("7")){
            unitinDouble = 7;
        }else if(unit.equalsIgnoreCase("8")){
            unitinDouble = 8;
        }else if(unit.equalsIgnoreCase("9")){
            unitinDouble = 9;
        }else if(unit.equalsIgnoreCase("10")){
            unitinDouble = 10;
        }
            return unitinDouble;
    }
}
