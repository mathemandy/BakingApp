package com.ng.tselebro.bakingapp.videoplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.ng.tselebro.bakingapp.Model.Step;
import com.ng.tselebro.bakingapp.R;
import com.ng.tselebro.bakingapp.recipeDetails.RecipeDetails;
import com.ng.tselebro.bakingapp.recipeDetails.fragments.MasterListStepFragment;
import com.ng.tselebro.bakingapp.util.MediaUtils;
import com.ng.tselebro.bakingapp.util.VideoCacheProxyFactory;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ng.tselebro.bakingapp.recipeDetails.fragments.MasterListStepFragment.STEP_ID;


public class VideoFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */

    private static final String LOG_TAG = VideoFragment.class.getSimpleName();
    private Step mStep;
    private String mTitle;
    private SimpleExoPlayer simpleExoPlayer;
    private boolean mValidPlayer = false;
    private MediaUtils mediaUtils;

    @BindView(R.id.recipe_step_detail)
     TextView mStepDetails;

    @BindView(R.id.recipe_step_container)
      ConstraintLayout mContainer;

    @BindView(R.id.videoplayer)
     SimpleExoPlayerView mSimpleExoPlayerView;


    public VideoFragment() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null){
            if (bundle.containsKey(STEP_ID)) {
                mStep = bundle.getParcelable(STEP_ID);
                if (mStep != null) {
                    mTitle = mStep.getShortDescription();
                }
            }
        }


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container  == null){
            getFragmentManager().beginTransaction().detach(this).commit();
                    }
        Log.e(LOG_TAG, "onCreateView for " + mTitle);
        View rootView = inflater.inflate(R.layout.recipe_details, container, false);
        ButterKnife.bind(this, rootView);
        setupSth();
        return rootView;
    }

    private void setupSth() {
        if (mStep != null) {
            mStepDetails.setText(mStep.getDescription());
            mContainer.setVisibility(View.VISIBLE);
            mSimpleExoPlayerView.setVisibility(View.GONE);

            if (TextUtils.isEmpty(mStep.getDescription())){
                Picasso.with(getContext())
                        .load(mStep.getThumbnailURL().isEmpty() ?  R.id.exo_artwork : R.id.exo_artwork)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                mSimpleExoPlayerView.setDefaultArtwork(bitmap);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                mSimpleExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.recipe_placeholder ));
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });

            }

            if (mStep.getVideoURL().contains("http")){
                simpleExoPlayer = setupPlayer(mStep.getVideoURL());
                setMediaUtils();
                mediaUtils.setExtras(mTitle, simpleExoPlayer);
                simpleExoPlayer.addListener(mediaUtils);
                mSimpleExoPlayerView.setPlayer(simpleExoPlayer);
                simpleExoPlayer.setPlayWhenReady(false);
                mSimpleExoPlayerView.setVisibility(View.VISIBLE);
                mValidPlayer = true;
            }
        }

    }

    private MediaSource newVideoSource(String url) {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        String userAgent = Util.getUserAgent(getActivity(), "BakingRecipe");
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(), userAgent, bandwidthMeter);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        return new ExtractorMediaSource(Uri.parse(url), dataSourceFactory, extractorsFactory, null, null);

    }

    private SimpleExoPlayer newSimpleExoPlayer() {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        return ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
    }

    private SimpleExoPlayer setupPlayer(String url) {
        mSimpleExoPlayerView.setUseController(false);
        HttpProxyCacheServer proxy = VideoCacheProxyFactory.getProxy(getContext());
        String proxyUrl = proxy.getProxyUrl(url);
        final SimpleExoPlayer exoPlayer = newSimpleExoPlayer();
        mSimpleExoPlayerView.setUseController(true);
        mSimpleExoPlayerView.requestFocus();
        mSimpleExoPlayerView.setPlayer(exoPlayer);
        MediaSource videoSource = newVideoSource(proxyUrl);
        final LoopingMediaSource loopingMediaSource = new LoopingMediaSource(videoSource);
        exoPlayer.prepare(loopingMediaSource);
        return exoPlayer;
    }

    @Override
    public void onStart() {
        super.onStart();
        getUserVisibleHint();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mValidPlayer){
            simpleExoPlayer.setPlayWhenReady(false);
        }
    }

    public void onSelected(){
        if (mValidPlayer){
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }
    public void onDeselected(){
        if (mValidPlayer){
            simpleExoPlayer.setPlayWhenReady(false);
        }
        Log.d(LOG_TAG, "onDeSelected for " + mTitle);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mValidPlayer){
            simpleExoPlayer.release();
        }
    }

    private void setMediaUtils(){
        if (getActivity() instanceof VideoPlayerActivity){
            this.mediaUtils = ((VideoPlayerActivity) getActivity()).getMediaUtils();
        } else if (getActivity() instanceof  RecipeDetails){

            FragmentManager frag = getFragmentManager();

            MasterListStepFragment stepFragment = ((MasterListStepFragment) frag.findFragmentById(R.id.masterStepContainer));
            this.mediaUtils = stepFragment.getMediaUtils();
        }
    }
}
