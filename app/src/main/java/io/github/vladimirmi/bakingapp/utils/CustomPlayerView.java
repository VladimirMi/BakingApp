package io.github.vladimirmi.bakingapp.utils;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;

import io.github.vladimirmi.bakingapp.R;

/**
 * Created by Vladimir Mikhalev 17.03.2018.
 */

public class CustomPlayerView extends PlayerView {

    private AspectRatioFrameLayout contentFrame;

    public CustomPlayerView(Context context) {
        this(context, null);
    }

    public CustomPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        contentFrame = findViewById(R.id.exo_content_frame);
    }

    public void resetSurfaceView() {
//        SurfaceView child = new SurfaceView(getContext());
//        getVideoSurfaceView() = child;
//        contentFrame.removeViewAt(0);
//        contentFrame.addView(child, 0);
    }


}
