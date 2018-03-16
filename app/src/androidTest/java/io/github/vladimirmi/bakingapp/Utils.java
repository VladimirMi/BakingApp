package io.github.vladimirmi.bakingapp;

import android.app.Activity;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;

import java.util.Collection;
import java.util.Iterator;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Created by Vladimir Mikhalev 16.03.2018.
 */

public class Utils {

    public static Activity getCurrentActivity() {
        final Activity[] currentActivity = {null};

        getInstrumentation().runOnMainSync(() -> {
            Collection<Activity> resumedActivity = ActivityLifecycleMonitorRegistry.getInstance()
                    .getActivitiesInStage(Stage.RESUMED);
            Iterator<Activity> it = resumedActivity.iterator();
            currentActivity[0] = it.next();
        });

        return currentActivity[0];
    }
}
