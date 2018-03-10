package io.github.vladimirmi.bakingapp.presentation.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.google.android.exoplayer2.Player;

import java.util.List;

import javax.inject.Inject;

import io.github.vladimirmi.bakingapp.data.PlayerHolder;
import io.github.vladimirmi.bakingapp.data.RecipeRepository;
import io.github.vladimirmi.bakingapp.data.Step;
import timber.log.Timber;

/**
 * Created by Vladimir Mikhalev 09.03.2018.
 */

public class DetailViewModel extends ViewModel {

    private final RecipeRepository repository;
    private final PlayerHolder player;

    @Inject
    public DetailViewModel(RecipeRepository repository, PlayerHolder player) {
        Timber.e("DetailViewModel: ");
        this.repository = repository;
        this.player = player;
    }

    @Override
    protected void onCleared() {
        Timber.e("onCleared: ");
        player.release();
    }

    LiveData<Integer> getSelectedStepPosition() {
        return repository.getSelectedStepPosition();
    }

    List<Step> getSteps() {
        return repository.getSelectedRecipe().getSteps();
    }

    void selectStepPosition(int position) {
        repository.selectStepPosition(position);
    }

    Player getPlayer() {
        return player.get();
    }

    LiveData<Boolean> isCanPlayVideo() {
        return Transformations.map(getSelectedStepPosition(), position -> {
            Step step = getSteps().get(position);
            return !step.getVideoURL().isEmpty();
        });
    }
}
