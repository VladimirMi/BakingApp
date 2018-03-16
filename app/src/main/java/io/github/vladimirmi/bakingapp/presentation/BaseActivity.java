package io.github.vladimirmi.bakingapp.presentation;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by Vladimir Mikhalev 15.03.2018.
 */

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private boolean canPerformActionView = true;

    @Override
    protected void onResume() {
        super.onResume();
        canPerformActionView = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        canPerformActionView = false;
    }

    protected <T> void bindData(Observable<T> observable, Consumer<? super T> onNext) {
        compositeDisposable.add(observable
                .filter(t -> canPerformActionView)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, Timber::e));
    }

    protected <T> void bindData(Single<T> single, Consumer<? super T> onSuccess) {
        compositeDisposable.add(single
                .filter(t -> canPerformActionView)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, Timber::e));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}
