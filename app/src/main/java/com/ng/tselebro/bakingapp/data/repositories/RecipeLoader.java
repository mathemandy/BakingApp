package com.ng.tselebro.bakingapp.data.repositories;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import com.ng.tselebro.bakingapp.Model.Recipe;
import com.ng.tselebro.bakingapp.Model.Step;
import com.ng.tselebro.bakingapp.Model.Ingredient;
import com.ng.tselebro.bakingapp.data.local.RecipeColumns;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.ng.tselebro.bakingapp.data.local.RecipeProvider.Recipes;


public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {

    private List<Recipe> mRecipeList = null;
    private ContentObserver mRecipeObserver;

    public RecipeLoader(Context context) {
        super(context);
    }


    @Override
    protected void onStartLoading() {
     if (mRecipeList != null){
         deliverResult(mRecipeList);
     }

     if (mRecipeObserver == null){
         mRecipeObserver = new ContentObserver(new Handler()) {
             @Override
             public void onChange(boolean selfChange) {
                 onContentChanged();
                 super.onChange(selfChange);
             }
         };
         getContext().getContentResolver().registerContentObserver(Recipes.CONTENT_RECIPES_URI, true, mRecipeObserver);
     }

     if (takeContentChanged() || mRecipeList == null){
         forceLoad();
     }
    }

    @Override
    public List<Recipe> loadInBackground() {
        List<Recipe> recipes = new ArrayList<>();


        try {

            Cursor c = getContext().getContentResolver().query(Recipes.CONTENT_RECIPES_URI,
                    null,
                    null,
                    null,
                    null);
            if (c != null && c.getCount() > 0){
                while (c.moveToNext()){
                    int id = Integer.valueOf(c.getString(c.getColumnIndexOrThrow(RecipeColumns.ID)));
                    String IngredientJson  = c.getString(c.getColumnIndexOrThrow(RecipeColumns.ingredient));
                    String RecipeName = c.getString(c.getColumnIndexOrThrow(RecipeColumns.recipeName));
                    int  servings =Integer.parseInt(c.getString(c.getColumnIndexOrThrow(RecipeColumns.servings)));
                    String image_url = c.getString(c.getColumnIndexOrThrow(RecipeColumns.image_url));
                    String stepsJson = c.getString (c.getColumnIndexOrThrow(RecipeColumns.steps));


                    Recipe recipe = new Recipe();
                    //do sth


                    Type collectionIngredient = new TypeToken<List<Ingredient>>(){}.getType();
                    List<Ingredient> ingredientsJson = new Gson().fromJson(IngredientJson, collectionIngredient);


                    Type collectionSteps = new TypeToken<List<Step>>(){}.getType();
                    List<Step> StepJson = new Gson().fromJson(stepsJson, collectionSteps);

//                    Add the recipe details to List
                    recipe.setId(id);
                    recipe.setImage(image_url);
                    recipe.setName(RecipeName);
                    recipe.setServings(servings);
                    recipe.setSteps(StepJson);
                    recipe.setIngredients(ingredientsJson);

                    //do sth
                    recipes.add(recipe);

                    Log.d("RecipeLoader" , "The amount of data returned" + c.getCount());
                }
            }
            if (c != null){
                c.close();


            }
        }catch (Exception e){
            e.printStackTrace();
        }
return recipes;
    }

    @Override
    public void deliverResult(List<Recipe> data) {
        if (isReset()) {
            mRecipeList = null;

        } else  if (isStarted()){
            super.deliverResult(data);
            mRecipeList= data;
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        mRecipeList = null;
//        Stop watching for recipe changes
        if (mRecipeObserver != null){
            getContext().getContentResolver().unregisterContentObserver(mRecipeObserver);
            mRecipeObserver = null;

        }
    }
}
