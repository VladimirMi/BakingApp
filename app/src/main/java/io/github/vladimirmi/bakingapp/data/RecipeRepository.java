package io.github.vladimirmi.bakingapp.data;

import com.jakewharton.rxrelay2.BehaviorRelay;

import java.util.List;

import javax.inject.Inject;

import io.github.vladimirmi.bakingapp.data.entity.Recipe;
import io.github.vladimirmi.bakingapp.data.entity.Step;
import io.github.vladimirmi.bakingapp.data.idlingresource.IdlingResources;
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
    private final IdlingResources idlingResources;

    private final BehaviorRelay<List<Recipe>> recipes = BehaviorRelay.create();
    private final BehaviorRelay<Recipe> selectedRecipe = BehaviorRelay.create();
    private final BehaviorRelay<Integer> selectedStepPosition = BehaviorRelay.create();

    @Inject
    public RecipeRepository(RestService restService, PlayerHolder player, Preferences preferences,
                            IdlingResources idlingResources) {
        this.rest = restService;
        this.player = player;
        prefs = preferences;
        this.idlingResources = idlingResources;
    }

    public Single<List<Recipe>> getRecipes() {
        idlingResources.setIdleState(false);

        Single<List<Recipe>> single;
        if (recipes.hasValue()) {
            single = recipes.firstOrError();
        } else {
            single = rest.getRecipes().doOnSuccess(recipes);
        }
        return single.doOnSuccess(r -> idlingResources.setIdleState(true));
    }

    public Completable selectRecipe(int recipeId) {
        return getRecipes().flatMapObservable(Observable::fromIterable)
                .filter(recipe -> recipe.getId() == recipeId)
                .doOnNext(selectedRecipe)
                .ignoreElements();
    }

    public Observable<Recipe> getSelectedRecipe() {
        return selectedRecipe;
    }

    public void selectStepPosition(int position) {
        if (!selectedStepPosition.hasValue() || selectedStepPosition.getValue() != position) {
            selectedStepPosition.accept(position);
            if (position != -1) {
                Step step = selectedRecipe.getValue().getSteps().get(position);
                player.prepare(step.getVideoURL());
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

    public BehaviorRelay<PlayerHolder.PlayerStatus> getPlayerStatus() {
        return player.playerStatus;
    }

    public BehaviorRelay<PlayerHolder.PlaybackStatus> getPlaybackStatus() {
        return player.playbackStatus;
    }

    public IdlingResources.SimpleIdleResource getIdlingResource() {
        return idlingResources.getResource();
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
