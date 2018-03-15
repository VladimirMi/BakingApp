package io.github.vladimirmi.bakingapp.data.preferences;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.SparseArray;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Preferences {

    private static final String KEY_WIDGET_ID = "WIDGET_ID_";
    private final SharedPreferences prefs;

    private final SparseArray<Preference<Integer>> recipeIds = new SparseArray<>();

    @Inject
    public Preferences(Context context) {
        prefs = context.getSharedPreferences("default", Context.MODE_PRIVATE);
    }


    public void saveRecipeId(int widgetId, int recipeId) {
        checkMap(widgetId, recipeId);
        recipeIds.get(widgetId).put(recipeId);
    }

    public int getRecipeId(int widgetId, int defaultId) {
        checkMap(widgetId, defaultId);
        return recipeIds.get(widgetId).get();
    }

    private void checkMap(int widgetId, int defaultRecipeId) {
        if (recipeIds.get(widgetId) == null) {
            recipeIds.put(widgetId, new Preference<>(prefs, KEY_WIDGET_ID + widgetId, defaultRecipeId));
        }
    }
}
