package io.github.vladimirmi.bakingapp.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.data.Recipe;
import io.github.vladimirmi.bakingapp.data.RecipeRepository;
import io.github.vladimirmi.bakingapp.di.Scopes;

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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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

        final LiveData<Recipe> liveData = repository.getRecipeForWidget(widgetId);

        liveData.observeForever(new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                BakingWidgetProvider.updateAppWidget(getApplicationContext(),
                        appWidgetManager, widgetId, recipe);
                appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.widget_ingredients_list);

                liveData.removeObserver(this);

                stopSelfResult(startId);
            }
        });

        return START_NOT_STICKY;
    }
}
