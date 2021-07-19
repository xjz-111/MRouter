package com.leslie.mrouter_api;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;

import com.leslie.mrouter_annotation.RouterMeta;
import com.leslie.mrouter_annotation.RouterType;
import com.leslie.mrouter_api.utils.DefaultPoolExecutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * 作者：xjzhao
 * 时间：2021-07-13 16:37
 */
public class Params extends RouterMeta implements IParams{

    private Bundle bundle;
    private String action;
    private int flags = -1;
    // 跳转拦截器
    private MRouterInterceptor interceptor;

    // 传递Object时用到的处理工具
    private ISerialization serializationUtils;


    // Animation
    private Bundle optionsCompat;
    private int enterAnim = -1;
    private int exitAnim = -1;

    public Params(String group, String path) {
        setGroup(group);
        setPath(path);
        bundle = new Bundle();
    }

    @Override
    public Params addInterceptor(MRouterInterceptor interceptor){
        this.interceptor = interceptor;
        return this;
    }

    @Override
    public MRouterInterceptor getInterceptor() {
        return interceptor;
    }

    @Override
    public Bundle getExtras() {
        return bundle;
    }

    @Override
    public int getFlags() {
        return flags;
    }

    @Override
    public String getAction() {
        return action;
    }

    public int getEnterAnim() {
        return enterAnim;
    }

    @Override
    public int getExitAnim() {
        return exitAnim;
    }


    public Bundle getOptionsCompat() {
        return optionsCompat;
    }

    @Override
    public Params with(@NonNull Bundle bundle) {
        if (null == this.bundle){
            this.bundle = bundle;
        }else {
            this.bundle.putAll(bundle);
        }
        return this;
    }

    @Override
    public Params withInt(@NonNull String key, int value) {
        bundle.putInt(key, value);
        return this;
    }

    @Override
    public Params withShort(@NonNull String key, short value) {
        bundle.putShort(key, value);
        return this;
    }

    @Override
    public Params withLong(@NonNull String key, long value) {
        bundle.putLong(key, value);
        return this;
    }

    @Override
    public Params withFloat(@NonNull String key, float value) {
        bundle.putFloat(key, value);
        return this;
    }

    @Override
    public Params withDouble(@NonNull String key, double value) {
        bundle.putDouble(key, value);
        return this;
    }

    @Override
    public Params withByte(@NonNull String key, byte value) {
        bundle.putByte(key, value);
        return this;
    }

    @Override
    public Params withChar(@NonNull String key, @NonNull Character value) {
        bundle.putChar(key, value);
        return this;
    }

    @Override
    public Params withString(@NonNull String key, @NonNull String value) {
        bundle.putString(key, value);
        return this;
    }

    @Override
    public Params withBoolean(@NonNull String key, boolean value) {
        bundle.putBoolean(key, value);
        return this;
    }

    @Override
    public Params withCharSequence(@NonNull String key, @NonNull CharSequence value) {
        bundle.putCharSequence(key, value);
        return this;
    }

    @Override
    public Params withParcelable(@NonNull String key, @NonNull Parcelable value) {
        bundle.putParcelable(key, value);
        return this;
    }

    @Override
    public Params withParcelableArray(@NonNull String key, @NonNull Parcelable[] value) {
        bundle.putParcelableArray(key, value);
        return this;
    }

    @Override
    public Params withParcelableArrayList(@NonNull String key, @NonNull ArrayList<? extends Parcelable> value) {
        bundle.putParcelableArrayList(key, value);
        return this;
    }

    @Override
    public Params withSparseParcelableArray(@NonNull String key, @NonNull SparseArray<? extends Parcelable> value) {
        bundle.putSparseParcelableArray(key, value);
        return this;
    }

    @Override
    public Params withIntegerArrayList(@NonNull String key, @NonNull ArrayList<Integer> value) {
        bundle.putIntegerArrayList(key, value);
        return this;
    }

    @Override
    public Params withStringArrayList(@NonNull String key, @NonNull ArrayList<String> value) {
        bundle.putStringArrayList(key, value);
        return this;
    }

    @Override
    public Params withCharSequenceArrayList(@NonNull String key, @NonNull ArrayList<CharSequence> value) {
        bundle.putCharSequenceArrayList(key, value);
        return this;
    }

    @Override
    public Params withSerializable(@NonNull String key, @NonNull Serializable value) {
        bundle.putSerializable(key, value);
        return this;
    }

    @Override
    public Params withByteArray(@NonNull String key, @NonNull byte[] value) {
        bundle.putByteArray(key, value);
        return this;
    }

    @Override
    public Params withShortArray(@NonNull String key, @NonNull short[] value) {
        bundle.putShortArray(key, value);
        return this;
    }

    @Override
    public Params withCharArray(@NonNull String key, @NonNull char[] value) {
        bundle.putCharArray(key, value);
        return this;
    }

    @Override
    public Params withFloatArray(@NonNull String key, @NonNull float[] value) {
        bundle.putFloatArray(key, value);
        return this;
    }

    @Override
    public Params withCharSequenceArray(@NonNull String key, @NonNull CharSequence[] value) {
        bundle.putCharSequenceArray(key, value);
        return this;
    }

    @Override
    public Params withObject(@NonNull String key, @NonNull Object obj) {
        if (null == serializationUtils){
            serializationUtils = navigation(ISerialization.class);
        }
        bundle.putString(key, serializationUtils.object2Json(obj));
        return this;
    }

    @Override
    public Params withBundle(@NonNull String key, @NonNull Bundle value) {
        bundle.putBundle(key, value);
        return this;
    }

    @Override
    public Params withTransition(int enterAnim, int exitAnim) {
        this.enterAnim = enterAnim;
        this.exitAnim = exitAnim;
        return this;
    }

    @Override
    public Params withOptionsCompat(ActivityOptionsCompat compat) {
        if (null != compat){
            this.optionsCompat = compat.toBundle();
        }
        return this;
    }

    @Override
    public Params withAction(String action) {
        this.action = action;
        return this;
    }

    public <T> T navigation(){
        return _navigation(null, -1, null);
    }

    public <T> T navigation(@NonNull Activity activity){
        return _navigation(activity, -1, null);
    }

    public <T> T navigation(@NonNull Activity activity, int requestCode){
        return  _navigation(activity, requestCode, null);
    }

    public <T> T navigation(int requestCode){
        return _navigation(null, requestCode, null);
    }

    public <T> T navigation(MRouterCallback callback){
        return _navigation(null, -1, callback);
    }

    public <T> T navigation(@NonNull Activity activity, MRouterCallback callback){
        return _navigation(activity, -1, callback);
    }

    public <T> T navigation(@NonNull Activity activity, int requestCode, MRouterCallback callback){
        return _navigation(activity, requestCode, callback);
    }

    public <T> T navigation(int requestCode, MRouterCallback callback){
        return _navigation(null, requestCode, callback);
    }

    private <T> T _navigation(final Context context, final int requestCode, final MRouterCallback callback){

        if (getType() == RouterType.TYPE_ACTIVITY) {
            DefaultPoolExecutor.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    Navigation.getInstance().navigation(context, Params.this, requestCode, callback);
                }
            });
        }else {
            Navigation.getInstance().navigation(context, Params.this, requestCode, callback);
        }
        return null;
    }

    /**
     * 根据class获取对象
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T navigation(@NonNull Class<? extends T> cls) {
        setDestination(cls);
        try {
            RouterMeta meta;
            Map<String, RouterMeta> serializations =_MRouter.getInstance().getSerializations();
            meta = serializations.get(cls.getCanonicalName());

            if (null == meta){
                Map<String, RouterMeta> routers =_MRouter.getInstance().getRouters();
                meta = routers.get(cls.getCanonicalName());
            }

            if (null != meta){
                return (T) meta.getDestination().newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
