package io.github.vladimirmi.bakingapp.data;

import android.content.Context;
import android.net.Uri;

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

    @Inject
    public PlayerHolder(Context context) {
        this.context = context;
    }

    public SimpleExoPlayer get() {
        if (player == null) {
            Timber.e("create player");
            player = ExoPlayerFactory.newSimpleInstance(Scopes.appContext(),
                    new DefaultTrackSelector());

            player.addListener(new Player.DefaultEventListener() {
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
            });
        }
        return player;
    }

    public void release() {
        if (player != null) {
            Timber.e("release player");
            player.stop();
            player.release();
            player = null;
        }
    }

    public void prepare(String videoUrl) {
//        videoUrl = "http://techslides.com/demos/sample-videos/small.mp4";
//        videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4";

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

        get().prepare(source);
    }

    public enum PlayerStatus {SOURCE_ERROR, RENDERER_ERROR, UNEXPECTED_ERROR, NORMAL}

    public enum PlaybackStatus {PLAYED, STOPPED}
}
