package com.ng.tselebro.bakingapp.data.repositories;

import android.util.Log;

import com.ng.tselebro.bakingapp.Model.Recipe;
import com.ng.tselebro.bakingapp.util.BakingRecipeJsonApiHelper;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecipeServiceApiImpl implements RecipeServiceApi{

    static final String TAG = RecipeServiceApiImpl.class.getSimpleName();

    private final BakingRecipeJsonApiHelper.UdacityBakingApi mApi;


    public RecipeServiceApiImpl ()
    {
        mApi = BakingRecipeJsonApiHelper.getRxApi();
    }

    @Override
    public void getALlRecipes(final RecipeServiceCallback<List<Recipe>> callback) {
        Call<List<Recipe>> recipes = mApi.getRecipeResult();


        recipes.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful())
                {
                    List<Recipe> recipeResult = response.body();
                    callback.onLoaded(recipeResult);
                }else
                {
                    callback.onLoadingFailed();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    callback.onLoadingFailed();
            }
        });

    }

}