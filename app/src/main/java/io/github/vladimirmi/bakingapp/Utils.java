package io.github.vladimirmi.bakingapp;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

/**
 * Created by Vladimir Mikhalev 10.03.2018.
 */

public class Utils {

    @MainThread
    public static <X, Y> LiveData<X> doOnChange(@NonNull LiveData<X> source,
                                                @NonNull final Function<X, Y> fun) {
        final MediatorLiveData<X> result = new MediatorLiveData<>();
        result.addSource(source, x -> {
            result.setValue(x);
            fun.apply(x);
        });
        return result;
    }

}
