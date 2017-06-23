package com.ng.tselebro.bakingapp.recipeDetails;

import android.support.annotation.NonNull;

import com.ng.tselebro.bakingapp.Model.POJO.Recipe;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mathemandy on 20 Jun 2017.
 */

public class recipeISPresenter  implements recipeISContract.UserActionListener{


    private  final recipeISContract.View mRecipeMasterlistView;
    private Recipe recipe;

    public  recipeISPresenter (@NonNull recipeISContract.View recipeDetailView){
        mRecipeMasterlistView = checkNotNull(recipeDetailView, "recipe MasterList cannot be null");
    }


    @Override
    public void openDetails(@NonNull Recipe requestedRecipe) {
        recipe = requestedRecipe;



    }
}
