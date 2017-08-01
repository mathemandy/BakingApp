package com.ng.tselebro.bakingapp.recipeDetails.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ng.tselebro.bakingapp.Model.Recipe;
import com.ng.tselebro.bakingapp.Model.Step;
import com.ng.tselebro.bakingapp.R;
import com.ng.tselebro.bakingapp.recipeDetails.ExpandableDetailsSteps;
import com.ng.tselebro.bakingapp.recipeDetails.adapters.recipeStepAdapter;
import com.ng.tselebro.bakingapp.recipeDetails.recipeISContract;
import com.ng.tselebro.bakingapp.util.MediaUtils;
import com.ng.tselebro.bakingapp.videoplayer.VideoFragment;
import com.ng.tselebro.bakingapp.videoplayer.VideoPlayerActivity;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ng.tselebro.bakingapp.recipe.MainActivity.RECIPE_ID;
import static com.ng.tselebro.bakingapp.videoplayer.VideoPlayerActivity.INTENT_ITEM_POSITION;


public class MasterListStepFragment extends Fragment implements recipeISContract.View {

    private recipeISContract.UserActionListener mActionListener;

    private final MasterListStepFragment.RecipeStepItemListener mItemListener =
            new MasterListStepFragment.RecipeStepItemListener() {
                @Override
                public void onStepClick(Step step, int position) {
                    mActionListener.openDetails(step, position);
                    mTwoPaneSelectedItem = position;
                }
            };

    private static final String ARG_RECIPE_ITEM = "recipes";
    public static final String STEP_ID = "steps";
    private Recipe mRecipe;
    private long mRecipeId;
    private boolean mTwoPane;
    private int mTwoPaneSelectedItem;
    private MediaUtils mediaUtils;
    private recipeStepAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    Parcelable mListState;


    @BindView(R.id.recyclerViewStep)
     RecyclerView masterListStep;
    @BindView(R.id.error_no_steps)
    TextView empty_view;


    public MasterListStepFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);

        mRecipe = getArguments().getParcelable(ARG_RECIPE_ITEM);
        mTwoPane = getArguments().getBoolean("mTwoPane");
        mRecipeId = mRecipe.getId();
        mActionListener = new fragmentPresenter(this);

        if (mTwoPane) {
            mediaUtils = new MediaUtils(getContext(), new MySessionCallback());
            mediaUtils.initializeMediaSession();

        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_master_step, container, false);
        ButterKnife.bind(this, view);
        return view;
    }



    private List<ExpandableDetailsSteps> makeSteps(Recipe recipe) {
        return Collections.singletonList(new ExpandableDetailsSteps("Steps", recipe.getSteps()));
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        adapter.onSaveInstanceState(outState);
        if (mListState == null) {
            mListState = mLayoutManager.onSaveInstanceState();
            outState.putParcelable("REC_STATE", mListState);
        }

        super.onSaveInstanceState(outState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null){
          adapter = new recipeStepAdapter(makeSteps(mRecipe), mItemListener);
            mLayoutManager = new LinearLayoutManager(getContext());
            masterListStep.setLayoutManager(mLayoutManager);
            masterListStep.setAdapter(adapter);
        }else {
            mListState = savedInstanceState.getParcelable("REC_STATE");
            mLayoutManager.onRestoreInstanceState(mListState);
            masterListStep.setAdapter(adapter);
        }

    }

    @Override
    public void showMasterDetails(Step step, int position) {

        if (mTwoPane) {
            mediaUtils.setRecipeName(mRecipe.getName());
            Bundle arguments = new Bundle();
            arguments.putParcelable(STEP_ID, step);
            VideoFragment fragment = new VideoFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.video_Player, fragment)
                    .commit();

        } else {

            Class destinationClass = VideoPlayerActivity.class;
            Intent intenttoStartVideoActivity = new Intent(getContext(), destinationClass);
            intenttoStartVideoActivity.putExtra(STEP_ID, step);
            intenttoStartVideoActivity.putExtra(RECIPE_ID, mRecipeId);
            intenttoStartVideoActivity.putExtra(INTENT_ITEM_POSITION, position);
            startActivity(intenttoStartVideoActivity);
        }
    }

    public interface RecipeStepItemListener {
        void onStepClick(Step step, int position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaUtils != null) {
            mediaUtils.release();
        }
    }

    public MediaUtils getMediaUtils(){
        return mediaUtils;
    }
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mediaUtils.getmExoPlayer().setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mediaUtils.getmExoPlayer().setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            if (mTwoPaneSelectedItem > 0) {

                final int position = --mTwoPaneSelectedItem;
                Step step = mRecipe.getSteps().get(position);
                mActionListener.openDetails(step, position);

            }
        }

        @Override
        public void onSkipToNext() {
            if (mTwoPaneSelectedItem < adapter.getItemCount()) {
                final int position = ++mTwoPaneSelectedItem;
                Step step = mRecipe.getSteps().get(position);
                mActionListener.openDetails(step, position);

            }
        }

    }
}
