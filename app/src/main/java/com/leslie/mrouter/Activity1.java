package com.leslie.mrouter;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.leslie.mrouter_annotation.AutoValue;
import com.leslie.mrouter_annotation.Router;
import com.leslie.mrouter_api.MRouter;

/**
 * 作者：xjzhao
 * 时间：2021-07-15 11:46
 */
@Router(group = "app", path = "activity1")
public class Activity1 extends AppCompatActivity {

    @AutoValue(key = "key")
    String name;

    @AutoValue(key = "xyz")
    String xyz;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText(this.getClass().getSimpleName());
        setContentView(textView);
        MRouter.getInstance().inject(this);
        Log.i("xjzhao", "name : " + name);
    }
}
