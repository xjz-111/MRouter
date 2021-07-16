package com.leslie.mrouter;

import android.app.Application;

import com.leslie.mrouter_api.MRouter;

/**
 * 作者：xjzhao
 * 时间：2021-07-15 12:08
 */
public class MApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MRouter.getInstance().isDebug(BuildConfig.DEBUG);
        MRouter.getInstance().init(this);
    }
}
