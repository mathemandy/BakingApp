package com.ng.tselebro.bakingapp.recipe;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.ng.tselebro.bakingapp.Model.POJO.Recipe;
import com.ng.tselebro.bakingapp.R;
import com.ng.tselebro.bakingapp.data.repositories.RecipeLoader;
import com.ng.tselebro.bakingapp.data.repositories.RepositoryContract;

import java.util.List;
import java.util.prefs.Preferences;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mathemandy on 13 Jun 2017.
 */

public class RecipePresenter implements RecipeContract.UserActionsListener{

    private static  final int RECIPE_LOADER_ID = 1;
    private final RepositoryContract.RecipeRepository mRecipeRepository;
    private final RecipeContract.View mRecipeView;
    private final LoaderManager mLoaderManager;
    private final RecipeLoader mRecipeLoader;
    SharedPreferences preferences;


    public RecipePresenter(@NonNull RepositoryContract.RecipeRepository mRecipeRepository,
                           @NonNull RecipeContract.View mRecipeView,
                           @NonNull SharedPreferences preferences,
                           @NonNull LoaderManager mLoaderManager,
                           @NonNull RecipeLoader mRecipeLoader) {
        this.mRecipeRepository = mRecipeRepository;
        this.mRecipeView = checkNotNull(mRecipeView, "recipesView cannot be null");
        this.mLoaderManager = mLoaderManager;
        this.mRecipeLoader = mRecipeLoader;
        this.preferences = preferences;
    }

    @Override
    public void loadRecipes(Context context) {
        mRecipeView.setProgressIndicator(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean firstTime = preferences.getBoolean("FIRSTRUN", true);

        if (!firstTime){
            loadRecipesFromContentProvider();
        } else
        {
            mRecipeRepository.clearCache();
            mRecipeRepository.loadRecipes(new RepositoryContract.RecipeRepository.LoadRecipesCallback() {
                @Override
                public void onRecipesLoaded(List<Recipe> recipes) {
                    mRecipeView.setProgressIndicator(false);
                    mRecipeView.showRecipes(recipes);

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("FIRSTRUN", false);
                    editor.apply();
                }

                @Override
                public void onLoadingFailed() {
                    mRecipeView.setProgressIndicator(false);
                    mRecipeView.showErrorView();

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("FIRSTRUN", true);
                    editor.apply();
                }
            }, context);
        }
    }

    private void loadRecipesFromContentProvider(){
        mLoaderManager.initLoader(RECIPE_LOADER_ID, null, new LoaderManager.LoaderCallbacks<List<Recipe>>() {

            @Override
            public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
                return mRecipeLoader;
            }

            @Override
            public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
                mRecipeView.setProgressIndicator(false);
                if (data == null || data.isEmpty()){
                    mRecipeView.showEmptyView();
                }else {
                    mRecipeView.showRecipes(data);
                }

            }

            @Override
            public void onLoaderReset(Loader<List<Recipe>> loader) {

            }
        });
    }

    @Override
    public void openRecipeIngredients(Recipe requestedRecipe) {
        checkNotNull(requestedRecipe, "requestedRecipe cannot be null");
        mRecipeView.showRecipeIngredients(requestedRecipe);

    }
}
