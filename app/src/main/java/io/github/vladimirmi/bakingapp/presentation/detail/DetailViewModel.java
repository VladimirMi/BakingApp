package io.github.vladimirmi.bakingapp.presentation.detail;

import android.arch.lifecycle.ViewModel;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.jakewharton.rxrelay2.BehaviorRelay;

import java.util.List;

import javax.inject.Inject;

import io.github.vladimirmi.bakingapp.data.PlayerHolder;
import io.github.vladimirmi.bakingapp.data.RecipeRepository;
import io.github.vladimirmi.bakingapp.data.entity.Recipe;
import io.github.vladimirmi.bakingapp.data.entity.Step;
import io.reactivex.Observable;

/**
 * Created by Vladimir Mikhalev 09.03.2018.
 */

class DetailViewModel extends ViewModel {

    private final RecipeRepository repository;
    private final PlayerHolder player;

    @Inject
    DetailViewModel(RecipeRepository repository, PlayerHolder player) {
        this.repository = repository;
        this.player = player;
    }

    @Override
    protected void onCleared() {
        player.release();
    }

    Observable<Integer> getSelectedStepPosition() {
        return repository.getSelectedStepPosition();
    }

    Observable<Step> getSelectedStep() {
        return repository.getSelectedStep();
    }

    Observable<List<Step>> getSteps() {
        return repository.getSelectedRecipe().map(Recipe::getSteps);
    }

    void selectStepPosition(int position) {
        repository.selectStepPosition(position);
    }

    Observable<Recipe> getSelectedRecipe() {
        return repository.getSelectedRecipe();
    }

    BehaviorRelay<PlayerHolder.PlayerStatus> getPlayerStatus() {
        return repository.getPlayerStatus();
    }

    SimpleExoPlayer getPlayer() {
        return player.get();
    }
}
