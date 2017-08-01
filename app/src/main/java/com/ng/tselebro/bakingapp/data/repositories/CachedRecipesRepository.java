package com.ng.tselebro.bakingapp.data.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.ng.tselebro.bakingapp.Model.Recipe;
import com.ng.tselebro.bakingapp.data.local.RecipeColumns;
import com.ng.tselebro.bakingapp.data.local.RecipeProvider;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


public class CachedRecipesRepository implements RepositoryContract.RecipeRepository {


    private final RecipeServiceApi mRecipeServiceApi;
    private static final String LOG_TAG = CachedRecipesRepository.class.getSimpleName();

    @VisibleForTesting
    private List<Recipe> mCachedRecipe;


    public CachedRecipesRepository(RecipeServiceApi mRecipeServiceApi) {
        this.mRecipeServiceApi = checkNotNull(mRecipeServiceApi);

    }

    @Override
    public void loadRecipes(@NonNull LoadRecipesCallback callback, Context context) {
        checkNotNull(callback);
        //Load from Api only if needed
        if (mCachedRecipe == null) {
            getRecipe(callback, context);
        } else {
            callback.onRecipesLoaded(mCachedRecipe);
        }

    }


    private void getRecipe(@NonNull final LoadRecipesCallback callback, final Context context) {
        mRecipeServiceApi.getALlRecipes(new RecipeServiceApi.RecipeServiceCallback<List<Recipe>>() {
            @Override
            public void onLoaded(List<Recipe> recipe) {


                mCachedRecipe = ImmutableList.copyOf(recipe);

                Gson gson = new Gson();
                ContentValues[] cvArray = new ContentValues[mCachedRecipe.size()];

                for (Recipe freshRecipe : mCachedRecipe) {
                    ContentValues recipevalue = new ContentValues();
                    recipevalue.put(RecipeColumns.ID, Integer.toString(freshRecipe.getId()));
                    recipevalue.put(RecipeColumns.image_url, (freshRecipe.getImage()));
                    recipevalue.put(RecipeColumns.recipeName, freshRecipe.getName());
                    recipevalue.put(RecipeColumns.servings, Integer.toString(freshRecipe.getServings()));
                    recipevalue.put(RecipeColumns.ingredient, gson.toJson(freshRecipe.getIngredients()));
                    recipevalue.put(RecipeColumns.steps, gson.toJson(freshRecipe.getSteps()));
                    cvArray[mCachedRecipe.indexOf(freshRecipe)] = recipevalue;
                }
                int inserted = 0;
//               add to the database
                if (cvArray.length > 0) {
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
