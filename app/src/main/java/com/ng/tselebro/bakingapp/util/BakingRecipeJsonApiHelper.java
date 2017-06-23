package com.ng.tselebro.bakingapp.util;

import com.ng.tselebro.bakingapp.Model.POJO.Recipe;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by mathemandy on 12 Jun 2017.
 */

public class BakingRecipeJsonApiHelper {
    private static  String BASE_URL = "http://go.udacity.com/";
    public static final String CATEGORY = "android-baking-app-json";



    public static UdacityBakingRxApi getRxApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return  retrofit.create(UdacityBakingRxApi.class);


    }


    public interface UdacityBakingRxApi {
        @GET(CATEGORY)
        Observable<List<Recipe>> getRecipeResult ();

    }

}
