package com.leslie.mrouter_annotation;

/**
 * 作者：xjzhao
 * 时间：2021-07-15 11:56
 */
public class Utils {

    public static String[] getPath(String group, String path){
        if (null == path || "".equals(path)){
            throw new AssertionError("Router路劲至少要两级");
        }

        if (null == group || "".equals(group)){
            if (path.indexOf("/") > 1){
                String[] split = path.split("/");
                group = split[0];
            }else {
                throw new AssertionError("Router路劲至少要两级");
            }
        }else {
            path = group + "/" + path;
        }
        return new String[]{group, path};
    }
}
