package com.leslie.mrouter_api;

import android.app.Application;

import androidx.annotation.NonNull;

import com.leslie.mrouter_annotation.Constant;
import com.leslie.mrouter_api.utils.DefaultPoolExecutor;
import com.leslie.mrouter_api.utils.logger;

/**
 * 作者：xjzhao
 * 时间：2021-07-13 16:34
 */
public class MRouter {
    private static MRouter instance;

    public static MRouter getInstance() {
        if (null == instance){
            synchronized (MRouter.class){
                if (null == instance){
                    instance = new MRouter();
                }
            }
        }
        return instance;
    }

    public void init(@NonNull final Application application){
        DefaultPoolExecutor.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                _MRouter.getInstance().init(application);
            }
        });

    }


    public void isDebug(boolean isDebug){
        Constant.isDebug = isDebug;
        logger.showLog(isDebug);
    }

    public Params build(String path){
        return new Params(null, path);
    }

    public Params build(String group, String path){
        return new Params(group, path);
    }

    @Deprecated
    public Params build(){
        return new Params(null, null);
    }

    public void inject(@NonNull Object target) {
        _MRouter.getInstance().inject(target);
    }
}
