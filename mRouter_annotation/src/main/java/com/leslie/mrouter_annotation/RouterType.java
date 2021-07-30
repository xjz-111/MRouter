package com.leslie.mrouter_annotation;

/**
 * 作者：xjzhao
 * 时间：2021-07-13 16:50
 */
public enum RouterType {
    TYPE_ACTIVITY("Activity"),  // Activity
    TYPE_FRAGMENT("Fragment"),  // Fragment
    TYPE_JSON("Json"),      // Json处理
    TYPE_INSTANCE("Instance"); // 其他类型，可直接获取实例

    private String type;
    RouterType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
