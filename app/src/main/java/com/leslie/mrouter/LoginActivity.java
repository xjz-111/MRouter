package com.leslie.mrouter;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.leslie.mrouter_annotation.Router;

/**
 * 作者：xjzhao
 * 时间：2021-07-15 17:25
 */
@Router(path = "app/login")
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText(this.getClass().getSimpleName());
        setContentView(textView);
    }
}
