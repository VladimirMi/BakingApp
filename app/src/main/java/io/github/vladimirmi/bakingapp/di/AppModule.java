package io.github.vladimirmi.bakingapp.di;

import io.github.vladimirmi.bakingapp.data.net.RestService;
import io.github.vladimirmi.bakingapp.data.net.RestServiceProvider;
import toothpick.config.Module;

/**
 * Created by Vladimir Mikhalev 08.03.2018.
 */

public class AppModule extends Module {

    public AppModule() {
        bind(RestService.class).toInstance(RestServiceProvider.getService());
    }
}
