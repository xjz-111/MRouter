package com.leslie.mrouter_api;

import java.lang.reflect.Type;

/**
 * 要传递自己定非序列化的数据，需在项目中实现该接口
 *
 * 作者：xjzhao
 * 时间：2021-07-16 05:13
 */
public interface ISerialization {

    String object2Json(Object instance);


    <T> T json2Object(String s, Type clazz);

}
