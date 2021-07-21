package com.leslie.mrouter_api;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.leslie.mrouter_annotation.Constant;
import com.leslie.mrouter_annotation.IInstance;
import com.leslie.mrouter_annotation.IJson;
import com.leslie.mrouter_annotation.IRouter;
import com.leslie.mrouter_annotation.RouterMeta;
import com.leslie.mrouter_api.utils.ClassUtils;
import com.leslie.mrouter_api.utils.VersionUtils;
import com.leslie.mrouter_api.utils.logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.leslie.mrouter_annotation.Constant.GENERATE_PACKAGER;

/**
 * 作者：xjzhao
 * 时间：2021-07-15 11:43
 */
class _MRouter {

    private static _MRouter instance;
    // path作key
    private static Map<String, RouterMeta> routers;
    /**
     *class全路径做key，是 {@link ISerialization}的全路径名称
     */
    private static Map<String, RouterMeta> jsons;

    private static Map<String, RouterMeta> instances;

    static _MRouter getInstance() {
        if (null == instance) {
            synchronized (MRouter.class) {
                if (null == instance) {
                    instance = new _MRouter();
                }
            }
        }
        return instance;
    }


    synchronized void init(Application context) {
        if (null != routers)
            throw new AssertionError("不能重复初始化");
        try {
            routers = new HashMap<>();
            jsons = new HashMap<>();
            instances = new HashMap<>();
            Set<String> files = null;
            if (Constant.isDebug || VersionUtils.isNewVersion(context)) {
                files = ClassUtils.getFileNameByPackageName(context, GENERATE_PACKAGER);
                if (!files.isEmpty()) {
                    context.getSharedPreferences(Constant.MROUTER_CACHE_SP_KEY, Context.MODE_PRIVATE).edit().putStringSet(Constant.MROUTER_CACHE_SP_KEY_MAP, files).apply();
                }
                VersionUtils.updateVersion(context);
            } else {
                files = new HashSet<>(context.getSharedPreferences(Constant.MROUTER_CACHE_SP_KEY, Context.MODE_PRIVATE).getStringSet(Constant.MROUTER_CACHE_SP_KEY_MAP, new HashSet<String>()));
            }
            logger.info("扫描出所有的文件：" + files);
            for (String className : files) {
                if (className.startsWith(Constant.CLASS_START_NAME)) {
                    IRouter iRouter = (IRouter) Class.forName(className).getConstructor().newInstance();
                    iRouter.load(routers);
                } else if (className.startsWith(Constant.JSON_START_NAME)) {
                    IJson iJson = (IJson) Class.forName(className).getConstructor().newInstance();
                    iJson.load(jsons);
                } else if (className.startsWith(Constant.INSTANCE_START_NAME)){
                    IInstance iInstance = (IInstance) Class.forName(className).getConstructor().newInstance();
                    iInstance.load(instances);
                }
            }
            Navigation.getInstance().init(context);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, RouterMeta> getRouters() {
        return routers;
    }

    public Map<String, RouterMeta> getSerializations() {
        return jsons;
    }

    public Map<String, RouterMeta> getInstances() {
        return instances;
    }

    void inject(@NonNull Object target) {
        AutoValueImpl.inject(target);
    }


    public RouterMeta getRouterMeta(String key){
        RouterMeta meta = getSerializations().get(key);
        if (null == meta) meta = getInstances().get(key);
        if (null == meta) meta = getRouters().get(key);
        return meta;
    }

    public RouterMeta getRouterMeta(@NonNull Class<?> cls){
        RouterMeta meta = getSerializations().get(cls.getCanonicalName());
        if (null == meta) meta = getRouterMeta(getInstances(), cls);
        if (null == meta) meta = getRouterMeta(getRouters(), cls);
        return meta;
    }

    private RouterMeta getRouterMeta(@NonNull Map<String, RouterMeta> map, @NonNull Class<?> cls){
        RouterMeta meta = null;
        for (String key : map.keySet()){
            meta = map.get(key);
            if (null != meta && meta.getDestination() == cls){
                return meta;
            }
        }
        return null;
    }

    public boolean isInitSuccess(){
        return Navigation.getInstance().isInitFinished();
    }
}
