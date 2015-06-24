package com.example.android.parvarish_nutricalculator.model;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Android on 07-05-2015.
 */
public class glossaryDescription {

    @SerializedName("data")
    public ArrayList<glossaryData> data;

    public ArrayList<glossaryIngredient> returnAllIngredients(){
        ArrayList<glossaryIngredient> arrayList = new ArrayList<>();
        for(glossaryData ingredient : data){
            arrayList.addAll(ingredient.Ingredient);
        }
        return arrayList;
    }
}
