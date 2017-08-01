package com.ng.tselebro.bakingapp.util;

import com.ng.tselebro.bakingapp.Model.Recipe;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class BakingRecipeJsonApiHelper {
    private static final String CATEGORY = "android-baking-app-json";



    public static UdacityBakingApi getRxApi() {
        String BASE_URL = "http://go.udacity.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return  retrofit.create(UdacityBakingApi.class);


    }


    public interface UdacityBakingApi {
        @GET(CATEGORY)
        Call<List<Recipe>> getRecipeResult ();

    }

}
