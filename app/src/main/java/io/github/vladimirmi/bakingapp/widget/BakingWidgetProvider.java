package io.github.vladimirmi.bakingapp.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.data.entity.Recipe;
import io.github.vladimirmi.bakingapp.presentation.master.MasterActivity;
import io.github.vladimirmi.bakingapp.presentation.recipelist.RecipeListActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe) {

        RemoteViews views = getWidgetView(context, appWidgetId, recipe);
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_ingredients_list);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            WidgetUpdateService.startUpdateWidget(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static RemoteViews getWidgetView(Context context, int widgetId, Recipe recipe) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        views.setTextViewText(R.id.widget_recipe_name, recipe.getName());
        Intent chooseRecipeIntent = new Intent(context, RecipeListActivity.class);
        chooseRecipeIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        PendingIntent chooseRecipePendingIntent = PendingIntent
                .getActivity(context, widgetId, chooseRecipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_recipe_name, chooseRecipePendingIntent);


        Intent adapterIntent = new Intent(context, IngredientViewsService.class);
        adapterIntent.setData(Uri.parse(String.valueOf(widgetId)));
        views.setRemoteAdapter(R.id.widget_ingredients_list, adapterIntent);

        Intent ingredientsIntent = new Intent(context, MasterActivity.class);
        ingredientsIntent.putExtra(RecipeListActivity.EXTRA_RECIPE_ID, recipe.getId());
        PendingIntent ingredientsPendingIntent = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(ingredientsIntent)
                .getPendingIntent(widgetId, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setPendingIntentTemplate(R.id.widget_ingredients_list, ingredientsPendingIntent);

        return views;
    }
}

