package com.leslie.mrouter_api;

import android.content.Context;

import com.leslie.mrouter_api.utils.logger;

/**
 * 作者：xjzhao
 * 时间：2021-07-15 16:48
 */
public abstract class MRouterInterceptor {
    private Context context;

    public MRouterInterceptor() {
    }

    public MRouterInterceptor(Context context) {
        this.context = context;
    }

    /**
     * return MRouter.getInstance().build().with***();
     *
     * @return
     */
    protected abstract Params getRouter();


    protected int getRequestCode(){return -1;}


    protected MRouterCallback getCallback(){return new MRouterCallback() {
        @Override
        public void onArrival(Params params) {
            logger.info("跳转到了LoginActivity页面");
        }
    };}

    protected Context getContext(){return context;}

}
