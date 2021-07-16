package com.leslie.mrouter_api;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.leslie.mrouter_annotation.RouterMeta;
import com.leslie.mrouter_annotation.Utils;

import java.util.Map;

/**
 * 作者：xjzhao
 * 时间：2021-07-13 19:22
 */
class Navigation {
    private static Navigation instance;
    private static Application context;
    private static Handler handler;
    private Inner inner;

    public synchronized void init(Application application) {
        context = application;
        handler = new Handler(Looper.getMainLooper());
        // 生命周期监听
        application.registerActivityLifecycleCallbacks(new  ActivityLifeCallback(new ActivityLifeCallback.OnListener() {
            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                if (null != inner){
                    navigation(inner.context, inner.params, inner.requestCode, inner.callback);
                }
            }
        }));
    }

    public static Navigation getInstance(){
        if (null == instance){
            synchronized (Navigation.class){
                if (null == instance){
                    instance = new Navigation();
                }
            }
        }
        return instance;
    }

    Object navigation(final Context context, @NonNull final Params params, final int requestCode, final MRouterCallback callback) {
        MRouterInterceptor interceptor = params.getInterceptor();
        if (null != interceptor){
            Params iParams = interceptor.getRouter();
            params.addInterceptor(null);
            resetInner(new Inner(context, params, requestCode, callback));
            return  parse(interceptor.getContext(), iParams, interceptor.getRequestCode(), interceptor.getCallback());
        }else {
            resetInner(null);
            return parse(context, params, requestCode, callback);
        }
    }

    private Object parse(Context context, Params params, int requestCode, MRouterCallback callback){
        String[] result = Utils.getPath(params.getGroup(), params.getPath());
        Map<String, RouterMeta> routers = _MRouter.getInstance().getRouters();
        Map<String, RouterMeta> jsons = _MRouter.getInstance().getSerializations();
        RouterMeta meta = routers.get(result[1]);
        if (null == meta){
            meta = jsons.get(result[1]);
        }
        if (null != meta){
            params.setGroup(result[0]);
            params.setPath(result[1]);
            params.setDestination(meta.getDestination());
            params.setType(meta.getType());
        }
        return _go(context, params, requestCode, callback);
    }

    Object _go(final Context context, @NonNull final Params params, final int requestCode, final MRouterCallback callback){
        final Context currentContext = null == context ? Navigation.context : context;
        switch (params.getType()) {
            case TYPE_ACTIVITY:
                final Intent intent = new Intent(currentContext, params.getDestination());
                intent.putExtras(params.getExtras());

                // flags
                int flags = params.getFlags();
                if (-1 != flags) {
                    intent.setFlags(flags);
                } else if (!(currentContext instanceof Activity)) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }

                // Set Actions
                String action = params.getAction();
                if (!TextUtils.isEmpty(action)) {
                    intent.setAction(action);
                }

                // Navigation in main looper.
                runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(requestCode, currentContext, intent, params, callback);
                    }
                });

                break;
            case TYPE_FTAGMENT:
                Class fragmentMeta = params.getDestination();
                try {
                    Object instance = fragmentMeta.getConstructor().newInstance();
                    if (instance instanceof Fragment) {
                        ((Fragment) instance).setArguments(params.getExtras());
                    } else if (instance instanceof androidx.fragment.app.Fragment) {
                        ((androidx.fragment.app.Fragment) instance).setArguments(params.getExtras());
                    }

                    return instance;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            case TYPE_JSON:
                try {
                    Class serialization = params.getDestination();
                    return serialization.getConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            default:
                return null;
        }

        return null;
    }

    private void startActivity(int requestCode, Context currentContext, Intent intent, Params params, MRouterCallback callback) {
        if (requestCode >= 0) {
            if (currentContext instanceof Activity) {
                ActivityCompat.startActivityForResult((Activity) currentContext, intent, requestCode, params.getOptionsCompat());
            } else {
                throw new AssertionError("有requestCode时，必须使用Activity: navigation(activity)");
            }
        } else {
            ActivityCompat.startActivity(currentContext, intent, params.getOptionsCompat());
        }

        if ((-1 != params.getEnterAnim() && -1 != params.getExitAnim()) && currentContext instanceof Activity) {
            ((Activity) currentContext).overridePendingTransition(params.getEnterAnim(), params.getExitAnim());
        }

        if (null != callback) {
            callback.onArrival(params);
        }
    }

    private void runInMainThread(Runnable runnable) {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            handler.post(runnable);
        } else {
            runnable.run();
        }
    }

    private void resetInner(Inner inner){
        this.inner = inner;
    }


    class Inner {
        private Context context;
        private Params params;
        private int requestCode;
        private MRouterCallback callback;

        public Inner(Context context, Params params, int requestCode, MRouterCallback callback) {
            this.context = context;
            this.params = params;
            this.requestCode = requestCode;
            this.callback = callback;
        }
    }
}
