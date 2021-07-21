package com.leslie.mrouter.test;

import androidx.appcompat.app.AppCompatActivity;

import com.leslie.mrouter_annotation.AutoValue;

/**
 * 作者：xjzhao
 * 时间：2021-07-21 11:41
 */
public class ActivityTest extends AppCompatActivity {

    @AutoValue(key = "key")
    String name;

}
