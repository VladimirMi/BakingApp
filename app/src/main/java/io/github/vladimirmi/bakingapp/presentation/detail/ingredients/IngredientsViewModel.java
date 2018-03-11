package io.github.vladimirmi.bakingapp.presentation.detail.ingredients;

import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.github.vladimirmi.bakingapp.data.Ingredient;
import io.github.vladimirmi.bakingapp.data.PlayerHolder;
import io.github.vladimirmi.bakingapp.data.RecipeRepository;

/**
 * Created by Vladimir Mikhalev 09.03.2018.
 */

public class IngredientsViewModel extends ViewModel {

    private final RecipeRepository repository;

    @Inject
    public IngredientsViewModel(RecipeRepository repository, PlayerHolder player) {
        this.repository = repository;
    }

    public List<Ingredient> getIngredients() {
        return repository.getSelectedRecipe().getIngredients();
    }
}
