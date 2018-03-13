package io.github.vladimirmi.bakingapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Vladimir Mikhalev 13.03.2018.
 */

public class Utils {

    private Utils() {
    }


    public static void setImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url)
                .centerCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .crossFade()
                .into(view);
    }

    public static void setAspectRatio(View view) {
        final float defaultAspect = 16 / 9f;

        view.post(() -> {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height = (int) (view.getWidth() / defaultAspect);
            view.setLayoutParams(lp);
        });
    }

}
