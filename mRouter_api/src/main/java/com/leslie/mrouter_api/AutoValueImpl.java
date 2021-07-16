package com.leslie.mrouter_api;

import android.util.LruCache;

import com.leslie.mrouter_annotation.Constant;
import com.leslie.mrouter_annotation.IInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理单个类中的@AutoValue注解并赋值
 *
 * 作者：xjzhao
 * 时间：2021-07-15 23:01
 */
class AutoValueImpl {
    private LruCache<String, IInject> classCache;
    private List<String> blackList;

    static void inject(Object target){
        new AutoValueImpl().init()._inject(target);
    }

    AutoValueImpl init() {
        classCache = new LruCache<>(66);
        blackList = new ArrayList<>();
        return this;
    }

    void _inject(Object instance) {
        String className = instance.getClass().getName();
        try {
            if (!blackList.contains(className)) {
                IInject inject = classCache.get(className);
                if (null == inject) {  // No cache.
                    inject = (IInject) Class.forName(instance.getClass().getName() + Constant.SUFFIX_INJECT).getConstructor().newInstance();
                }
                inject.inject(instance);
                classCache.put(className, inject);
            }
        } catch (Exception ex) {
            blackList.add(className);    // This instance need not autowired.
        }
    }
}
