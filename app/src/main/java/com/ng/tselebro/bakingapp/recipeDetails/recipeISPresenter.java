package com.ng.tselebro.bakingapp.recipeDetails;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.ng.tselebro.bakingapp.Model.Recipe;
import com.ng.tselebro.bakingapp.R;
import com.ng.tselebro.bakingapp.data.repositories.RecipeItemLoader;
import com.ng.tselebro.bakingapp.widget.BakingIngredientwidget;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;



public class recipeISPresenter  implements recipeISContract.DetailsUserActionListener{



    private static final int RECIPE_LoADER_ID =1;
    private final recipeISContract.DetailsView mRecipeView;
    private final LoaderManager mLoaderManager;
    private final RecipeItemLoader mRecipeItemLoader;
    private  SharedPreferences mSharedPreferences;
    private  final Context mApplicationContext;
    private final int WHAT = 1;
    boolean mNewState;





    public  recipeISPresenter (@NonNull recipeISContract.DetailsView recipeDetailView,
                               @NonNull LoaderManager mLoaderManager,
                               @NonNull RecipeItemLoader mRecipeLoader,
                               SharedPreferences preferences,
                               Context mContext
                               ){
        this.mRecipeView = checkNotNull(recipeDetailView, "recipe MasterList cannot be null");
        this.mLoaderManager = mLoaderManager;
        this.mRecipeItemLoader= mRecipeLoader;
        this.mSharedPreferences = preferences;
        this.mApplicationContext = mContext;


    }


    @Override
    public void loadRecipes() {
        mRecipeView.showProgressIndicator();
        loadRecipesFromContentProvider();

    }

    private void loadRecipesFromContentProvider() {
        mLoaderManager.initLoader(RECIPE_LoADER_ID, null, new LoaderManager.LoaderCallbacks<List<Recipe>>(){
            @Override
            public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
               return mRecipeItemLoader;
            }

            @Override
            public void onLoadFinished(Loader<List<Recipe>> loader, final List<Recipe> data) {
                mRecipeView.showProgressIndicator();
                if (data == null || data.isEmpty()){
                    mRecipeView.showEmptyView();
                } else {

                    Handler handler = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.what == WHAT) mRecipeView.showFragments(data);
                        }
                    };
                    handler.sendEmptyMessage(WHAT);


                }
            }

            @Override
            public void onLoaderReset(Loader<List<Recipe>> loader) {

            }
        });
    }

    @Override
    public void setIcon(long recipeId) {
       mNewState = isFav(recipeId);
        mRecipeView.showFavoriteState(mNewState);
    }

    private boolean isFav(long recipeID){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mApplicationContext);
        int favoriteRecipeId = mSharedPreferences.getInt(mApplicationContext.getString(R.string.pref_fav_recipe_id_key),Integer.parseInt(mApplicationContext.getString( R.string.pref_fav_recipe_id_default)));
        if (favoriteRecipeId == recipeID){
            updateWidget(mApplicationContext);
            return  true;
        }else {
            return  false;
        }

    }

    private void updateWidget(Context mApplicationContext) {
        Intent intent =  new Intent(mApplicationContext, BakingIngredientwidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance((mApplicationContext)).getAppWidgetIds(new ComponentName(mApplicationContext, BakingIngredientwidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        mApplicationContext.sendBroadcast(intent);
    }

}
