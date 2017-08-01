package com.ng.tselebro.bakingapp.recipe;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ng.tselebro.bakingapp.IdlingResource.SimpleIdlingResource;
import com.ng.tselebro.bakingapp.Injection;
import com.ng.tselebro.bakingapp.Model.Recipe;
import com.ng.tselebro.bakingapp.R;
import com.ng.tselebro.bakingapp.data.repositories.RecipeLoader;
import com.ng.tselebro.bakingapp.recipeDetails.RecipeDetails;
import com.ng.tselebro.bakingapp.widget.BakingAppWidget;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeContract.View {

    public static final String RECIPE_ID = "recipes";
    private SharedPreferences prefs;
    private RecipeAdapter mListAdapter;
    private RecipeContract.UserActionsListener mActionListener;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    private final RecipeItemListener mItemListener = new RecipeItemListener() {
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


    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSwipeRefreshlayout.setColorSchemeColors(
                ContextCompat.getColor(MainActivity.this, R.color.colorPrimary),
                ContextCompat.getColor(MainActivity.this, R.color.colorAccent),
                ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));

        mSwipeRefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mActionListener.loadRecipes(getApplicationContext());
                mSwipeRefreshlayout.setRefreshing(false);
            }
        });


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int widthPixels = displayMetrics.widthPixels;
        float scaleFactor = displayMetrics.density;
        float widthDp = widthPixels / scaleFactor;

        assert mRecyclerView != null;

        //Determine if the screen is tablet
        if (widthDp > 600) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, Math.round(widthDp / 300), GridLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            setupRecyclerView(mRecyclerView);

        } else {
            //Portrait View implementation of the MainActivity
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemViewCacheSize(4);
            setupRecyclerView(mRecyclerView);

        }

        mActionListener = new RecipePresenter(
                Injection.provideRecipeRepository(),
                this,
                prefs,
                getSupportLoaderManager(),
                new RecipeLoader(this));
        mActionListener.setIdleResource(getIdlingResource());
    }

    private void setupRecyclerView(RecyclerView mRecyclerView) {
        mListAdapter = new RecipeAdapter(new ArrayList<Recipe>(0), mItemListener);
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
    public void showRecipeIngredients(long recipeId) {
        Class destinationClass = RecipeDetails.class;
        Intent intentToStartActivity = new Intent(this, destinationClass);
        intentToStartActivity.putExtra(RECIPE_ID, recipeId);
        startActivity(intentToStartActivity);
    }

    interface RecipeItemListener {
        void onRecipeClick(Recipe clickedRecipe);
    }

    @Override
    public void updateWidget() {
        Intent intent = new Intent(this, BakingAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), BakingAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
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
