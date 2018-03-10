package io.github.vladimirmi.bakingapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
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

    private final PlayerHolder player;
    private final RestService rest;

    private List<Recipe> recipesCache;
    private Recipe selectedRecipe;
    private MutableLiveData<Integer> selectedStepPosition = new MutableLiveData<>();

    @Inject
    public RecipeRepository(RestService restService, PlayerHolder player) {
        this.rest = restService;
        this.player = player;
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

    public void selectRecipe(Recipe recipe) {
        selectedRecipe = recipe;
        selectedStepPosition.setValue(-1);
    }

    public Recipe getSelectedRecipe() {
        return selectedRecipe;
    }

    @SuppressWarnings("ConstantConditions")
    public void selectStepPosition(int position) {
        if (selectedStepPosition.getValue() == position) return;

        selectedStepPosition.setValue(position);
        Step step = selectedRecipe.getSteps().get(position);
        if (!step.getVideoURL().isEmpty()) {
            player.prepare(Uri.parse(step.getVideoURL()));
        }
    }

    public LiveData<Integer> getSelectedStepPosition() {
        return selectedStepPosition;
    }
}
