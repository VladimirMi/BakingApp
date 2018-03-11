package io.github.vladimirmi.bakingapp.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.config.Module;

/**
 * Created by Vladimir Mikhalev 08.03.2018.
 */

public class ViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        Scope scope = Toothpick.openScopes(Scopes.APP_SCOPE, modelClass);

        scope.installModules(new Module() {
            {
                bind(modelClass).singletonInScope();
            }
        });
        T viewModel = scope.getInstance(modelClass);

        Toothpick.closeScope(modelClass);

        return viewModel;
    }
}
