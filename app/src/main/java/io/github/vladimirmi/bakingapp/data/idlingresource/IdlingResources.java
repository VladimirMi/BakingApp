package io.github.vladimirmi.bakingapp.data.idlingresource;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Vladimir Mikhalev 16.03.2018.
 */

@Singleton
public class IdlingResources {

    private SimpleIdleResource resource;

    @Inject
    public IdlingResources() {
    }

    public SimpleIdleResource getResource() {
        if (resource == null) {
            resource = new SimpleIdleResource();
        }
        return resource;
    }

    public void setIdleState(boolean isIdleNow) {
        if (resource != null) {
            resource.setIdleState(isIdleNow);
        }
    }

    public static class SimpleIdleResource implements IdlingResource {

        @Nullable private volatile IdlingResource.ResourceCallback mCallback;

        // Idleness is controlled with this boolean.
        private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

        @Override
        public String getName() {
            return this.getClass().getName();
        }

        @Override
        public boolean isIdleNow() {
            return mIsIdleNow.get();
        }

        @Override
        public void registerIdleTransitionCallback(IdlingResource.ResourceCallback callback) {
            mCallback = callback;
        }

        /**
         * Sets the new idle state, if isIdleNow is true, it pings the {@link IdlingResource.ResourceCallback}.
         *
         * @param isIdleNow false if there are pending operations, true if idle.
         */
        public void setIdleState(boolean isIdleNow) {
            mIsIdleNow.set(isIdleNow);
            if (isIdleNow && mCallback != null) {
                mCallback.onTransitionToIdle();
            }
        }

    }
}
