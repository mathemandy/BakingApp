package com.ng.tselebro.bakingapp.videoplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.ShareCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ng.tselebro.bakingapp.Model.Recipe;
import com.ng.tselebro.bakingapp.Model.Step;
import com.ng.tselebro.bakingapp.R;
import com.ng.tselebro.bakingapp.data.repositories.RecipeItemLoader;
import com.ng.tselebro.bakingapp.recipeDetails.RecipeDetails;
import com.ng.tselebro.bakingapp.util.MediaUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static com.ng.tselebro.bakingapp.recipe.MainActivity.RECIPE_ID;


public class VideoPlayerActivity extends AppCompatActivity implements videoContract.VideoView {

    private int mPosition;
    private List<Step> mStep;
    private SectionPagerAdapter mSectionPagerAdapter;
    public static final String INTENT_ITEM_POSITION = "selected_item";
    private String mRecipeName;
    private long mId;
    private videoContract.VideoUserActionListener mActionListener;


    @BindView(R.id.recipe_detail_container)
    ViewPager mViewPager;
    @BindView(R.id.recipe_detail_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.detail_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.video_loading)
    ProgressBar videoLoading;
    @BindView(R.id.no_video_textView)
    TextView mNoVideoTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        if (mToolbar != null){ setSupportActionBar(mToolbar);}



        //create the detail fragment and add it to the activity
        //using a fragment transaction
        if (savedInstanceState == null) {
            mPosition = getIntent().getIntExtra(INTENT_ITEM_POSITION, 0);
            mRecipeName = getIntent().getStringExtra("recipe_name");
            mId = getIntent().getIntExtra(RECIPE_ID, 0);
        }

        Intent intent = getIntent();
        if (intent.hasExtra(RECIPE_ID)) {
            long recipeId = intent.getLongExtra(RECIPE_ID, 1);
            mActionListener = new videoPresenter(this,
                    getSupportLoaderManager(),
                    new MediaUtils(this, new MySessionCallback()),
                    new RecipeItemLoader(this, recipeId));
        }

        //Show the Up Button in the action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mActionListener.initialiseMediaPlater();
        //set up the ViewPager with the sections Adapter.
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);


    }

    public MediaUtils getMediaUtils() {
        return mActionListener.getMediaUtils();
    }

    @Override
    protected void onResume() {
        mActionListener.loadSteps();
        super.onResume();
        mActionListener.setRecipeName(mRecipeName);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mPosition", mPosition);
        outState.putString("mRecipeName", mRecipeName);
        outState.putLong("mId", mId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("mPosition")) {
                mPosition = savedInstanceState.getInt("mPosition");
            }
            if (savedInstanceState.containsKey("mRecipeName")) {
                mRecipeName = savedInstanceState.getString("mRecipeName");
            }
            if (savedInstanceState.containsKey("mId")) {
                mId = savedInstanceState.getLong("mId");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = new Intent(this, RecipeDetails.class);
                returnIntent.putExtra(RECIPE_ID, mId);
                NavUtils.navigateUpTo(this, returnIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareText(String shareText, String title) {
        ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(shareText)
                .setChooserTitle(String.format(getString(R.string.sharing_string), title))
                .startChooser();
    }

    @Override
    public void showProgressIndicator() {
        videoLoading.setVisibility(View.VISIBLE);
    }


    @Override
    public void showSectionPageAdapter(List<Recipe> recipe) {
        Recipe mRecipe = recipe.get(0);
        mStep = mRecipe.getSteps();
        videoLoading.setVisibility(GONE);
        mNoVideoTextView.setVisibility(GONE);

//        Show the layout for the video
        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionPagerAdapter);
        mViewPager.addOnPageChangeListener(mSectionPagerAdapter);
        mViewPager.setCurrentItem(mPosition);
        mSectionPagerAdapter.onPageSelected(mPosition);


    }


    @Override
    public void showEmptyView() {
        mNoVideoTextView.setVisibility(View.VISIBLE);
        mFab.setVisibility(View.INVISIBLE);
        mTabLayout.setVisibility(View.INVISIBLE);

    }


    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mActionListener.playWhenReady(true);
        }

        @Override
        public void onPause() {
            mActionListener.playWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            if (mPosition > 0) {
                mViewPager.setCurrentItem(--mPosition);
                mSectionPagerAdapter.onPageSelected(mPosition);
            }
        }

        @Override
        public void onSkipToNext() {
            if (mPosition < mSectionPagerAdapter.getCount()) {
                mViewPager.setCurrentItem(++mPosition);
                mSectionPagerAdapter.onPageSelected(mPosition);
            }
        }
    }

    private class SectionPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
        VideoFragment[] mFragment;
        private List<Step> mSteps = new ArrayList<>();


        SectionPagerAdapter(FragmentManager fm) {
            super(fm);
            mSteps = mStep;
            mFragment = new VideoFragment[mSteps.size()];
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(final int position) {
            Activity activity = VideoPlayerActivity.this;
            CollapsingToolbarLayout appbarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            ImageView imageView = (ImageView) activity.findViewById(R.id.recipe_video_image_view);
            if (appbarLayout != null) {
                imageView.setVisibility(View.VISIBLE);
                appbarLayout.setTitle(mSteps.get(position).getShortDescription());
                if (mSteps.get(position).getThumbnailURL() != null && !mSteps.get(position).getThumbnailURL().isEmpty()) {
                    Picasso.with(activity).load(mSteps.get(position).getThumbnailURL()).error(R.drawable.recipe_placeholder).placeholder(R.drawable.recipe_placeholder).into(imageView);
                }

                mFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = mSteps.get(position).getDescription() +
                                "\n" + mSteps.get(position).getVideoURL();
                        shareText(text, mSteps.get(position).getShortDescription());

                    }
                });


            }

            if (mFragment[position] != null) {
                mFragment[position].onSelected();
                if (position == 0) {
                    if (mFragment[position + 1] != null) {
                        mFragment[position + 1].onDeselected();
                    }
                } else if (position == mFragment.length - 1) {
                    if (mFragment[position - 1] != null) {
                        mFragment[position - 1].onDeselected();
                    }
                } else {
                    if (mFragment[position - 1] != null) {
                        mFragment[position - 1].onDeselected();
                    }

                    if (mFragment[position + 1] != null) {
                        mFragment[position + 1].onDeselected();
                    }
                }
            }

            mPosition = position;
        }


        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public Fragment getItem(int position) {
            return newInstance(position);
        }

        @Override
        public int getCount() {
            return mSteps.size();
        }

        VideoFragment newInstance(int stepNumber) {

            final Step step = mSteps.get(stepNumber);

            Bundle arguments = new Bundle();
            arguments.putParcelable("steps", step);
            VideoFragment fragment = new VideoFragment();
            fragment.setArguments(arguments);
            mFragment[stepNumber] = fragment;
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mSteps.get(position).getShortDescription();
        }
    }


}





