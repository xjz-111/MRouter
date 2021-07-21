package com.leslie.mrouter_annotation;

/**
 * 作者：xjzhao
 * 时间：2021-07-13 15:50
 */
public class Constant {

    public static final String TAG = "MRouter";

    public static boolean isDebug = false;


    public static final String FILE_NAME_AUTOVALUE = "$$AutoValue";

    public static final String SUFFIX_INJECT = FILE_NAME_AUTOVALUE;

    public static final String GENERATE_PACKAGER = "com.leslie.android.mrouter";

    public static final String ROUTER_PACKAGE_SUFFIX = "router";

    public static final String JSON_PACKAGE_SUFFIX = "json";

    public static final String INSTANCE_PACKAGE_SUFFIX = "instance";

    public static final String FILE_NAME_START = "_MRouter$$Router$$";

    public static final String JSON_FILE_NAME_START = "_MRouter$$Json$$";

    public static final String INSTANCE_FILE_NAME_START = "_MRouter$$Instance$$";

    public static final String CLASS_START_NAME =  GENERATE_PACKAGER + "."  + ROUTER_PACKAGE_SUFFIX + "." + FILE_NAME_START;

    public static final String JSON_START_NAME = GENERATE_PACKAGER + "."  + JSON_PACKAGE_SUFFIX + "." + JSON_FILE_NAME_START;

    public static final String INSTANCE_START_NAME = GENERATE_PACKAGER + "."  + INSTANCE_PACKAGE_SUFFIX + "." + INSTANCE_FILE_NAME_START;


    public static final String MROUTER_CACHE_SP_KEY = "MROUTER_CACHE_SP_KEY";
    public static final String MROUTER_CACHE_SP_KEY_MAP = "MROUTER_CACHE_SP_KEY_MAP";
    public static final String MROUTER_CACHE_SP = "MROUTER_CACHE_SP";
    public static final String LAST_VERSION_NAME = "LAST_VERSION_NAME";
    public static final String LAST_VERSION_CODE = "LAST_VERSION_CODE";
}
