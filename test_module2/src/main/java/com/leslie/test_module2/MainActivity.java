package com.leslie.test_module2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.leslie.mrouter_annotation.AutoValue;
import com.leslie.mrouter_annotation.Router;

@Router(path = "module2/main")
public class MainActivity extends AppCompatActivity {

    @AutoValue(key = "key")
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("module2/main");
        setContentView(textView);
    }
}
