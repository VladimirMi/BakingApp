package io.github.vladimirmi.bakingapp.presentation.master;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import io.github.vladimirmi.bakingapp.data.Recipe;
import io.github.vladimirmi.bakingapp.data.RecipeRepository;

/**
 * Created by Vladimir Mikhalev 09.03.2018.
 */

public class MasterViewModel extends ViewModel {

    private final RecipeRepository repository;

    @Inject
    public MasterViewModel(RecipeRepository repository) {
        this.repository = repository;
    }

    LiveData<Recipe> getRecipe(int id) {
        return repository.getRecipe(id);
    }
}
