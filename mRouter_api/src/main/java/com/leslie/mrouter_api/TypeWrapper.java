package com.leslie.mrouter_api;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 作者：xjzhao
 * 时间：2021-07-16 05:22
 */
public class TypeWrapper<T> {

    protected final Type type;

    protected TypeWrapper() {
        // 返回表示此Class所表示的实体（类、接口、基本类型或 void）的直接超类的Type - 超类的泛型如果有㛑会返回
        Type superClass = getClass().getGenericSuperclass();

        // 获取泛型的Type
        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }
}
