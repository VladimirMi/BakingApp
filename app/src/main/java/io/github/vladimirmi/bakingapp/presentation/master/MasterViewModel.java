package io.github.vladimirmi.bakingapp.presentation.master;

import android.arch.lifecycle.ViewModel;

import com.google.android.exoplayer2.Player;

import java.util.List;

import javax.inject.Inject;

import io.github.vladimirmi.bakingapp.data.PlayerHolder;
import io.github.vladimirmi.bakingapp.data.RecipeRepository;
import io.github.vladimirmi.bakingapp.data.entity.Recipe;
import io.github.vladimirmi.bakingapp.data.entity.Step;
import io.reactivex.Observable;
import timber.log.Timber;

/**
 * Created by Vladimir Mikhalev 09.03.2018.
 */

class MasterViewModel extends ViewModel {

    private final RecipeRepository repository;
    private final PlayerHolder player;

    @Inject
    MasterViewModel(RecipeRepository repository, PlayerHolder player) {
        this.repository = repository;
        this.player = player;
    }

    @Override
    protected void onCleared() {
        player.release();
    }

    Observable<List<Step>> getSteps() {
        return repository.getSelectedRecipe().map(Recipe::getSteps);
    }

    void selectStepPosition(int position) {
        repository.selectStepPosition(position);
    }

    void selectIngredients() {
        selectStepPosition(-1);
    }

    Observable<Integer> getSelectedStepPosition() {
        return repository.getSelectedStepPosition();
    }

    Observable<Step> getSelectedStep() {
        return repository.getSelectedStep();
    }

    Observable<Recipe> getSelectedRecipe() {
        return repository.getSelectedRecipe();
    }

    Observable<PlayerHolder.PlayerStatus> getPlayerStatus() {
        return repository.getPlayerStatus();
    }

    Observable<PlayerHolder.PlaybackStatus> getPlaybackStatus() {
        return repository.getPlaybackStatus();
    }

    void selectRecipe(int recipeId) {
        repository.selectRecipe(recipeId).subscribe(() -> {
        }, Timber::e);
    }

    Player getPlayer() {
        return player.getPlayer();
    }

    void releasePlayer() {
        player.release();
    }
}
