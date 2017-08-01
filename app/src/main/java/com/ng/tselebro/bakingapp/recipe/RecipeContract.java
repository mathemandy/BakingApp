package com.ng.tselebro.bakingapp.recipe;

import android.content.Context;
import android.support.test.espresso.IdlingResource;

import com.ng.tselebro.bakingapp.Model.Recipe;

import java.util.List;


public class RecipeContract {

    interface View{
        void setProgressIndicator(boolean active);

        void showRecipes(List<Recipe> recipes);

        void showErrorView();

        void updateWidget();

        void showEmptyView();

        void showRecipeIngredients(long  recipeId);

    }

    interface  UserActionsListener{
        void loadRecipes (Context context);
        void setIdleResource(IdlingResource resource);
        void openRecipeIngredients(Recipe requestedRecipe);
    }
}
