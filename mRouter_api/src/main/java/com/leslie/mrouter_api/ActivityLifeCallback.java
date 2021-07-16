package com.leslie.mrouter_api;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leslie.mrouter_api.utils.logger;

/**
 * Activity生命周期监听
 *
 * 作者：xjzhao
 * 时间：2021-07-15 16:33
 */
public class ActivityLifeCallback implements Application.ActivityLifecycleCallbacks {
    private OnListener onListener;

    public ActivityLifeCallback(@NonNull OnListener onListener) {
        this.onListener = onListener;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        logger.info(activity.getLocalClassName() + ": onActivityCreated");
        _MRouter.getInstance().inject(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        logger.info(activity.getLocalClassName() + ": onActivityStarted");
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        logger.info(activity.getLocalClassName() + ": onActivityResumed");
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        logger.error(activity.getLocalClassName() + ": onActivityPaused");
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        logger.error(activity.getLocalClassName() + ": onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        logger.info(activity.getLocalClassName() + ": onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        logger.error(activity.getLocalClassName() + ": onActivityDestroyed");
        onListener.onActivityDestroyed(activity);
    }

    interface OnListener{
        void onActivityDestroyed(@NonNull Activity activity);
    }
}
