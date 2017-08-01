package com.ng.tselebro.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.ng.tselebro.bakingapp.R;
import com.ng.tselebro.bakingapp.data.local.RecipeColumns;
import com.ng.tselebro.bakingapp.data.local.RecipeProvider;
import com.ng.tselebro.bakingapp.services.IngredientsListWidgetService;

/**
 * Implementation of App widget functionality
 */

public class BakingIngredientwidget extends AppWidgetProvider {

    private static String mRecipeName;
    private static int mRecipeServings;

    private static void updateAppWidget(Context context,
                                        AppWidgetManager appWidgetManager,
                                        int appWidgetID) {
        Resources res = context.getResources();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int favoriteId =prefs.getInt(context.getString(R.string.pref_fav_recipe_id_key),  Integer.parseInt(context.getString(R.string.pref_fav_recipe_id_default)));
        Cursor mCursor = context.getContentResolver().query(RecipeProvider.Recipes.CONTENT_RECIPES_URI, null, null, null, null);

        if (!(mCursor == null || mCursor.getCount() == 0)) {
            int mRecipeId;
            mCursor.moveToFirst();

            do {
                mRecipeId = mCursor.getInt(mCursor.getColumnIndex(RecipeColumns.ID));
                mRecipeName = mCursor.getString(mCursor.getColumnIndex(RecipeColumns.recipeName));
                mRecipeServings = mCursor.getInt(mCursor.getColumnIndex(RecipeColumns.servings));

            } while (mCursor.moveToNext() && favoriteId != mRecipeId);
            mCursor.close();
        }
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_ingredient_widget);
        views.setTextViewText(R.id.ingredient_widget_title,  mRecipeName == null ? "database is Empty" : String.format(context.getString(R.string.ingredient_widget_title),  mRecipeName));
        views.setTextViewText(R.id.ingredient_widget_servings, res.getQuantityString(R.plurals.servings, mRecipeServings, mRecipeServings));
        Intent intent = new Intent(context, IngredientsListWidgetService.class);
        views.setRemoteAdapter(R.id.ingredient_widget_list_view, intent);
        views.setEmptyView(R.id.ingredient_widget_list_view, R.id.ingredient_empty_view);
        appWidgetManager.updateAppWidget(appWidgetID, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetID : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetID);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredient_widget_list_view);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }
}
