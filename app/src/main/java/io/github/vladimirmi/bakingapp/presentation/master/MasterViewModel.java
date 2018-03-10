package io.github.vladimirmi.bakingapp.presentation.master;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.github.vladimirmi.bakingapp.data.RecipeRepository;
import io.github.vladimirmi.bakingapp.data.Step;

/**
 * Created by Vladimir Mikhalev 09.03.2018.
 */

public class MasterViewModel extends ViewModel {

    private final RecipeRepository repository;

    @Inject
    public MasterViewModel(RecipeRepository repository) {
        this.repository = repository;
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
}
