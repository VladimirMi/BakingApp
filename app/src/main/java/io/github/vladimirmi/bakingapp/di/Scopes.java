package io.github.vladimirmi.bakingapp.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by Vladimir Mikhalev 08.03.2018.
 */

public class Scopes {

    public static final String APP_SCOPE = "APP_SCOPE";

    private Scopes() {
    }

    public static Scope appScope() {
        return Toothpick.openScope(APP_SCOPE);
    }

    public static Context appContext() {
        return appScope().getInstance(Context.class);
    }

    public static <T extends ViewModel> T getViewModel(@NonNull FragmentActivity owner,
                                                       @NonNull Class<T> modelClass) {
        return ViewModelProviders.of(owner, viewModelFactory()).get(modelClass);
    }

    public static ViewModelFactory viewModelFactory() {
        return appScope().getInstance(ViewModelFactory.class);
    }

    public static <T extends ViewModel> T getViewModel(@NonNull Fragment owner,
                                                       @NonNull Class<T> modelClass) {
        return ViewModelProviders.of(owner, viewModelFactory()).get(modelClass);
    }


}
