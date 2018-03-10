package io.github.vladimirmi.bakingapp.presentation.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import javax.inject.Inject;

import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.data.Recipe;
import io.github.vladimirmi.bakingapp.data.RecipeRepository;
import io.github.vladimirmi.bakingapp.data.Step;
import io.github.vladimirmi.bakingapp.di.Scopes;

/**
 * Created by Vladimir Mikhalev 09.03.2018.
 */

public class DetailViewModel extends ViewModel {

    private final RecipeRepository repository;
    private SimpleExoPlayer player;

    @Inject
    public DetailViewModel(RecipeRepository repository) {
        this.repository = repository;
    }

    @Override
    protected void onCleared() {
        releasePlayer();
    }

    LiveData<List<Step>> getSteps() {
        return Transformations.map(repository.getSelectedRecipe(), Recipe::getSteps);
    }

    Integer getSelectedStepPosition() {
        return repository.getSelectedStepPosition().getValue();
    }

    public void selectStepPosition(int position) {
        preparePlayer(repository.getSelectedRecipe().getValue().getSteps().get(position));
        repository.selectStepPosition(position);
    }

    public SimpleExoPlayer getPlayer() {
        if (player == null) {
            initPlayer();
        }
        return player;
    }

    private void initPlayer() {
        player = ExoPlayerFactory.newSimpleInstance(Scopes.appContext(), new DefaultTrackSelector());
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void preparePlayer(Step step) {
        Uri uri = Uri.parse(step.getVideoURL());
        if (uri.toString().isEmpty()) return;

        Context context = Scopes.appContext();
        String appName = context.getString(R.string.app_name);
        DataSource.Factory factory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, appName));

        MediaSource source = new ExtractorMediaSource.Factory(factory).createMediaSource(uri);

        getPlayer().prepare(source);
    }
}
