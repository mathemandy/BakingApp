package com.ng.tselebro.bakingapp.data.repositories;

import com.ng.tselebro.bakingapp.Model.Recipe;

import java.util.List;



interface RecipeServiceApi {
    void getALlRecipes (RecipeServiceCallback<List<Recipe>> callback);
    interface RecipeServiceCallback<T> {
        void onLoaded (T recipe);

        void onLoadingFailed();
    }
}
