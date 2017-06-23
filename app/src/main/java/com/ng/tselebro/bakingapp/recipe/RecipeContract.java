package com.ng.tselebro.bakingapp.recipe;

import android.content.Context;

import com.ng.tselebro.bakingapp.Model.POJO.Recipe;

import java.util.List;


public class RecipeContract {

    interface View{
        void setProgressIndicator(boolean active);

        void showRecipes(List<Recipe> recipes);

        void showErrorView();

        void showEmptyView();

        void showRecipeIngredients(Recipe recipe);

    }

    interface  UserActionsListener{
        void loadRecipes (Context context);

        void openRecipeIngredients(Recipe requestedRecipe);
    }
}
