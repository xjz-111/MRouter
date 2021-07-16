package com.leslie.test_module1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.leslie.mrouter_annotation.AutoValue;
import com.leslie.mrouter_annotation.Router;
import com.leslie.mrouter_api.utils.logger;

@Router(path = "module1/main")
public class MainActivity extends AppCompatActivity {

    @AutoValue(key = "obj")
    Object obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("module1/main");
        setContentView(textView);

        logger.info("app模块传递到module1模块的obj : " + obj);
    }
}
