package io.github.vladimirmi.bakingapp.presentation;

import android.annotation.SuppressLint;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

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
    protected View contentView;

    @Override
    protected void onResume() {
        super.onResume();
        canPerformActionView = true;
        contentView = findViewById(android.R.id.content);
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

    protected void keepScreenOn(boolean keep) {
        if (keep) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    protected void enterFullScreen() {
        int visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        getWindow().getDecorView().setSystemUiVisibility(visibility);
    }

    protected void showSnack(int stringRes) {
        Snackbar.make(contentView, stringRes, Snackbar.LENGTH_SHORT).show();
    }
}
