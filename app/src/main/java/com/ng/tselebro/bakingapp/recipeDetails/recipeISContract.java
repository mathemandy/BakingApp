package com.ng.tselebro.bakingapp.recipeDetails;

import android.support.annotation.NonNull;

import com.ng.tselebro.bakingapp.Model.POJO.Recipe;

/**
 * Created by mathemandy on 20 Jun 2017.
 */

public class recipeISContract {

    interface View {
        void showEmptyView();
        void showMasterList(Recipe recipe);
        void showMasterDetails(Recipe recipe);
    }

    interface UserActionListener{
        void  openDetails(@NonNull Recipe requestedRecipe);

    }


}
