package io.github.vladimirmi.bakingapp.presentation;

import android.support.v4.app.Fragment;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by Vladimir Mikhalev 15.03.2018.
 */

@SuppressWarnings("unused")
public class BaseFragment extends Fragment {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private boolean canPerformActionView = true;

    @Override
    public void onResume() {
        canPerformActionView = true;
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        canPerformActionView = false;
    }


    protected <T> void bindData(Observable<T> observable, Consumer<? super T> onNext) {
        compositeDisposable.add(observable
                .observeOn(AndroidSchedulers.mainThread())
                .filter(t -> canPerformActionView)
                .subscribe(onNext, Timber::e));
    }

    protected <T> void bindData(Single<T> single, Consumer<? super T> onSuccess) {
        compositeDisposable.add(single
                .filter(t -> canPerformActionView)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, Timber::e));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
