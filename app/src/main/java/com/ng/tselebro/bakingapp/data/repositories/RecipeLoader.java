package com.ng.tselebro.bakingapp.data.repositories;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.ng.tselebro.bakingapp.Model.POJO.Ingredient;

import com.ng.tselebro.bakingapp.Model.POJO.Recipe;
import com.ng.tselebro.bakingapp.Model.POJO.Step;
import com.ng.tselebro.bakingapp.data.local.RecipeColumns;

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
        List<Ingredient> ingredientList = new ArrayList<>();
        List<Step> stepList = new ArrayList<>();

        try {

            Cursor c = getContext().getContentResolver().query(Recipes.CONTENT_RECIPES_URI,
                    null,
                    null,
                    null,
                    null);
            if (c != null && c.getCount() > 0){
                while (c.moveToNext()){
                    int stepId  = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(RecipeColumns.id)));
                    String measure = c.getString(c.getColumnIndexOrThrow(RecipeColumns.Measure));
                    float Quantiy = Float.parseFloat(c.getString(c.getColumnIndexOrThrow(RecipeColumns.Quantity)));
                    String Ingredient  = c.getString(c.getColumnIndexOrThrow(RecipeColumns.ingredient));
                    String RecipeName = c.getString(c.getColumnIndexOrThrow(RecipeColumns.recipeName));
                    int  servings = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(RecipeColumns.servings)));
                    String image_url = c.getString(c.getColumnIndexOrThrow(RecipeColumns.image_url));
                    String shortDescription = c.getString(c.getColumnIndexOrThrow(RecipeColumns.shortDescription));
                    String longDescription = c.getString(c.getColumnIndexOrThrow(RecipeColumns.longDescription));
                    String video_url = c.getString(c.getColumnIndexOrThrow(RecipeColumns.mVideourl));
                    String thumbnailurl = c.getString(c.getColumnIndexOrThrow(RecipeColumns.thumbnailUrl));

                    Recipe recipe = new Recipe();
                    Ingredient ingredients = new Ingredient();
                    Step steps = new Step();

                    //Add the steps objects to the arrayList
                    steps.setId(stepId);
                    steps.setShortDescription(shortDescription);
                    steps.setDescription(longDescription);
                    steps.setThumbnailURL(thumbnailurl);
                    steps.setVideoURL(video_url);
                    stepList.add(steps);
                    recipe.setSteps(stepList);

                    //Add the ingredient objects to the array list
                    ingredients.setIngredient(Ingredient);
                    ingredients.setMeasure(measure);
                    ingredients.setQuantity(Quantiy);
                    ingredientList.add(ingredients);
                    recipe.setIngredients(ingredientList);

//                    Add the recipe details to List
                    recipe.setImage(image_url);
                    recipe.setName(RecipeName);
                    recipe.setServings(servings);
                    recipes.add(recipe);

                    Log.d("RecipeLoader" , "The amount of data returned" + c);
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
