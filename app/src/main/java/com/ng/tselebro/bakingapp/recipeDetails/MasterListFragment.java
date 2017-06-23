package com.ng.tselebro.bakingapp.recipeDetails;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ng.tselebro.bakingapp.Model.POJO.Recipe;
import com.ng.tselebro.bakingapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterListFragment extends Fragment implements  recipeISContract.View{

    public static final String ARG_RECIPE_ITEM = "recipes";
    private Recipe  mRecipe;
    private recipeISContract.UserActionListener mActionListener;
    private recipeISAdapter adapter;

    @BindView(R.id.recyclerView)
    RecyclerView masterList;

    private View mProgressBar;
    private View mDetailView;
    private View mEmptyView;


    public MasterListFragment(){


    }

    public static  MasterListFragment newInstance (Recipe recipe) {
        MasterListFragment fragment = new MasterListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_RECIPE_ITEM, recipe);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
        mActionListener = new recipeISPresenter(this);
        mRecipe = getArguments().getParcelable(ARG_RECIPE_ITEM);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        adapter = new recipeISAdapter(makeSteps(mRecipe));

        View view = inflater.inflate(R.layout.fragment_master_list,container, false);
        ButterKnife.bind(this, view);

        return  view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
    }

    private List<ExpandableDetails> makeSteps(Recipe recipe) {
        return Arrays.asList(makeStep(recipe), makeIngredient(recipe));
    }

    public static  ExpandableDetails makeIngredient(Recipe recipe) {
        return  new ExpandableDetails("Steps", makeStepValues(recipe));
    }

    private static ExpandableDetails makeStep(Recipe recipe) {
        return  new ExpandableDetails("Ingredients", makeStepValues(recipe));
    }

    private static List<Recipe> makeStepValues(Recipe recipe) {
        List<Recipe> listRecipe = new ArrayList<>();
        listRecipe.add(recipe);

        return  listRecipe;
    }


    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        masterList.setLayoutManager(layoutManager);
        masterList.setItemAnimator(new DefaultItemAnimator());
        masterList.setAdapter(adapter);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(retain);
    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showMasterList(Recipe recipe) {

    }

    @Override
    public void showMasterDetails(Recipe recipe) {

    }
}
