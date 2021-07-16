package com.leslie.mrouter;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.leslie.mrouter_annotation.AutoValue;
import com.leslie.mrouter_annotation.Router;
import com.leslie.mrouter_api.utils.logger;

import java.util.ArrayList;

/**
 * 作者：xjzhao
 * 时间：2021-07-15 12:43
 */
@Router(path = "app/activity2")
public class Activity2 extends AppCompatActivity {

    @AutoValue(key = "key")
    String name;

    @AutoValue
    Bundle bundle;

    @AutoValue(key = "int")
    int value;

    @AutoValue(key = "action")
    String action;

    @AutoValue(key = "boolean")
    boolean bool;

    @AutoValue(key = "byte")
    byte mByte;

    @AutoValue(key = "char")
    char mchar;

    @AutoValue(key = "bFloat")
    float mFloat;

    @AutoValue(key = "double")
    double mDouble;

    @AutoValue(key = "iList")
    ArrayList<Integer> iList;

    @AutoValue(key = "charArray")
    char[] chArray;

    @AutoValue(key = "parcelable")
    TestParcel testParcel;

    @AutoValue(key = "serializable")
    TestSerializable testSerializable;

    @AutoValue(key = "obj")
    Object obj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText(this.getClass().getSimpleName());
        setContentView(textView);

        logger.info("value: " + value);

        logger.info("action: " + action);

        logger.info("obj: " + obj);
    }
}
