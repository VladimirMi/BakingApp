package io.github.vladimirmi.bakingapp.data;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.di.Scopes;

/**
 * Created by Vladimir Mikhalev 10.03.2018.
 */

public class PlayerHolder {

    private SimpleExoPlayer player;
    private final Context context;

    public PlayerHolder(Context context) {
        this.context = context;
    }

    public SimpleExoPlayer get() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(Scopes.appContext(),
                    new DefaultTrackSelector());
        }
        return player;
    }

    public void release() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    public void prepare(Uri uri) {
        String appName = context.getString(R.string.app_name);
        DataSource.Factory factory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, appName));

        MediaSource source = new ExtractorMediaSource.Factory(factory)
                .setExtractorsFactory(new DefaultExtractorsFactory())
                .createMediaSource(uri);

        get().prepare(source);
    }

}
