package io.github.vladimirmi.bakingapp.presentation.recipelist;

import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.github.vladimirmi.bakingapp.data.RecipeRepository;
import io.github.vladimirmi.bakingapp.data.entity.Recipe;
import io.github.vladimirmi.bakingapp.di.Scopes;
import io.github.vladimirmi.bakingapp.widget.WidgetUpdateService;
import io.reactivex.Single;

/**
 * Created by Vladimir Mikhalev 08.03.2018.
 */

public class RecipeListViewModel extends ViewModel {

    private final RecipeRepository repository;


    @Inject
    RecipeListViewModel(RecipeRepository repository) {
        this.repository = repository;
    }

    Single<List<Recipe>> getRecipes() {
        return repository.getRecipes();
    }

    void selectRecipe(int recipeId) {
        repository.selectRecipe(recipeId).subscribe();
    }

    void saveRecipeForWidget(int widgetId, Recipe recipe) {
        repository.saveRecipeForWidget(widgetId, recipe);
        WidgetUpdateService.startUpdateWidget(Scopes.appContext(), widgetId);
    }
}
