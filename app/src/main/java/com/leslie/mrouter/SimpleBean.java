package com.leslie.mrouter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xjzhao
 * 时间：2021-07-16 08:53
 */
public class SimpleBean {
    private String name;
    private List<Integer> list;

    public SimpleBean() {
        this.name = "test_simple_bean";
        this.list = new ArrayList<>();
        list.add(123);
        list.add(456);
    }
}
