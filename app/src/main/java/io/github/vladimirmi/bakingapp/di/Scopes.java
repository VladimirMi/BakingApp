package io.github.vladimirmi.bakingapp.di;

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

    public static ViewModelFactory VmFactory() {
        return appScope().getInstance(ViewModelFactory.class);
    }

}
