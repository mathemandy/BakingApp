package com.ng.tselebro.bakingapp.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ng.tselebro.bakingapp.Model.Ingredient;
import com.ng.tselebro.bakingapp.R;
import com.ng.tselebro.bakingapp.data.local.RecipeColumns;
import com.ng.tselebro.bakingapp.data.local.RecipeProvider;

import java.lang.reflect.Type;
import java.util.List;
public class IngredientsListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientListRemoteViewsFactory(this.getApplicationContext());

    }


}

class IngredientListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private List<Ingredient> mIngredientsJsonList;

    public IngredientListRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        int favoriteRecipeId = preferences.getInt(mContext.getString(R.string.pref_fav_recipe_id_key), Integer.parseInt(mContext.getString(R.string.pref_fav_recipe_id_default)));

        Cursor mCursor = mContext.getContentResolver().query(RecipeProvider.Recipes.CONTENT_RECIPES_URI, null, null, null, null);
        if (!(mCursor == null || mCursor.getCount() == 0)) {
            mCursor.moveToFirst();
            String mIngredientjson;
            int mRecipeid;
            do {
                mRecipeid = mCursor.getInt(mCursor.getColumnIndex(RecipeColumns.ID));
                mIngredientjson = mCursor.getString(mCursor.getColumnIndex(RecipeColumns.ingredient));
            } while (mCursor.moveToNext() && favoriteRecipeId != mRecipeid);
            mCursor.close();

            Type mCollectionIngredient = new TypeToken<List<Ingredient>>() {}.getType();
            mIngredientsJsonList = new Gson().fromJson(mIngredientjson, mCollectionIngredient);
        }

    }


    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIngredientsJsonList == null) return 0;
        return mIngredientsJsonList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_widget_item);
        views.setTextViewText(R.id.ingredients_widget_item_textView, mIngredientsJsonList.get(position).getIngredient());
        views.setTextViewText(R.id.ingredients_widget_quantity_textView, String.valueOf(mIngredientsJsonList.get(position).getQuantity()));
        views.setTextViewText(R.id.ingredients_widget_measure_textView, mIngredientsJsonList.get(position).getMeasure());

        return views;
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