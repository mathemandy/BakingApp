package com.ng.tselebro.bakingapp.recipeDetails;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ng.tselebro.bakingapp.Model.Recipe;
import com.ng.tselebro.bakingapp.R;
import com.ng.tselebro.bakingapp.data.repositories.RecipeItemLoader;
import com.ng.tselebro.bakingapp.recipe.MainActivity;
import com.ng.tselebro.bakingapp.recipeDetails.fragments.MasterListIngredientFragment;
import com.ng.tselebro.bakingapp.recipeDetails.fragments.MasterListStepFragment;
import com.ng.tselebro.bakingapp.videoplayer.VideoFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ng.tselebro.bakingapp.recipe.MainActivity.RECIPE_ID;


public class RecipeDetails extends AppCompatActivity implements recipeISContract.DetailsView, SharedPreferences.OnSharedPreferenceChangeListener {


    private long recipeId;
    private Recipe mRecipe;
    private recipeISContract.DetailsUserActionListener mActionListener;
    private boolean mTwoPane = false;
    private MenuItem mFav;
    private SharedPreferences prefs;

    @BindView(R.id.details_progress)
    ProgressBar loading_indicator;
    @BindView(R.id.masterStepContainer)
    FrameLayout stepFragment;
    @BindView(R.id.master_list_container)
    FrameLayout ingredientFragment;
    @BindView(R.id.erro_no_data)
    TextView errorView;
    MasterListStepFragment mMasterListStepFragment;
    Fragment mFragment;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_is);
        ButterKnife.bind(this);


        if (findViewById(R.id.videoplayer_fragment) != null) {
            mTwoPane = true;
        }
        Intent intent = getIntent();
        if (intent.hasExtra(RECIPE_ID)) {
            recipeId = getIntent().getLongExtra(RECIPE_ID, 1);
            mActionListener = new recipeISPresenter(this,
                    getSupportLoaderManager(),
                    new RecipeItemLoader(this, recipeId), prefs, getApplicationContext());
        }
        if (mActionListener != null) {
            mActionListener.loadRecipes();
        }

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    private void beginFragmentTransaction(Recipe recipe, boolean mTwoPane) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle argument = new Bundle();
        argument.putParcelable(RECIPE_ID, recipe);
        argument.putBoolean("mTwoPane", mTwoPane);



        if (mTwoPane) {

            VideoFragment recipeDetails = new VideoFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.videoplayer_fragment, recipeDetails)
                    .commit();
        }

        if (MasterListIngredientFragment.newInstance(mRecipe) == null) {
            //create the fragment for ingredients
            fragmentManager.beginTransaction()
                    .add(R.id.master_list_container, MasterListIngredientFragment.newInstance(recipe))
                    .commit();
        }else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.master_list_container, MasterListIngredientFragment.newInstance(recipe))
                    .commit();
        }
        if (fragmentManager.findFragmentById(R.id.master_list_container ) == null) {
            //create fragment for step
            mMasterListStepFragment = new MasterListStepFragment();
            mMasterListStepFragment.setArguments(argument);

            //add to container
            fragmentManager.beginTransaction()
                    .add(R.id.masterStepContainer, mMasterListStepFragment)
                    .commit();
        }
else {
            mMasterListStepFragment = (MasterListStepFragment) mFragment;

            if (mMasterListStepFragment.isAdded()){
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.masterStepContainer,mMasterListStepFragment)
                        .commit();

            }

    }}


    @Override
    public void showProgressIndicator() {
        if (!mTwoPane) {
            loading_indicator.setVisibility(View.VISIBLE);
        }

        stepFragment.setVisibility(GONE);
        ingredientFragment.setVisibility(GONE);
    }

    @Override
    public void showFragments(List<Recipe> recipe) {
        errorView.setVisibility(View.GONE);
        loading_indicator.setVisibility(GONE);
        stepFragment.setVisibility(VISIBLE);
        ingredientFragment.setVisibility(VISIBLE);

        mRecipe = recipe.get(0);

        beginFragmentTransaction(mRecipe, mTwoPane);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mRecipe.getName());
        } else {
            setTitle(mRecipe.getName());
        }
    }

    @Override
    public void showEmptyView() {
        errorView.setVisibility(VISIBLE);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("TwoPane", mTwoPane);
        outState.putLong("recipe", recipeId);
        getSupportFragmentManager().putFragment(outState, "step_fragment",
                getSupportFragmentManager().findFragmentById(R.id.masterStepContainer));

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("recipe")) {
                recipeId = savedInstanceState.getLong("recipe");
            }
            if (savedInstanceState.containsKey("TwoPane")) {
                mTwoPane = savedInstanceState.getBoolean("TwoPane");
            }
         mFragment = getSupportFragmentManager().getFragment(savedInstanceState, "step_fragment");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorite, menu);
        mFav = menu.findItem(R.id.favorite);
        mActionListener.setIcon(recipeId);
   return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            try {
                NavUtils.navigateUpFromSameTask(this);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Intent startMainActivity = new Intent(this, MainActivity.class);
                startActivity(startMainActivity);
            }

        }else if (id == R.id.favorite){
            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().putInt(getString(R.string.pref_fav_recipe_id_key), (int)recipeId).apply();

        }
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        mActionListener.setIcon(recipeId);
    }

    @Override
    public void showFavoriteState(boolean state) {

            if (state){
                mFav.setIcon(ContextCompat.getDrawable(this, R.drawable.favorite));
            }else {
                mFav.setIcon(ContextCompat.getDrawable(this, R.drawable.favorite_border));
            }
    }
}

