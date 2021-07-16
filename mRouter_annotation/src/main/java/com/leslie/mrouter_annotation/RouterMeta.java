package com.leslie.mrouter_annotation;


/**
 * 作者：xjzhao
 * 时间：2021-07-13 16:50
 */
public class RouterMeta {
    private RouterType type;
    private String path;
    private String group;
    private Class<?> destination;

    public static RouterMeta build(String group, String path, RouterType type, Class<?> destination){
        return new RouterMeta(group, path, type, destination);
    }

    public RouterMeta() {
    }

    public RouterMeta(String group, String path, RouterType type, Class<?> destination) {
        this.group = group;
        this.path = path;
        this.type = type;
        this.destination = destination;
    }

    public RouterType getType() {
        return type;
    }

    public void setType(RouterType type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Class<?> getDestination() {
        return destination;
    }

    public void setDestination(Class<?> destination) {
        this.destination = destination;
    }
}
