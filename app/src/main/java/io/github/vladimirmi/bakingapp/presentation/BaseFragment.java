package io.github.vladimirmi.bakingapp.presentation;

import android.support.v4.app.Fragment;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by Vladimir Mikhalev 15.03.2018.
 */

@SuppressWarnings("unused")
public class BaseFragment extends Fragment {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected <T> void bindData(Observable<T> observable, Consumer<? super T> onNext) {
        compositeDisposable.add(observable.subscribe(onNext, Timber::e));
    }

    protected <T> void bindData(Single<T> single, Consumer<? super T> onSuccess) {
        compositeDisposable.add(single.subscribe(onSuccess, Timber::e));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.dispose();
    }
}
