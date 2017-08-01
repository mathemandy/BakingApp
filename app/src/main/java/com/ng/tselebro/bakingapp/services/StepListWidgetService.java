package com.ng.tselebro.bakingapp.services;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ng.tselebro.bakingapp.Model.Ingredient;
import com.ng.tselebro.bakingapp.Model.Step;
import com.ng.tselebro.bakingapp.R;
import com.ng.tselebro.bakingapp.data.local.RecipeColumns;
import com.ng.tselebro.bakingapp.data.local.RecipeProvider;
import com.ng.tselebro.bakingapp.recipe.MainActivity;

import java.lang.reflect.Type;
import java.util.List;


public class StepListWidgetService  extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StepListRemoteViewsFactory(this.getApplicationContext());
    }
}

class StepListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Cursor mCursor;
    private Context mContext;


    public StepListRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mCursor = mContext.getContentResolver().query(RecipeProvider.Recipes.CONTENT_RECIPES_URI, null, null, null, null);

    }

    @Override
    public void onDestroy() {
        mCursor.close();

    }

    @Override
    public int getCount() {
        if (mCursor == null)return 0;
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        long id = 0;
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.step_widget_item);
        if (mCursor == null || mCursor.getCount() == 0) return  null;
        if (mCursor.moveToPosition(position)){
            id = mCursor.getLong(mCursor.getColumnIndex(RecipeColumns.ID));
            String name = mCursor.getString(mCursor.getColumnIndex(RecipeColumns.recipeName));
            int servings = mCursor.getInt(mCursor.getColumnIndex(RecipeColumns.servings));
            String stepsJson = mCursor.getString(mCursor.getColumnIndex(RecipeColumns.steps));
            String IngredientJson = mCursor.getString(mCursor.getColumnIndex(RecipeColumns.ingredient));

            Resources res = mContext.getResources();

            Type collectionIngredient = new TypeToken<List<Ingredient>>(){}.getType();
            List<Ingredient> ingredientsJson = new Gson().fromJson(IngredientJson, collectionIngredient);


            Type collectionSteps = new TypeToken<List<Step>>(){}.getType();
            List<Step> StepJson = new Gson().fromJson(stepsJson, collectionSteps);

            int step_length = StepJson.size();
            int ingredient_length = ingredientsJson.size();


            views.setTextViewText(R.id.recipe_name_textView, name);
            views.setTextViewText(R.id.recipe_servings_textView, res.getQuantityString(R.plurals.servings, servings, servings) );
            views.setTextViewText(R.id.recipe_description_textView, String.format("%s %s", res.getQuantityString(R.plurals.ingredients, ingredient_length, ingredient_length), res.getQuantityString(R.plurals.steps, step_length, step_length)));

        }

        Bundle bundle = new Bundle();
        bundle.putLong(MainActivity.RECIPE_ID, id);
        Intent fillingIntent = new Intent();
        fillingIntent.putExtras(bundle);
        views.setOnClickFillInIntent(R.id.recipe_recyclerView_item_container, fillingIntent);
        return  views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}