package io.github.vladimirmi.bakingapp.presentation.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.github.vladimirmi.bakingapp.data.Recipe;
import io.github.vladimirmi.bakingapp.data.RecipeRepository;
import io.github.vladimirmi.bakingapp.data.Step;

/**
 * Created by Vladimir Mikhalev 09.03.2018.
 */

public class DetailViewModel extends ViewModel {

    private final RecipeRepository repository;

    @Inject
    public DetailViewModel(RecipeRepository repository) {
        this.repository = repository;
    }

    LiveData<List<Step>> getSteps(int recipeId) {
        return Transformations.map(repository.getRecipe(recipeId), Recipe::getSteps);
    }
}
