package com.leslie.mrouter;

import android.content.Context;

import com.leslie.mrouter_api.MRouter;
import com.leslie.mrouter_api.MRouterCallback;
import com.leslie.mrouter_api.MRouterInterceptor;
import com.leslie.mrouter_api.Params;

/**
 * 作者：xjzhao
 * 时间：2021-07-15 17:26
 */
public class LoginActivityInterceptor extends MRouterInterceptor {

    public LoginActivityInterceptor() {
    }

    public LoginActivityInterceptor(Context context) {
        super(context);
    }

    @Override
    protected Params getRouter() {
        return MRouter.getInstance().build("app/login").withInt("key", 121).withTransition(R.anim.left_in, R.anim.right_out);
    }


    @Override
    protected int getRequestCode() {
        return -1;
    }

    @Override
    protected MRouterCallback getCallback() {
        return params -> {};
    }
}
