package com.leslie.mrouter_compiler;

/**
 * 作者：xjzhao
 * 时间：2021-07-16 00:03
 */
class Constant {

    static final String MROUTER_MODULE_NAME = "MROUTER_MODULE_NAME";

    private static final String ANNOTION_PACKAGE = "com.leslie.mrouter_annotation";

    static final String API_PACKAGE = "com.leslie.mrouter_api";

    static final String ANNOTION_ROUTER = ANNOTION_PACKAGE + ".Router";

    static final String ANNOTION_AUTO_VALUE = ANNOTION_PACKAGE + ".AutoValue";

    static final String MROUTER_METHOD_NAME = "load";

    static final String AUTOVALUE_METHOD_NAME = "inject";

    static final String TYPE_WRAPPER = API_PACKAGE + ".TypeWrapper";

    static final String JSON_KEY = API_PACKAGE + ".ISerialization";

    static final String NO_MODULE_NAME_TIPS = "These no module name, at 'build.gradle', like :\n" +
            "android {\n" +
            "    defaultConfig {\n" +
            "        ...\n" +
            "        javaCompileOptions {\n" +
            "            annotationProcessorOptions {\n" +
            "                arguments = [MROUTER_MODULE_NAME: project.getName()]\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}\n";

    static final String ACTIVITY = "android.app.Activity";
    static final String FRAGMENT = "android.app.Fragment";
    static final String FRAGMENT_V4 = "android.support.v4.app.Fragment";
    static final String JSON_TYPE = API_PACKAGE + ".ISerialization";


    // Java type
    private static final String LANG = "java.lang";
    static final String BYTE = LANG + ".Byte";
    static final String SHORT = LANG + ".Short";
    static final String INTEGER = LANG + ".Integer";
    static final String LONG = LANG + ".Long";
    static final String FLOAT = LANG + ".Float";
    static final String DOUBEL = LANG + ".Double";
    static final String BOOLEAN = LANG + ".Boolean";
    static final String CHAR = LANG + ".Character";
    static final String STRING = LANG + ".String";
    static final String SERIALIZABLE = "java.io.Serializable";
    static final String PARCELABLE = "android.os.Parcelable";
}
