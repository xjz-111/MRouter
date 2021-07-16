package com.leslie.mrouter;

import java.io.Serializable;

/**
 * 作者：xjzhao
 * 时间：2021-07-15 23:49
 */
public class TestSerializable implements Serializable {
    private int y;
    private String m;

    public TestSerializable(int y, String m) {
        this.y = y;
        this.m = m;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }
}
