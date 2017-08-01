package com.ng.tselebro.bakingapp.widget;


import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.ng.tselebro.bakingapp.R;
import com.ng.tselebro.bakingapp.recipe.MainActivity;
import com.ng.tselebro.bakingapp.recipeDetails.RecipeDetails;
import com.ng.tselebro.bakingapp.services.StepListWidgetService;

public class BakingAppWidget extends AppWidgetProvider {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        int columns = getCellsForSize(width);
        RemoteViews remoteViews = getStepListRemoteView(context, columns);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private static RemoteViews getStepListRemoteView(Context context, int columns) {
        RemoteViews views;
        if (columns > 1) {
            views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
            Intent intent = new Intent(context, StepListWidgetService.class);
            views.setRemoteAdapter(R.id.step_widget_list_view, intent);
            Intent appIntent = new Intent(context, RecipeDetails.class);
            PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.step_widget_list_view, appPendingIntent);
            views.setEmptyView(R.id.step_widget_list_view, R.id.empty_view);
        } else {
            views = new RemoteViews(context.getPackageName(), R.layout.recipewidget);
            Intent appIntent = new Intent(context, MainActivity.class);
            PendingIntent appPendingINtent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.recipe_widget_imageview, appPendingINtent);
        }

        return views;
    }

    private static int getCellsForSize(int size) {
        int n = 2;
        while (70 * n - 30 < size) {
            ++n;
        }
        return n - 1;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.step_widget_list_view);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.step_widget_list_view);
        updateAppWidget(context, appWidgetManager, appWidgetId);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }
}


