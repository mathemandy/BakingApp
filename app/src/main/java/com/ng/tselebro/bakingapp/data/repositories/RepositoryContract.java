package com.ng.tselebro.bakingapp.data.repositories;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ng.tselebro.bakingapp.Model.Recipe;

import java.util.List;



public interface RepositoryContract {
    interface RecipeRepository {
        void loadRecipes(@NonNull LoadRecipesCallback callback, Context context);

        void clearCache();

        interface LoadRecipesCallback {
            void onRecipesLoaded(List<Recipe> recipes);

            void onLoadingFailed();
        }

    }
}