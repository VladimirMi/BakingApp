package io.github.vladimirmi.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import io.github.vladimirmi.bakingapp.presentation.recipelist.RecipeListActivity;
import timber.log.Timber;

/**
 * Created by Vladimir Mikhalev 12.03.2018.
 */

public class IngredientViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Timber.e("onGetViewFactory:  recipe id %s", intent.getIntExtra(RecipeListActivity.EXTRA_RECIPE_ID, 1));

        int recipeId = intent.getIntExtra(RecipeListActivity.EXTRA_RECIPE_ID, 1);
        return new IngredientViewsFactory(getApplicationContext(), recipeId);
    }
}
