package io.github.vladimirmi.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Vladimir Mikhalev 12.03.2018.
 */

public class IngredientViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 1);
        return new IngredientViewsFactory(getApplicationContext(), widgetId);
    }
}
