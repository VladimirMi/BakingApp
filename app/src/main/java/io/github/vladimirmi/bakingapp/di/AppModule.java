package io.github.vladimirmi.bakingapp.di;

import android.content.Context;

import io.github.vladimirmi.bakingapp.data.RecipeRepository;
import io.github.vladimirmi.bakingapp.data.net.RestService;
import io.github.vladimirmi.bakingapp.data.net.RestServiceProvider;
import toothpick.config.Module;

/**
 * Created by Vladimir Mikhalev 08.03.2018.
 */

public class AppModule extends Module {

    public AppModule(Context applicationContext) {
        bind(Context.class).toInstance(applicationContext);
        bind(ViewModelFactory.class).toInstance(new ViewModelFactory());
        bind(RestService.class).toInstance(RestServiceProvider.getService());

        bind(RecipeRepository.class).singletonInScope();
    }
}
