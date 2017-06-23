package com.ng.tselebro.bakingapp.data.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.util.LongSparseArray;
import android.util.Log;

import com.google.common.collect.ImmutableList;
import com.ng.tselebro.bakingapp.Model.POJO.Ingredient;
import com.ng.tselebro.bakingapp.Model.POJO.Recipe;
import com.ng.tselebro.bakingapp.Model.POJO.Step;
import com.ng.tselebro.bakingapp.data.local.RecipeColumns;
import com.ng.tselebro.bakingapp.data.local.RecipeProvider;
import com.ng.tselebro.bakingapp.recipe.RecipeContract;

import java.util.List;
import java.util.Vector;

import static com.google.common.base.Preconditions.checkNotNull;


public class CachedRecipesRepository implements RepositoryContract.RecipeRepository {


   private final RecipeServiceApi mRecipeServiceApi;
    public static final String LOG_TAG = CachedRecipesRepository.class.getSimpleName();
    @VisibleForTesting
    private List<Recipe>  mCachedRecipe;


    public CachedRecipesRepository(RecipeServiceApi mRecipeServiceApi) {
        this.mRecipeServiceApi = checkNotNull(mRecipeServiceApi);

    }

    @Override
    public void loadRecipes(@NonNull LoadRecipesCallback callback, Context context) {
     checkNotNull(callback);
     //Load from Api only if needed
     if (mCachedRecipe == null){
      getRecipe(callback, context);
     }else {
      callback.onRecipesLoaded(mCachedRecipe);
     }

    }


 private void getRecipe(@NonNull final LoadRecipesCallback callback, Context context){
     mRecipeServiceApi.getALlRecipes(new RecipeServiceApi.RecipeServiceCallback<List<Recipe>>() {
      @Override
      public void onLoaded(List<Recipe> recipe) {


          mCachedRecipe= ImmutableList.copyOf(recipe);



          ContentValues[] cvArray = new ContentValues[mCachedRecipe.size()];

       for (Recipe freshRecipe: mCachedRecipe){

           ContentValues recipevalue = new ContentValues();
           recipevalue.put(RecipeColumns.image_url, (freshRecipe.getImage()));
           recipevalue.put(RecipeColumns.recipeName, freshRecipe.getName());
           recipevalue.put(RecipeColumns.servings, Integer.toString(freshRecipe.getServings()));

           List<Ingredient> ingredients = freshRecipe.getIngredients();
           for (Ingredient ingredientObj : ingredients)
           {

               recipevalue.put(RecipeColumns.ingredient, ingredientObj.getIngredient());
               recipevalue.put(RecipeColumns.Measure, ingredientObj.getMeasure());
               recipevalue.put(RecipeColumns.Quantity, Float.toString(ingredientObj.getQuantity()));

           }

           List<Step> stepList = freshRecipe.getSteps();

          for ( Step stepObj : stepList) {

              recipevalue.put(RecipeColumns.id, Integer.toString(stepObj.getId()));
              recipevalue.put(RecipeColumns.shortDescription, stepObj.getShortDescription());
              recipevalue.put(RecipeColumns.longDescription, stepObj.getDescription());
              recipevalue.put(RecipeColumns.thumbnailUrl, stepObj.getThumbnailURL());
              recipevalue.put(RecipeColumns.mVideourl, stepObj.getVideoURL());
          }

          cvArray[mCachedRecipe.indexOf(freshRecipe)] = recipevalue;
       }
       int inserted = 0;
//               add to the database
          if (cvArray.length > 0){
              inserted = context.getContentResolver().bulkInsert(RecipeProvider.Recipes.CONTENT_RECIPES_URI, cvArray);
          }
          Log.d(LOG_TAG, "Insert Db Complete" + inserted + " Inserted");

          callback.onRecipesLoaded(recipe);

      }

      @Override
      public void onLoadingFailed() {
       callback.onLoadingFailed();
      }
     });
    }




    @Override
    public void clearCache() {
     mCachedRecipe = null;
    }
}
