package io.github.vladimirmi.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Vladimir Mikhalev 12.03.2018.
 */

public class IngredientViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int widgetId = Integer.valueOf(intent.getData().toString());
        return new IngredientViewsFactory(getApplicationContext(), widgetId);
    }
}
