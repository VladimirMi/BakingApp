package io.github.vladimirmi.bakingapp.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import io.github.vladimirmi.bakingapp.data.RecipeRepository;
import io.github.vladimirmi.bakingapp.di.Scopes;
import timber.log.Timber;

/**
 * Created by Vladimir Mikhalev 12.03.2018.
 */

public class WidgetUpdateService extends Service {

    private RecipeRepository repository;

    public static void startUpdateWidget(Context context, int widgetId) {
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        repository = Scopes.appScope().getInstance(RecipeRepository.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        repository.getRecipeForWidget(widgetId)
                .subscribe(recipe -> {
                    BakingWidgetProvider.updateAppWidget(getApplicationContext(),
                            appWidgetManager, widgetId, recipe);

                    stopSelf(startId);
                }, Timber::e);

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
