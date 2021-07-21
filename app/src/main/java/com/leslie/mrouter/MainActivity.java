package com.leslie.mrouter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.leslie.mrouter.test.TestBean;
import com.leslie.mrouter_annotation.Router;
import com.leslie.mrouter_api.MRouter;
import com.leslie.mrouter_api.utils.logger;

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


        while (true){
            if (MRouter.getInstance().isInitSuccess()) {
                Fragment testFragment = MRouter.getInstance().build("app/fragment1").navigation();
                TestBean testBean = MRouter.getInstance().build("app/testBean").navigation();

                Fragment testFragment1 = MRouter.getInstance().build().navigation(Fragment1.class);
                TestBean testBean1 = MRouter.getInstance().build().navigation(TestBean.class);

                logger.info("testFragment : " + testFragment + "  testBean : " + testBean);
                logger.info("testFragment1 : " + testFragment1 + "  testBean1 : " + testBean1);
                break;
            }
        }
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
