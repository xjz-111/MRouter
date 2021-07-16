package com.leslie.mrouter_compiler;

import com.google.auto.service.AutoService;
import com.leslie.mrouter_annotation.IJson;
import com.leslie.mrouter_annotation.IRouter;
import com.leslie.mrouter_annotation.Router;
import com.leslie.mrouter_annotation.RouterMeta;
import com.leslie.mrouter_annotation.RouterType;
import com.leslie.mrouter_annotation.Utils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * 扫描路由注解，并生成相应代码
 * <p>
 * 作者：xjzhao
 * 时间：2021-07-13 15:49
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8) //指定java版本
@SupportedAnnotationTypes({Constant.ANNOTION_ROUTER})
public class RouterProcessor extends AbstractProcessor {

    // 操作Element的工具类（类，函数，属性，其实都是Element）
    private Elements elementTool;

    // type(类信息)的工具类，包含用于操作TypeMirror的工具方法
    private Types typeTool;

    // Message用来打印 日志相关信息
    private Messager messager;

    // 文件生成器， 类 资源 等，就是最终要生成的文件 是需要Filer来完成的
    private Filer filer;

    // module名字
    private String moduleName;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementTool = processingEnv.getElementUtils();
        typeTool = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();

        //参数是模块名 为了防止多模块/组件化开发的时候 生成相同的文件
        Map<String, String> options = processingEnv.getOptions();
        if (!options.isEmpty()) {
            moduleName = options.get(Constant.MROUTER_MODULE_NAME);
        }
        if (null == moduleName || "".equals(moduleName)) {
            throw new AssertionError(Constant.NO_MODULE_NAME_TIPS);
        }
        print("RouterProcessor::************* 开启处理Router相关注解 *************");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) return false;
        parse(roundEnv.getElementsAnnotatedWith(Router.class));
        return true;
    }

    private void parse(Set<? extends Element> elements) {
        TypeMirror activityType = elementTool.getTypeElement(Constant.ACTIVITY).asType();
        TypeMirror fragmentType = elementTool.getTypeElement(Constant.FRAGMENT).asType();
        TypeMirror fragmentV4Type = elementTool.getTypeElement(Constant.FRAGMENT_V4).asType();
        TypeMirror jsonType = elementTool.getTypeElement(Constant.JSON_TYPE).asType();

        MethodSpec.Builder routerMb = getMethodSpecBuilder();

        MethodSpec.Builder jsonMb = getMethodSpecBuilder();


        RouterType type;
        for (Element element : elements) {
            String packageName = elementTool.getPackageOf(element).getQualifiedName().toString();

            // 获取简单类名
            String className = element.getSimpleName().toString();

            Router router = element.getAnnotation(Router.class);
            String group = router.group();
            String path = router.path();
            String[] result = Utils.getPath(group, path);
            group = result[0];
            path = result[1];

            TypeMirror currentType = element.asType();
            boolean isJson = false;
            if (typeTool.isSubtype(currentType, activityType)) {
                type = RouterType.TYPE_ACTIVITY;
            } else if (typeTool.isSubtype(currentType, fragmentType) || typeTool.isSubtype(currentType, fragmentV4Type)) {
                type = RouterType.TYPE_FTAGMENT;
            } else if (typeTool.isSubtype(currentType, jsonType)) {
                type = RouterType.TYPE_JSON;
                isJson = true;
            } else {
                throw new AssertionError("仅支持Activity，Fragment和IProvider ！！！");
            }

            print("被@Router注解的类有：" + packageName + "." + className);

            // list中添加元素
            if (isJson) {
                addStatement(jsonMb, "map.put(\"" + Constant.JSON_KEY + "\", $T.build(\"" + group + "\",\"" + path + "\"," + "$T." + type + ", $T.class" + "))", element);
            }else {
                addStatement(routerMb, "map.put(\"" + path + "\", $T.build(\"" + group + "\",\"" + path + "\"," + "$T." + type + ", $T.class" + "))", element);
            }


        }

        // _MRouter$$Router$${moduleName}.class
        generate(com.leslie.mrouter_annotation.Constant.FILE_NAME_START + moduleName, IRouter.class, routerMb.build());

        // _MRouter$$Json$$$${moduleName}.class
        generate(com.leslie.mrouter_annotation.Constant.JSON_FILE_NAME_START + moduleName, IJson.class, jsonMb.build());

    }

    private MethodSpec.Builder getMethodSpecBuilder(){
        // protected final void load(){}
        ParameterizedTypeName mapTypeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouterMeta.class)
        );
        return MethodSpec.methodBuilder(Constant.MROUTER_METHOD_NAME)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(ParameterSpec.builder(mapTypeName, "map").build());
//                .returns(returnList)
//                .addStatement("List<RouterMeta> list = new java.util.ArrayList<RouterMeta>()");
    }

    private void addStatement(MethodSpec.Builder builder, String statement, Element element){
        builder.addStatement(
                statement,
                RouterMeta.class,
                RouterType.class,
                ClassName.get((TypeElement) element));
    }

    private void generate(String fileName, Class<?> superCls, MethodSpec methodSpec){
        TypeSpec _MRouter$$Router = TypeSpec.classBuilder(fileName)
                .addSuperinterface(superCls)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodSpec)
                .build();

        try {
            JavaFile javaFile = JavaFile.builder(com.leslie.mrouter_annotation.Constant.PACKAGE, _MRouter$$Router)
                    .addFileComment(" This codes are generated automatically. Do not modify!")
                    .build();
            // write to file
            javaFile.writeTo(filer);

            messager.printMessage(Diagnostic.Kind.NOTE, "MRouter::代码已生成！");

        } catch (IOException e) {
            e.printStackTrace();
            messager.printMessage(Diagnostic.Kind.NOTE, "MRouter::生成代码失败");

        }
    }

    private void print(String s) {
        messager.printMessage(Diagnostic.Kind.NOTE, s);
    }
}
