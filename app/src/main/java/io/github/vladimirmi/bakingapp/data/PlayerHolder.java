package io.github.vladimirmi.bakingapp.data;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.jakewharton.rxrelay2.BehaviorRelay;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.di.Scopes;
import timber.log.Timber;

/**
 * Created by Vladimir Mikhalev 10.03.2018.
 */

@Singleton
public class PlayerHolder {

    private SimpleExoPlayer player;
    private final Context context;
    final BehaviorRelay<PlayerStatus> playerStatus = BehaviorRelay.create();
    final BehaviorRelay<PlaybackStatus> playbackStatus = BehaviorRelay.create();
    private long lastPosition;
    private boolean lastPlayWhenReady;
    private String lastVideoUrl;

    private Player.DefaultEventListener listener = new Player.DefaultEventListener() {
        @Override
        public void onPlayerError(ExoPlaybackException error) {
            Timber.e(error);
            playerStatus.accept(PlayerStatus.values()[error.type]);
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_READY && playWhenReady) {
                playbackStatus.accept(PlaybackStatus.PLAYED);
            } else {
                playbackStatus.accept(PlaybackStatus.STOPPED);
            }
        }
    };

    @Inject
    public PlayerHolder(Context context) {
        this.context = context;
    }

    public SimpleExoPlayer getPlayer() {
        if (player == null) {
            createPlayer();
            if (lastVideoUrl != null) {
                Timber.d("again");
                prepare(lastVideoUrl);
            }
        }
        return player;
    }

    public void release() {
        if (player != null) {
            savePlayerState();

            Timber.d("release player");
            player.release();
            player = null;
        }
    }

    public void prepare(@NonNull String videoUrl) {
        Timber.d("prepare: %s", videoUrl.isEmpty() ? "EMPTY" : videoUrl);

        restorePlayerState(videoUrl);

        if (videoUrl.isEmpty()) {
            playerStatus.accept(PlayerStatus.SOURCE_ERROR);
            return;
        }
        playerStatus.accept(PlayerStatus.NORMAL);

        Uri videoUri = Uri.parse(videoUrl);

        String appName = context.getString(R.string.app_name);
        DataSource.Factory factory = new DefaultHttpDataSourceFactory(Util.getUserAgent(context, appName));

        MediaSource source = new ExtractorMediaSource.Factory(factory)
                .setExtractorsFactory(new DefaultExtractorsFactory())
                .createMediaSource(videoUri);

        player.prepare(source, false, true);
    }

    private void createPlayer() {
        Timber.d("create player");
        player = ExoPlayerFactory.newSimpleInstance(Scopes.appContext(), new DefaultTrackSelector());
        player.addListener(listener);
    }

    private void savePlayerState() {
        lastPosition = player.getCurrentPosition();
        lastPlayWhenReady = player.getPlayWhenReady();
    }

    private void restorePlayerState(String videoUrl) {
        if (player == null) createPlayer();

        if (!videoUrl.equals(lastVideoUrl)) {
            lastPosition = 0;
            lastPlayWhenReady = false;
        }
        player.setPlayWhenReady(lastPlayWhenReady);
        player.seekTo(lastPosition);
        lastVideoUrl = videoUrl;

    }

    public enum PlayerStatus {SOURCE_ERROR, RENDERER_ERROR, UNEXPECTED_ERROR, NORMAL}

    public enum PlaybackStatus {PLAYED, STOPPED}
}
