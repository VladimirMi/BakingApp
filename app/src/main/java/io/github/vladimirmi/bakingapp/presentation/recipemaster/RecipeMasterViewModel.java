package io.github.vladimirmi.bakingapp.presentation.recipemaster;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import io.github.vladimirmi.bakingapp.data.Recipe;
import io.github.vladimirmi.bakingapp.data.RecipeRepository;

/**
 * Created by Vladimir Mikhalev 09.03.2018.
 */

public class RecipeMasterViewModel extends ViewModel {

    private final RecipeRepository repository;
    private LiveData<Recipe> recipe;

    @Inject
    public RecipeMasterViewModel(RecipeRepository repository) {
        this.repository = repository;
    }

    LiveData<Recipe> getRecipe(int id) {
        recipe = repository.getRecipe(id);
        return recipe;
    }
}
