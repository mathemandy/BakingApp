package com.ng.tselebro.bakingapp.recipeDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ng.tselebro.bakingapp.Model.POJO.Recipe;
import com.ng.tselebro.bakingapp.R;

import butterknife.BindView;


public class RecipeDetails extends AppCompatActivity {
    private boolean mTwoPane;


    @BindView(R.id.videoplayer_fragment)
    LinearLayout master_detail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_is);

        if (savedInstanceState == null) {
            //            create a detail fragment and add it to the activity
            //              using a fragment transaction

            Intent i = getIntent();
            if (i != null){

               Recipe mRecipe = i.getExtras().getParcelable("recipes");


                    if (master_detail != null){
                        mTwoPane = true;

//            business Logic here
                        FragmentManager fragmentManager = getSupportFragmentManager();

//            Create the masterList fragment
                        MasterListFragment recipeList = new MasterListFragment();
                        fragmentManager.beginTransaction()
                                .add(R.id.master_list_container, recipeList)
                                .commit();

                        //Create the details View
                        DetailsFragment recipeDetails = new DetailsFragment();
                        fragmentManager.beginTransaction()
                                .add(R.id.videoplayer_fragment, recipeDetails);
                    }

                    else {
                        mTwoPane = false;
//                      create the fragment here
                        FragmentManager fragmentManager = getSupportFragmentManager();

                        fragmentManager.beginTransaction()
                                .add(R.id.master_list_container, MasterListFragment.newInstance(mRecipe))
                                .commit();

                    }
                }
            }


//        Determine if its a two pane or single pane layout

        }
}

