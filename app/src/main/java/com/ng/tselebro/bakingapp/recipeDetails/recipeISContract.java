package com.ng.tselebro.bakingapp.recipeDetails;

import android.support.annotation.NonNull;

import com.ng.tselebro.bakingapp.Model.Recipe;
import com.ng.tselebro.bakingapp.Model.Step;

import java.util.List;


public class recipeISContract {

    public interface View {
        void showMasterDetails(Step step, int position);
    }


    public interface DetailsView {
        void showProgressIndicator();
        void showFragments(List<Recipe> recipe);
        void showEmptyView();
        void showFavoriteState(boolean state);
    }


    public interface DetailsUserActionListener {
        void loadRecipes();
        void setIcon(long recipeId);



    }

   public interface UserActionListener{
        void  openDetails(@NonNull Step step, int position);


    }


}
