package io.github.vladimirmi.bakingapp.presentation.master;

import android.arch.lifecycle.LiveData;
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

public class MasterViewModel extends ViewModel {

    private final RecipeRepository repository;
    private final PlayerHolder player;

    @Inject
    public MasterViewModel(RecipeRepository repository, PlayerHolder player) {
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

    public void selectStepPosition(int position) {
        repository.selectStepPosition(position);
    }

    public LiveData<Integer> getSelectedStepPosition() {
        return repository.getSelectedStepPosition();
    }

    public Player getPlayer() {
        return player.get();
    }

    public Recipe getSelectedRecipe() {
        return repository.getSelectedRecipe();
    }
}
