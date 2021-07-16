package com.leslie.mrouter_api;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 作者：xjzhao
 * 时间：2021-07-13 16:39
 */
interface IParams {


    Class<?> getDestination();

    Bundle getExtras();

    int getFlags();

    String getAction();

    int getEnterAnim();

    int getExitAnim();

    Bundle getOptionsCompat();

    Params addInterceptor(MRouterInterceptor interceptor);

    MRouterInterceptor getInterceptor();

    Params with(@NonNull Bundle bundle);

    Params withInt(@NonNull String key, int value);

    Params withShort(@NonNull String key, short value);

    Params withLong(@NonNull String key, long value);

    Params withFloat(@NonNull String key, float value);

    Params withDouble(@NonNull String key, double value);

    Params withByte(@NonNull String key, byte value);

    Params withChar(@NonNull String key, @NonNull Character value);

    Params withString(@NonNull String key, @NonNull String value);

    Params withBoolean(@NonNull String key, boolean value);

    Params withCharSequence(@NonNull String key, @NonNull CharSequence value);

    Params withParcelable(@NonNull String key, @NonNull Parcelable value);

    Params withParcelableArray(@NonNull String key, @NonNull Parcelable[] value);

    Params withParcelableArrayList(@NonNull String key, @NonNull ArrayList<? extends Parcelable> value);

    Params withSparseParcelableArray(@NonNull String key, @NonNull SparseArray<? extends Parcelable> value);

    Params withIntegerArrayList(@NonNull String key, @NonNull ArrayList<Integer> value);

    Params withStringArrayList(@NonNull String key, @NonNull ArrayList<String> value);

    Params withCharSequenceArrayList(@NonNull String key, @NonNull ArrayList<CharSequence> value);

    Params withSerializable(@NonNull String key, @NonNull Serializable value);

    Params withByteArray(@NonNull String key, @NonNull byte[] value);

    Params withShortArray(@NonNull String key, @NonNull short[] value);

    Params withCharArray(@NonNull String key, @NonNull char[] value);

    Params withFloatArray(@NonNull String key, @NonNull float[] value);

    Params withCharSequenceArray(@NonNull String key, @NonNull CharSequence[] value);

    Params withObject(@NonNull String key, @NonNull Object obj);

    Params withBundle(@NonNull String key, @NonNull Bundle value);

    Params withTransition(int enterAnim, int exitAnim);

    @RequiresApi(16)
    Params withOptionsCompat(ActivityOptionsCompat compat);

    Params withAction(String action);

}
