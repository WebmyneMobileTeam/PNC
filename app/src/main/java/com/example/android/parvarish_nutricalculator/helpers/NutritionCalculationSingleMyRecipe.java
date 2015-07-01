package com.example.android.parvarish_nutricalculator.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.parvarish_nutricalculator.model.glossaryDescription;
import com.example.android.parvarish_nutricalculator.model.glossaryIngredient;
import com.example.android.parvarish_nutricalculator.model.recipeIngredient;
import com.example.android.parvarish_nutricalculator.model.recipeSubIngredient;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Android on 24-06-2015.
 */
public class NutritionCalculationSingleMyRecipe {

    private Context _context;
    private ArrayList<recipeSubIngredient> recipesIng;
    private glossaryDescription ingredients_values;
    private float energy = 0f;

    private ArrayList<Float> energies;
    private ArrayList<Float> proteins;
    private ArrayList<Float> fats;
    private ArrayList<Float> calciums;
    private ArrayList<Float> irons;

    private ArrayList<Float> inner_energies;
    private ArrayList<Float> inner_proteins;
    private ArrayList<Float> inner_fats;
    private ArrayList<Float> inner_calciums;
    private ArrayList<Float> inner_irons;

    float ICMR_RATIO_ENERGY = 0f;
    float ICMR_RATIO_PROTEIN = 0f;
    float ICMR_RATIO_CALCIUM = 0f;
    float ICMR_RATIO_FAT = 0f;
    float ICMR_RATIO_IRON = 0f;

    private ProgressDialog pd;
    private OnCalculationResult onCalculationResult;
    private int NO_OF_SERVINGS=1;

    public NutritionCalculationSingleMyRecipe(Context _context, ArrayList<recipeSubIngredient> recipeing, glossaryDescription ingredients_values,String servings) {
        this._context = _context;
        this.recipesIng = recipeing;
        this.ingredients_values = ingredients_values;
        this.NO_OF_SERVINGS = Integer.valueOf(servings);

    }

    public void startCalculation(){

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pd = ProgressDialog.show(_context,"Please wait","Calculating",false);
                energies = new ArrayList<>();
                proteins = new ArrayList<>();
                fats = new ArrayList<>();
                calciums = new ArrayList<>();
                irons = new ArrayList<>();

            }

            @Override
            protected Void doInBackground(Void... params) {

                    calculate();


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                pd.dismiss();
                onCalculationResult.onResult(returnOverAllEnergy()/NO_OF_SERVINGS, returnOverAllProtien()/NO_OF_SERVINGS, returnOverAllFat()/NO_OF_SERVINGS, returnOverAllCalcium()/NO_OF_SERVINGS, returnOverAllIron()/NO_OF_SERVINGS);
            }
        }.execute();


    }

    private void calculate( ) {

        inner_energies = new ArrayList<>();
        inner_proteins = new ArrayList<>();
        inner_fats = new ArrayList<>();
        inner_calciums = new ArrayList<>();
        inner_irons = new ArrayList<>();

        try {

            for (recipeSubIngredient ingredient : recipesIng) {

                String cuurentRecipeIngredientID = ingredient.RecipeIngredient.ingredient_id;
                String qty = ingredient.RecipeIngredient.quantity;
                String unit = ingredient.RecipeIngredient.unit;
                float convertedUnit = 0f;
                float quantity = Float.parseFloat(qty);



                    try {
                        JSONObject cookingMeasures = new JSONObject(loadJSONFromAsset());
                        JSONObject innerObj = cookingMeasures.getJSONObject("cookingmeasure");
                        JSONObject particularObject = innerObj.getJSONObject(String.format("%s", ingredient.RecipeIngredient.unit.toLowerCase()));
                        convertedUnit = Float.parseFloat(particularObject.getString("1"));
                    }catch (Exception e){
                        convertedUnit = 1;
                        Log.e("$$ Exc value not found",e.toString());

                    }



                Log.e("Value Of Quantity ", "" + quantity);
                Log.e("Value Of 1 Unit ", "" + convertedUnit);
                Log.e("ML of single Ingredient", (convertedUnit * quantity) + " ML");

                for (glossaryIngredient ingredient2 : ingredients_values.returnAllIngredients()) {

                    if (cuurentRecipeIngredientID.equalsIgnoreCase(ingredient2.id)) {
                        Log.e("-----ING ENERGY ", ingredient2.energy);
                        Log.e("-----ING PROTEIN ", ingredient2.protein);
                        Log.e("-----ING FAT ", ingredient2.fat);
                        Log.e("-----ING CALCIUM ", ingredient2.calcium);
                        Log.e("-----ING IRON ", ingredient2.iron);

                        // energy
                        float energyFor100GM = Float.parseFloat(ingredient2.energy);
                        float energyFor1GM = energyFor100GM/100;
                        float currentIngredientEnergy = (convertedUnit * quantity) * energyFor1GM;
                        inner_energies.add(currentIngredientEnergy);

                        // protien
                        float protienFor100GM = Float.parseFloat(ingredient2.protein);
                        float protienFor1GM = protienFor100GM/100;
                        float currentIngredientProtien = (convertedUnit * quantity) * protienFor1GM;
                        inner_proteins.add(currentIngredientProtien);

                        // calcium
                        float calciumFor100GM = Float.parseFloat(ingredient2.calcium);
                        float calciumFor1GM = calciumFor100GM/100;
                        float currentIngredientCalcium = (convertedUnit * quantity) * calciumFor1GM;
                        inner_calciums.add(currentIngredientCalcium);

                        // fat
                        float fatFor100GM = Float.parseFloat(ingredient2.fat);
                        float fatFor1GM = fatFor100GM/100;
                        float currentIngredientFat = (convertedUnit * quantity) * fatFor1GM;
                        inner_fats.add(currentIngredientFat);

                        // iron
                        float ironFor100GM = Float.parseFloat(ingredient2.iron);
                        float ironFor1GM = ironFor100GM/100;
                        float currentIngredientIron = (convertedUnit * quantity) * ironFor1GM;
                        inner_irons.add(currentIngredientIron);

                    }
                }



            }

            Log.e("ENERGIES =",""+returnOverAllInnerEnergy());

            energies.add(returnOverAllInnerEnergy());
            calciums.add(returnOverAllInnerCalcium());
            proteins.add( returnOverAllInnerProtien());
            irons.add(returnOverAllInnerIron());
            fats.add(returnOverAllInnerFat());

        } catch (Exception e) {
            Log.e("##### Exec",e.toString());
            e.printStackTrace();
        }

    }

    public void setOnCalculationResult(OnCalculationResult listner){
        this.onCalculationResult = listner;
    }

    private void calculateEnergy() {


    }

    private float returnOverAllEnergy(){
        float totalEnergy = 0f;

        for(Float f : energies){
            totalEnergy = totalEnergy + f;
        }
        return totalEnergy;
    }

    private float returnOverAllInnerEnergy(){
        float totalEnergy = 0f;

        for(Float f : inner_energies){
            totalEnergy = totalEnergy + f;
        }
        return totalEnergy;
    }

    private float returnOverAllCalcium(){
        float totalCalcium = 0f;

        for(Float f : calciums){
            totalCalcium = totalCalcium + f;
        }
        return totalCalcium;
    }

    private float returnOverAllInnerCalcium(){
        float totalCalcium = 0f;

        for(Float f : inner_calciums){
            totalCalcium = totalCalcium + f;
        }
        return totalCalcium;
    }


    private float returnOverAllProtien(){
        float totalProtien = 0f;

        for(Float f : proteins){
            totalProtien = totalProtien + f;
        }
        return totalProtien;
    }

    private float returnOverAllInnerProtien(){
        float totalProtien = 0f;

        for(Float f : inner_proteins){
            totalProtien = totalProtien + f;
        }
        return totalProtien;
    }

    private float returnOverAllFat(){
        float totalFat = 0f;

        for(Float f : fats){
            totalFat = totalFat + f;
        }
        return totalFat;
    }

    private float returnOverAllInnerFat(){
        float totalFat = 0f;

        for(Float f : inner_fats){
            totalFat = totalFat + f;
        }
        return totalFat;
    }

    private float returnOverAllIron(){
        float totalIron = 0f;

        for(Float f : irons){
            totalIron = totalIron + f;
        }
        return totalIron;
    }

    private float returnOverAllInnerIron(){
        float totalIron = 0f;

        for(Float f : inner_irons){
            totalIron = totalIron + f;
        }
        return totalIron;
    }


    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = _context.getAssets().open("NewCookingMeasure.json");

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

    public interface OnCalculationResult{

        public void onResult(float energy, float protien, float fat, float calcium, float iron);

    }



}
