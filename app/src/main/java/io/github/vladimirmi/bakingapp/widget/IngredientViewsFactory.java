package io.github.vladimirmi.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.data.RecipeRepository;
import io.github.vladimirmi.bakingapp.data.entity.Ingredient;
import io.github.vladimirmi.bakingapp.di.Scopes;

/**
 * Created by Vladimir Mikhalev 12.03.2018.
 */

public class IngredientViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;
    private final int widgetId;
    private List<Ingredient> ingredients = new ArrayList<>();

    public IngredientViewsFactory(Context context, int widgetId) {
        this.context = context;
        this.widgetId = widgetId;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        RecipeRepository repository = Scopes.appScope().getInstance(RecipeRepository.class);
        ingredients = repository.getRecipeForWidget(widgetId).blockingGet().getIngredients();
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_ingredient);
        Ingredient ingredient = ingredients.get(position);
        views.setTextViewText(R.id.ingredient_name, ingredient.getIngredient());
        views.setTextViewText(R.id.ingredient_quantity, ingredient.getQuantity());
        views.setOnClickFillInIntent(R.id.ingredient, new Intent());
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
