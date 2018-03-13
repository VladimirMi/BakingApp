package io.github.vladimirmi.bakingapp.presentation.master;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.google.android.exoplayer2.Player;

import java.util.List;

import javax.inject.Inject;

import io.github.vladimirmi.bakingapp.data.PlayerHolder;
import io.github.vladimirmi.bakingapp.data.Recipe;
import io.github.vladimirmi.bakingapp.data.RecipeRepository;
import io.github.vladimirmi.bakingapp.data.Step;

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

    List<Step> getSteps() {
        return repository.getSelectedRecipe().getSteps();
    }

    void selectStepPosition(int position) {
        repository.selectStepPosition(position);
    }

    LiveData<Integer> getSelectedStepPosition() {
        return repository.getSelectedStepPosition();
    }

    LiveData<Step> getSelectedStep() {
        return Transformations.map(getSelectedStepPosition(), position -> getSteps().get(position));
    }

    Player getPlayer() {
        return player.get();
    }

    Recipe getSelectedRecipe() {
        return repository.getSelectedRecipe();
    }

    LiveData<Boolean> isCanShowMultimedia() {
        return repository.isCanShowMultimedia();
    }
}
