package io.github.vladimirmi.bakingapp.presentation;

import android.support.v7.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by Vladimir Mikhalev 15.03.2018.
 */

public class BaseActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected <T> void bindData(Observable<T> observable, Consumer<? super T> onNext) {
        compositeDisposable.add(observable.subscribe(onNext, Timber::e));
    }

    protected <T> void bindData(Single<T> single, Consumer<? super T> onSuccess) {
        compositeDisposable.add(single.subscribe(onSuccess, Timber::e));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}
