package com.ng.tselebro.bakingapp.recipeDetails.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ng.tselebro.bakingapp.Model.Recipe;
import com.ng.tselebro.bakingapp.R;
import com.ng.tselebro.bakingapp.recipeDetails.ExpandableDetailsIngredients;
import com.ng.tselebro.bakingapp.recipeDetails.adapters.recipeIngredientAdapter;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterListIngredientFragment extends Fragment{

    private static final String ARG_RECIPE_ITEM = "recipes";
    private Recipe mRecipe;
    private recipeIngredientAdapter adapter;

    @BindView(R.id.recyclerView)
    RecyclerView masterList;

    public MasterListIngredientFragment(){

    }

    public static MasterListIngredientFragment newInstance (Recipe mRecipe) {
        MasterListIngredientFragment fragment = new MasterListIngredientFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_RECIPE_ITEM, mRecipe);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
        mRecipe = getArguments().getParcelable(ARG_RECIPE_ITEM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        adapter = new recipeIngredientAdapter(makeIngredients(mRecipe));
        View view = inflater.inflate(R.layout.fragment_master_list,container, false);
        ButterKnife.bind(this, view);
        return  view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
    }

    private List<ExpandableDetailsIngredients> makeIngredients(Recipe recipe) {
        return Collections.singletonList(new ExpandableDetailsIngredients("Ingredients", recipe.getIngredients()));
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


}
