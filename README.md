#### 跳转路由器
```
仿ARouter的路由管理器。
```
#### 一. 功能介绍
* 支持多module间跳转；
* 支持包括Object在内的多种传参；
* 支持依赖注入，传参无需手动获取；
* 支持手动添加拦截器，拦截跳转；
* 支持获取Fragment；
* 支持切换动画；
* 支持扫描缓存，debug模式每次都会扫描任务注解，release模式根据App版本号对比选择是否使用扫描缓存。
#### 二. 各个库介绍
* [mRouter-api](https://github.com/xjz-111/MRouter/tree/master/mRouter_api)：Android Library 处理路由表及依赖注入等
* [mRouter-annotation](https://github.com/xjz-111/MRouter/tree/master/mRouter-annotation)：Java Library 定义注解Router及AutoValue等
* [mRouter-compiler](https://github.com/xjz-111/MRouter/tree/master/mRouter-compiler)：Java Library 实现AbstractProcessor，完成注解的扫描和每个module下路由收集代码处理及依赖注入相关扫描处理等
#### 三. 具体使用
* 使用；
```java
@Router(group = "app", path = "activity1")
public class Activity1 extends AppCompatActivity {}

@Router(path = "app/activity2")
public class Activity2 extends AppCompatActivity {}

@Router(path = "module1/fragment1")
public class Fragment1 extends Fragment {}

// 跳转
MRouter.getInstance().build("app/activity1")
                    .withString("key", "test")
                    .addInterceptor(new LoginActivityInterceptor())
                    .navigation();
                    
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
            .navigation(this, 123, params -> Log.i("MRouter", "跳转成功：" + params.getPath()));

// 获取Fragment
Fragment fragment = MRouter.getInstance().build("app/fragment1").navigation();

// 参数
@AutoValue(key = "key")
String name;

@AutoValue(key = "parcelable")
TestParcel testParcel;

@AutoValue(key = "serializable")
TestSerializable testSerializable;

@AutoValue(key = "obj")
Object obj;
```
* 注意：传参为未序列化的类型时，需实现ISerialization接口，使用Router注解。
```java
@Router(path = "app/MSerialization")
public class MSerialization implements ISerialization {
    private Gson gson = new Gson();

    @Override
    public String object2Json(Object instance) {
        return gson.toJson(instance);
    }

    @Override
    public <T> T json2Object(String s, Type clazz) {
        return gson.fromJson(s, clazz);
    }
}
```
* Library中自定义的Application.ActivityLifecycleCallbacks监听生命周期，在onActivityCreated调用了_MRouter.getInstance().inject(activity)，使用页面无需再手动注入。
* 关于拦截器，需自定义，实现MRouterInterceptor。跳转的地方手动addInterceptor，便可先跳转拦截器页面，返回后再去落地页。
```java
public class LoginActivityInterceptor extends MRouterInterceptor {

    public LoginActivityInterceptor() {
    }

    public LoginActivityInterceptor(Context context) {
        super(context);
    }

    @Override
    protected Params getRouter() {
        return MRouter.getInstance().build("app/login").withInt("key", 121).withTransition(R.anim.left_in, R.anim.right_out);
    }


    @Override
    protected int getRequestCode() {
        return -1;
    }

    @Override
    protected MRouterCallback getCallback() {
        return params -> {};
    }
}

```




