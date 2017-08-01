package com.ng.tselebro.bakingapp.videoplayer;


import com.ng.tselebro.bakingapp.Model.Recipe;
import com.ng.tselebro.bakingapp.util.MediaUtils;

import java.util.List;

class videoContract {

    public interface VideoUserActionListener{
        void loadSteps();
        void initialiseMediaPlater();
        void playWhenReady(boolean ready);
        MediaUtils getMediaUtils();
        void setRecipeName(String RecipeName);
    }


    public interface VideoView{
        void showProgressIndicator();
        void showSectionPageAdapter(List<Recipe> recipe);
        void showEmptyView();
    }
}
