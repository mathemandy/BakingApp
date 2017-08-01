package com.ng.tselebro.bakingapp.videoplayer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.ng.tselebro.bakingapp.Model.Recipe;
import com.ng.tselebro.bakingapp.data.repositories.RecipeItemLoader;
import com.ng.tselebro.bakingapp.util.MediaUtils;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


public class videoPresenter implements videoContract.VideoUserActionListener {

    private static final int RECIPE_LOADER_ID = 2;
    private final videoContract.VideoView mVideoView;
    private final LoaderManager mLoaderManager;
    private final RecipeItemLoader mRecipeItemLoader;
    private final MediaUtils mMediaUtils;


    @Override
    public void initialiseMediaPlater() {
        //initialise the media Session
        mMediaUtils.initializeMediaSession();
    }


    @Override
    public void setRecipeName(String RecipeName) {
        mMediaUtils.setRecipeName(RecipeName);
    }

    @Override
    public void loadSteps() {
        mVideoView.showProgressIndicator();
        loadRecipesFromContentProvider();
    }

    @Override
    public void playWhenReady(boolean ready) {
        mMediaUtils.getmExoPlayer().setPlayWhenReady(ready);

    }

    @Override
    public MediaUtils getMediaUtils() {
        return mMediaUtils;
    }

    public videoPresenter(@NonNull videoContract.VideoView mVideoView,
                          @NonNull LoaderManager mLoaderManager,
                          @NonNull MediaUtils mediaUtils,
                          @NonNull RecipeItemLoader mRecipeLoader
    ) {
        this.mVideoView = checkNotNull(mVideoView, "Steps List cannot be null");
        this.mLoaderManager = mLoaderManager;
        this.mMediaUtils = mediaUtils;
        this.mRecipeItemLoader = mRecipeLoader;

    }


    private void loadRecipesFromContentProvider() {
        mLoaderManager.initLoader(RECIPE_LOADER_ID, null, new LoaderManager.LoaderCallbacks<List<Recipe>>() {
            @Override
            public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
                return mRecipeItemLoader;
            }

            @Override
            public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
                mVideoView.showProgressIndicator();
                if (data == null || data.isEmpty()) {
                    mVideoView.showEmptyView();
                } else {
                    mVideoView.showSectionPageAdapter(data);
                }
            }

            @Override
            public void onLoaderReset(Loader<List<Recipe>> loader) {

            }
        });
    }


}
