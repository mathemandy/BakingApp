package com.ng.tselebro.bakingapp.data.repositories;

import android.util.Log;

import com.ng.tselebro.bakingapp.Model.POJO.Recipe;
import com.ng.tselebro.bakingapp.util.BakingRecipeJsonApiHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mathemandy on 12 Jun 2017.
 */

public class RecipeServiceApiImpl implements RecipeServiceApi{

    public static final String TAG = RecipeServiceApiImpl.class.getSimpleName();

    private final BakingRecipeJsonApiHelper.UdacityBakingRxApi mApi;


    public RecipeServiceApiImpl ()
    {
        mApi = BakingRecipeJsonApiHelper.getRxApi();
    }

    @Override
    public void getALlRecipes(RecipeServiceCallback<List<Recipe>> callback) {
        Observable<List<Recipe>> recipes = mApi.getRecipeResult();

        recipes.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(throwable -> {
                        callback.onLoadingFailed();
                    })
        .subscribe( recipe -> {
            if (recipe != null )
                callback.onLoaded(recipe);

                Log.v(TAG, "The Recipe has been received" + recipe);


        } );

    }

}