package com.example.android.parvarish_nutricalculator.helpers;

import android.content.Context;
import android.util.Log;

import com.example.android.parvarish_nutricalculator.model.glossaryDescription;
import com.example.android.parvarish_nutricalculator.model.glossaryIngredient;
import com.example.android.parvarish_nutricalculator.model.myrecipedata;
import com.example.android.parvarish_nutricalculator.model.recipeData;
import com.example.android.parvarish_nutricalculator.model.recipeIngredient;
import com.example.android.parvarish_nutricalculator.model.recipeSubIngredient;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Android on 24-06-2015.
 */
public class NutritionCalculation {

    private Context _context;
    private myrecipedata recipe;
    private glossaryDescription ingredients_values;
    private float energy = 0f;

    private ArrayList<Float> energies;
    private ArrayList<Float> proteins;
    private ArrayList<Float> fats;
    private ArrayList<Float> calciums;
    private ArrayList<Float> irons;

    public NutritionCalculation(Context _context, myrecipedata recipe, glossaryDescription ingredients_values) {
        this._context = _context;
        this.recipe = recipe;
        this.ingredients_values = ingredients_values;
    }

    public void calculate() {

        energies = new ArrayList<>();
        proteins = new ArrayList<>();
        fats = new ArrayList<>();
        calciums = new ArrayList<>();
        irons = new ArrayList<>();


        try {
            ArrayList<recipeSubIngredient> ingredients = recipe.RecipeIngredientList;

            for (recipeSubIngredient ingredient : ingredients) {

                String cuurentRecipeIngredientID = ingredient.RecipeIngredient.ingredient_id;
                String qty = ingredient.RecipeIngredient.quantity;
                String unit = ingredient.RecipeIngredient.unit;
                float convertedUnit = 0f;
                int quantity = Integer.parseInt(qty);

                if (unit.equalsIgnoreCase("ml")) {
                    convertedUnit = Float.parseFloat(ingredient.RecipeIngredient.quantity);
                    Log.e("Value Of 1 Unit ", "" + convertedUnit);
                } else {

                    JSONObject cookingMeasures = new JSONObject(loadJSONFromAsset());
                    JSONObject innerObj = cookingMeasures.getJSONObject("cookingmeasure");
                    JSONObject particularObject = innerObj.getJSONObject(String.format("%s-%s", ingredient.RecipeIngredient.unit.toLowerCase(), "ml"));
                    convertedUnit = Float.parseFloat(particularObject.getString("1"));
                    Log.e("Value Of 1 Unit ", "" + convertedUnit);
                }


                Log.e("ML of single Ingredient",(convertedUnit*quantity)+" ML");


                for(glossaryIngredient ingredient2 : ingredients_values.returnAllIngredients()){
                    if(cuurentRecipeIngredientID.equalsIgnoreCase(ingredient2.id)) {
                        Log.e("-----ING ENERGY ", ingredient2.energy);
                        Log.e("-----ING PROTEIN ", ingredient2.protein);
                        Log.e("-----ING FAT ", ingredient2.fat);
                        Log.e("-----ING CALCIUM ", ingredient2.calcium);
                        Log.e("-----ING IRON ", ingredient2.iron);
                    }


                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void calculateEnergy(){


    }


    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = _context.getAssets().open("CookingMeasure.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }


}
