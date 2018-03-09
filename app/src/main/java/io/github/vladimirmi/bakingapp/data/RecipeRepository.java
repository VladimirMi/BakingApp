package io.github.vladimirmi.bakingapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.github.vladimirmi.bakingapp.data.net.RestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Vladimir Mikhalev 07.03.2018.
 */

public class RecipeRepository {

    private RestService rest;
    private List<Recipe> recipesCache;

    @Inject
    public RecipeRepository(RestService restService) {
        this.rest = restService;
    }

    public LiveData<List<Recipe>> getRecipes() {
        final MutableLiveData<List<Recipe>> data = new MutableLiveData<>();
        if (recipesCache != null) {
            data.setValue(recipesCache);
        } else {

            rest.getRecipes().enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                    List<Recipe> recipes = response.body();
                    recipesCache = recipes;
                    data.setValue(recipes);
                }

                @Override
                public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                    Timber.e(t);
                }
            });
        }
        return data;
    }

    public LiveData<Recipe> getRecipe(int id) {
        final MutableLiveData<Recipe> data = new MutableLiveData<>();
        for (Recipe recipe : recipesCache) {
            if (recipe.getId() == id) {
                data.setValue(recipe);
                break;
            }
        }
        return data;
    }

}
