package com.leslie.mrouter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.leslie.mrouter_annotation.Router;
import com.leslie.mrouter_api.MRouter;

import java.util.ArrayList;

@Router(path = "app/main")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn1){
            MRouter.getInstance().build("app/activity1")
                    .withString("key", "test")
                    .addInterceptor(new LoginActivityInterceptor())
                    .navigation();
        }else if (id == R.id.btn2){
            Bundle bundle = new Bundle();
            bundle.putFloat("bFloat", 18F);
            ArrayList<Integer> iList = new ArrayList<>();
            iList.add(100);


            MRouter.getInstance().build("app/activity2")
                    .withString("key", "test")
                    .withInt("int", 233)
                    .withAction("test/action")
                    .withBoolean("boolean", true)
                    .withByte("byte", Byte.parseByte("12"))
                    .withChar("char", (char) 65)
                    .withDouble("double", 349d)
                    .withIntegerArrayList("iList", iList)
                    .withCharArray("charArray", "cs".toCharArray())
                    .withParcelable("parcelable", new TestParcel(21, "hhhhhhh"))
                    .withSerializable("serializable", new TestSerializable(77, "bncbznmcb"))
                    .withObject("obj", new SimpleBean())
                    .with(bundle)
                    .navigation(this, 123, params -> Log.i("xjzhao", "跳转成功：" + params.getPath()));
        } else if (id == R.id.btn3){
            MRouter.getInstance().build("module1/main")
                    .withObject("obj", new SimpleBean())
                    .navigation();
        } else if (id == R.id.btn4){
            MRouter.getInstance().build("module2/main")
                    .withString("key", "test")
                    .addInterceptor(new LoginActivityInterceptor())
                    .navigation();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}