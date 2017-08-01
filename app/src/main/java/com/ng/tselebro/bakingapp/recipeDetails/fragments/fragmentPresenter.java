package com.ng.tselebro.bakingapp.recipeDetails.fragments;


import android.support.annotation.NonNull;

import com.ng.tselebro.bakingapp.Model.Step;
import  com.ng.tselebro.bakingapp.recipeDetails.recipeISContract;

public class fragmentPresenter implements recipeISContract.UserActionListener {

    private recipeISContract.View fragmentView;

    public fragmentPresenter(@NonNull recipeISContract.View fragmentView) {
        this.fragmentView = fragmentView;
    }

    @Override
    public void openDetails(@NonNull Step step, int position) {
        fragmentView.showMasterDetails(step , position);
    }


}
