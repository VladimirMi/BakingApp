package io.github.vladimirmi.bakingapp.presentation.detail.ingredients;

import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.github.vladimirmi.bakingapp.data.Ingredient;
import io.github.vladimirmi.bakingapp.data.PlayerHolder;
import io.github.vladimirmi.bakingapp.data.Recipe;
import io.github.vladimirmi.bakingapp.data.RecipeRepository;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Vladimir Mikhalev 09.03.2018.
 */

class IngredientsViewModel extends ViewModel {

    private final RecipeRepository repository;

    @Inject
    IngredientsViewModel(RecipeRepository repository, PlayerHolder player) {
        this.repository = repository;
    }

    Observable<List<Ingredient>> getIngredients() {
        return repository.getSelectedRecipe().map(Recipe::getIngredients)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
