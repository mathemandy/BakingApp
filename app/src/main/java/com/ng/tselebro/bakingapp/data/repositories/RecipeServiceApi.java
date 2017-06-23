package com.ng.tselebro.bakingapp.data.repositories;

import com.ng.tselebro.bakingapp.Model.POJO.Recipe;

import java.util.List;

/**
 * Created by mathemandy on 12 Jun 2017.
 */

public interface RecipeServiceApi {
    void getALlRecipes (RecipeServiceCallback<List<Recipe>> callback);
    interface RecipeServiceCallback<T> {
        void onLoaded (T recipe);

        void onLoadingFailed();
    }
}
