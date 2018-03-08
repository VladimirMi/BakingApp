package io.github.vladimirmi.bakingapp.presentation.recipelist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.github.vladimirmi.bakingapp.data.Recipe;
import io.github.vladimirmi.bakingapp.data.RecipeRepository;

/**
 * Created by Vladimir Mikhalev 08.03.2018.
 */

public class RecipeListViewModel extends ViewModel {

    private final RecipeRepository repository;
    private LiveData<List<Recipe>> recipes;

    @Inject
    public RecipeListViewModel(RecipeRepository repository) {
        this.repository = repository;
        recipes = repository.getRecipes();
    }

    LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }
}
