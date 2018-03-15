package io.github.vladimirmi.bakingapp.data;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
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

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.di.Scopes;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by Vladimir Mikhalev 10.03.2018.
 */

@Singleton
public class PlayerHolder {

    private SimpleExoPlayer player;
    private final Context context;
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private boolean canShowVideo;
    private boolean canShowThumb;
    private String lastVideoUrl;
    private String lastThumbUrl;
    final BehaviorRelay<Boolean> canShowMultimedia = BehaviorRelay.createDefault(false);

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
                    setCanShowVideo(false);
                }
            });
            if (lastVideoUrl != null && lastThumbUrl != null) {
                prepare(lastVideoUrl, lastThumbUrl);
            }
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

    public void prepare(String videoUrl, String thumbUrl) {
        lastVideoUrl = videoUrl;
        lastThumbUrl = thumbUrl;
        setCanShowThumb(true);
        setCanShowVideo(true);
        checkThumb(thumbUrl);

//        Uri videoUri = Uri.parse("http://techslides.com/demos/sample-videos/small.mp4");
        Uri videoUri = Uri.parse(videoUrl);

        String appName = context.getString(R.string.app_name);
        DataSource.Factory factory = new DefaultHttpDataSourceFactory(Util.getUserAgent(context, appName));

        MediaSource source = new ExtractorMediaSource.Factory(factory)
                .setExtractorsFactory(new DefaultExtractorsFactory())
                .createMediaSource(videoUri);

        mainThreadHandler.post(() -> get().prepare(source));
    }

    private void checkThumb(String thumbUrl) {
        OkHttpClient client = Scopes.appScope().getInstance(OkHttpClient.class);
        try {
            final Request req = new Request.Builder().url(thumbUrl).get().build();
            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Timber.e(e);
                    setCanShowThumb(false);
                }

                @SuppressWarnings("ConstantConditions")
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.body() != null) {
                        MediaType mediaType = response.body().contentType();
                        if (mediaType.type().equals("image")) {
                            setCanShowThumb(true);
                            return;
                        } else if (mediaType.type().equals("video")) {
                            prepare(thumbUrl, "");
                        }
                        response.body().close();
                    }
                    setCanShowThumb(false);
                }
            });
        } catch (RuntimeException e) {
            setCanShowThumb(false);
        }
    }

    private void setCanShowVideo(boolean canShowVideo) {
        this.canShowVideo = canShowVideo;
        canShowMultimedia.accept(canShowVideo || canShowThumb);
    }

    private void setCanShowThumb(boolean canShowThumb) {
        this.canShowThumb = canShowThumb;
        canShowMultimedia.accept(canShowVideo || canShowThumb);
    }
}
