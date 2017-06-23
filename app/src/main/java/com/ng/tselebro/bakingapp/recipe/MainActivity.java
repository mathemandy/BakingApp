package com.ng.tselebro.bakingapp.recipe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ng.tselebro.bakingapp.Injection;
import com.ng.tselebro.bakingapp.Model.POJO.Recipe;
import com.ng.tselebro.bakingapp.R;
import com.ng.tselebro.bakingapp.data.repositories.RecipeLoader;
import com.ng.tselebro.bakingapp.recipeDetails.MasterListFragment;
import com.ng.tselebro.bakingapp.recipeDetails.RecipeDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.app.ActivityOptionsCompat.*;

public class MainActivity extends AppCompatActivity implements RecipeContract.View{


    private RecipeAdapter mListAdapter;
    private  RecipeContract.UserActionsListener mActionListener;
    private  final RecipeItemListener mItemListener = new RecipeItemListener() {
        @Override
        public void onRecipeClick(Recipe clickedRecipe) {
            mActionListener.openRecipeIngredients(clickedRecipe);
        }
    };

    @BindView(R.id.recipeList)
    RecyclerView mRecyclerView;

    @BindView(R.id.error_view)
    View mErrorView;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshlayout;

    @BindView(R.id.progress)
    ProgressBar mProgress;

    @BindView(R.id.empty_view)
    TextView mEmptyView;

    @BindBool(R.bool.firstTime)
    boolean firstTime;
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSwipeRefreshlayout.setColorSchemeColors(
                ContextCompat.getColor(MainActivity.this, R.color.colorPrimary),
                ContextCompat.getColor(MainActivity.this, R.color.colorAccent),
                ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
        mSwipeRefreshlayout.setOnRefreshListener(() -> {
            mActionListener.loadRecipes(this);
            mSwipeRefreshlayout.setRefreshing(false);
        });

        assert mRecyclerView != null;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(5);
        setupRecyclerView(mRecyclerView);


        mActionListener = new RecipePresenter(
                Injection.provideRecipeRepository(),
                this,
                prefs,
                getSupportLoaderManager(),
                new RecipeLoader(this));

    }

    private void setupRecyclerView(RecyclerView mRecyclerView) {
        mListAdapter = new RecipeAdapter(new ArrayList<>(0), mItemListener);
        mRecyclerView.setAdapter(mListAdapter);
    }


    @Override
    public void setProgressIndicator(boolean active) {
        mProgress.setVisibility(active ? View.VISIBLE : View.GONE);


    }

    @Override
    public void showRecipes(List<Recipe> recipes) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mListAdapter.replaceData(recipes);

    }

    @Override
    public void showErrorView() {
        mRecyclerView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyView() {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRecipeIngredients(Recipe recipe) {
        Class destinationClass = RecipeDetails.class;

        Intent intentToStartActivity = new Intent(getBaseContext(), destinationClass);
        intentToStartActivity.putExtra("recipes", recipe);
        startActivity(intentToStartActivity);
    }

    interface RecipeItemListener {
        void onRecipeClick(Recipe clickedRecipe);
    }

    @Override
    protected void onResume() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.available_reccipe);
        } else {
            setTitle(R.string.available_reccipe);
        }
        mActionListener.loadRecipes(this);
        super.onResume();
    }
}
