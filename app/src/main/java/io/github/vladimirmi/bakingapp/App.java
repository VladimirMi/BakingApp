package io.github.vladimirmi.bakingapp;

import android.app.Application;

import com.facebook.stetho.Stetho;

import io.github.vladimirmi.bakingapp.di.AppModule;
import io.github.vladimirmi.bakingapp.di.Scopes;
import timber.log.Timber;
import toothpick.Toothpick;
import toothpick.configuration.Configuration;

import static toothpick.registries.FactoryRegistryLocator.setRootRegistry;
import static toothpick.registries.MemberInjectorRegistryLocator.setRootRegistry;


/**
 * Class for maintaining global application state.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        if (BuildConfig.DEBUG) {
            Toothpick.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes());
        } else {
            Toothpick.setConfiguration(Configuration.forProduction().disableReflection());
            setRootRegistry(new FactoryRegistry());
            setRootRegistry(new MemberInjectorRegistry());
        }

        Scopes.appScope().installModules(new AppModule(getApplicationContext()));
    }
}
