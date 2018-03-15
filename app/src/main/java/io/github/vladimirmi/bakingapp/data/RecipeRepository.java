package io.github.vladimirmi.bakingapp.data;

import com.jakewharton.rxrelay2.BehaviorRelay;

import java.util.List;

import javax.inject.Inject;

import io.github.vladimirmi.bakingapp.data.net.RestService;
import io.github.vladimirmi.bakingapp.data.preferences.Preferences;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Vladimir Mikhalev 07.03.2018.
 */

public class RecipeRepository {

    private final PlayerHolder player;
    private final RestService rest;
    private final Preferences prefs;

    private BehaviorRelay<List<Recipe>> recipes = BehaviorRelay.create();
    private BehaviorRelay<Recipe> selectedRecipe = BehaviorRelay.create();
    private BehaviorRelay<Integer> selectedStepPosition = BehaviorRelay.create();

    @Inject
    public RecipeRepository(RestService restService, PlayerHolder player, Preferences preferences) {
        this.rest = restService;
        this.player = player;
        prefs = preferences;
    }

    public Single<List<Recipe>> getRecipes() {
        if (recipes.hasValue()) {
            return recipes.firstOrError();
        } else {
            return rest.getRecipes()
                    .doOnSuccess(res -> recipes.accept(res));
        }
    }

    public Completable selectRecipe(int recipeId) {
        return getRecipes().flatMapObservable(Observable::fromIterable)
                .filter(recipe -> recipe.getId() == recipeId)
                .doOnNext(recipe -> {
                    selectedRecipe.accept(recipe);
                    selectStepPosition(-1);
                })
                .ignoreElements();
    }

    public Observable<Recipe> getSelectedRecipe() {
        return selectedRecipe;
    }

    public void selectStepPosition(int position) {
        if (!selectedStepPosition.hasValue() || selectedStepPosition.getValue() != position) {
            selectedStepPosition.accept(position);
            if (position != -1) {
                Step step = getSelectedStep().blockingFirst();
                player.prepare(step.getVideoURL(), step.getThumbnailURL());
            }
        }
    }

    public Observable<Integer> getSelectedStepPosition() {
        return selectedStepPosition;
    }

    public Observable<Step> getSelectedStep() {
        return selectedStepPosition.filter(pos -> pos != -1)
                .map(pos -> selectedRecipe.getValue().getSteps().get(pos));
    }

    public Maybe<Recipe> getRecipeForWidget(int widgetId) {
        return getRecipes().filter(res -> !res.isEmpty())
                .map(res -> {
                    int defaultId = res.get(0).getId();

                    int recipeId = prefs.getRecipeId(widgetId, defaultId);
                    return findRecipe(res, recipeId);
                });
    }


    public void saveRecipeForWidget(int widgetId, Recipe recipe) {
        prefs.saveRecipeId(widgetId, recipe.getId());
    }

    public Observable<Boolean> isCanShowMultimedia() {
        return player.canShowMultimedia;
    }

    private Recipe findRecipe(List<Recipe> recipes, int id) {
        for (Recipe recipe : recipes) {
            if (recipe.getId() == id) {
                return recipe;
            }
        }
        throw new IllegalStateException("Can not find recipe with given id");
    }
}
