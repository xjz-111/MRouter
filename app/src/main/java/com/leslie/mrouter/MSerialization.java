package com.leslie.mrouter;

import com.google.gson.Gson;
import com.leslie.mrouter_annotation.Router;
import com.leslie.mrouter_api.ISerialization;

import java.lang.reflect.Type;

/**
 * 作者：xjzhao
 * 时间：2021-07-16 05:16
 */
@Router(path = "app/MSerialization")
public class MSerialization implements ISerialization {
    private Gson gson = new Gson();

    @Override
    public String object2Json(Object instance) {
        return gson.toJson(instance);
    }

    @Override
    public <T> T json2Object(String s, Type clazz) {
        return gson.fromJson(s, clazz);
    }
}
