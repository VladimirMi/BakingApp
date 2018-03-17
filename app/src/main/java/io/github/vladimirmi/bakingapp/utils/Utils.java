package io.github.vladimirmi.bakingapp.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.github.vladimirmi.bakingapp.R;

/**
 * Created by Vladimir Mikhalev 13.03.2018.
 */

public class Utils {

    private Utils() {
    }

    public static void setImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .crossFade()
                .error(R.drawable.ic_error)
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

    @SuppressWarnings("ConstantConditions")
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay()
                .getMetrics(metrics);
        return metrics;
    }

}
