package com.leslie.mrouter_compiler;

import com.google.auto.service.AutoService;
import com.leslie.mrouter_annotation.AutoValue;
import com.leslie.mrouter_annotation.IInject;
import com.leslie.mrouter_annotation.TypeKind;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * 跳转自动传参自动获取
 * <p>
 * 作者：xjzhao
 * 时间：2021-07-15 18:28
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8) //指定java版本
@SupportedAnnotationTypes({com.leslie.mrouter_compiler.Constant.ANNOTION_AUTO_VALUE})
public class AutoValueProcessor extends AbstractProcessor {
    private Map<TypeElement, List<Element>> parentAndChild = new HashMap<>();
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

    private TypeUtils typeUtils;

    private static final ClassName AndroidLog = ClassName.get("android.util", "Log");


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementTool = processingEnv.getElementUtils();
        typeTool = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        typeUtils = new TypeUtils(typeTool, processingEnv.getElementUtils());

        //参数是模块名 为了防止多模块/组件化开发的时候 生成相同的文件
        Map<String, String> options = processingEnv.getOptions();
        if (!options.isEmpty()) {
            moduleName = options.get(Constant.MROUTER_MODULE_NAME);
        }
        if (null == moduleName || "".equals(moduleName)) {
            throw new AssertionError(Constant.NO_MODULE_NAME_TIPS);
        }
        print("AutoValueProcessor::************* 开启处理AutoValue相关注解 *************");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) return false;
        try {
            categories(roundEnv.getElementsAnnotatedWith(AutoValue.class));
            parse();
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void parse() throws IllegalAccessException{
        TypeMirror activityType = elementTool.getTypeElement(Constant.ACTIVITY).asType();
        TypeMirror fragmentType = elementTool.getTypeElement(Constant.FRAGMENT).asType();
        TypeMirror fragmentV4Type = elementTool.getTypeElement(Constant.FRAGMENT_V4).asType();
        TypeElement jsonType = elementTool.getTypeElement(Constant.JSON_TYPE);

        boolean isActivity = false;
        for (Map.Entry<TypeElement, List<Element>> entry : parentAndChild.entrySet()) {

            TypeElement parent = entry.getKey();
            List<Element> children = entry.getValue();

            String qualifiedName = parent.getQualifiedName().toString();
            // 必须使用相同包名，否则智能将变量申明为public的
            String packageName = qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
            ClassName clsName = ClassName.get(parent);

            // 方法
            MethodSpec.Builder injectMethodBuilder = MethodSpec.methodBuilder(Constant.AUTOVALUE_METHOD_NAME)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(TypeName.OBJECT, "target").build());


            injectMethodBuilder.addStatement("ISerialization serializationUtils = $T.getInstance().build().navigationInstance($T.class)", ClassName.get(Constant.API_PACKAGE, "MRouter"), ClassName.get(jsonType));

            injectMethodBuilder.addStatement("$T instance = ($T)target", clsName, clsName);

            TypeMirror currentType = parent.asType();
            for (Element element : children) {
                AutoValue fieldConfig = element.getAnnotation(AutoValue.class);
                String fieldName = element.getSimpleName().toString();
                isActivity = typeTool.isSubtype(currentType, activityType);
                String statement = "instance." + fieldName + " = " + buildCastCode(element) + "instance.";
                if (typeTool.isSubtype(currentType, activityType)) {
                    isActivity = true;
                    statement += "getIntent().";
                } else if (typeTool.isSubtype(parent.asType(), fragmentType) || typeTool.isSubtype(parent.asType(), fragmentV4Type)) {
                    statement += "getArguments().";
                } else {
                    throw new IllegalAccessException("The field [" + fieldName + "] need autoValue from intent, its parent must be activity or fragment!");
                }
                statement = buildStatement("instance." + fieldName, statement, typeUtils.typeExchange(element), isActivity);
                if (statement.startsWith("serializationUtils.")) {
                    injectMethodBuilder.beginControlFlow("if (null != serializationUtils)");
                    injectMethodBuilder.addStatement(
                            "instance." + fieldName + " = " + statement,
                            (typeUtils.isEmpty(fieldConfig.key()) ? fieldName : fieldConfig.key()),
                            ClassName.get(element.asType())
                    );
                    injectMethodBuilder.nextControlFlow("else");
                    injectMethodBuilder.addStatement(
                            "$T.e(\"" + com.leslie.mrouter_annotation.Constant.TAG + "\", \"You want automatic inject the field '" + fieldName + "' in class '$T' , then you should implement 'ISerialization' to support object auto inject!\")", AndroidLog, ClassName.get(parent));
                    injectMethodBuilder.endControlFlow();
                } else {
                    injectMethodBuilder.addStatement(statement, typeUtils.isEmpty(fieldConfig.key()) ? fieldName : fieldConfig.key());
                }

            }

            // 类
            TypeSpec typeSpec = TypeSpec.classBuilder(parent.getSimpleName() + com.leslie.mrouter_annotation.Constant.FILE_NAME_AUTOVALUE)
                    .addSuperinterface(IInject.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(injectMethodBuilder.build())
                    .build();


            try {
                JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
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
    }


    private void categories(Set<? extends Element> elements) throws IllegalAccessException {
        for (Element element : elements) {
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

            if (element.getModifiers().contains(Modifier.PRIVATE)) {
                throw new IllegalAccessException("The inject fields CAN NOT BE 'private'!!! please check field ["
                        + element.getSimpleName() + "] in class [" + enclosingElement.getQualifiedName() + "]");
            }

            if (parentAndChild.containsKey(enclosingElement)) {
                parentAndChild.get(enclosingElement).add(element);
            } else {
                List<Element> children = new ArrayList<>();
                children.add(element);
                parentAndChild.put(enclosingElement, children);
            }
        }
    }


    private String buildStatement(String originalValue, String statement, int type, boolean isActivity) {
        switch (TypeKind.values()[type]) {
            case BOOLEAN:
                statement += "getBoolean" + (isActivity ? "Extra" : "") + "($S, " + originalValue + ")";
                break;
            case BYTE:
                statement += "getByte" + (isActivity ? "Extra" : "") + "($S, " + originalValue + ")";
                break;
            case SHORT:
                statement += "getShort" + (isActivity ? "Extra" : "") + "($S, " + originalValue + ")";
                break;
            case INT:
                statement += "getInt" + (isActivity ? "Extra" : "") + "($S, " + originalValue + ")";
                break;
            case LONG:
                statement += "getLong" + (isActivity ? "Extra" : "") + "($S, " + originalValue + ")";
                break;
            case CHAR:
                statement += "getChar" + (isActivity ? "Extra" : "") + "($S, " + originalValue + ")";
                break;
            case FLOAT:
                statement += "getFloat" + (isActivity ? "Extra" : "") + "($S, " + originalValue + ")";
                break;
            case DOUBLE:
                statement += "getDouble" + (isActivity ? "Extra" : "") + "($S, " + originalValue + ")";
                break;
            case STRING:
                statement += (isActivity ? ("getExtras() == null ? " + originalValue + " : instance.getIntent().getExtras().getString($S") : ("getString($S")) + ", " + originalValue + ")";
                break;
            case SERIALIZABLE:
                statement += (isActivity ? ("getSerializableExtra($S)") : ("getSerializable($S)"));
                break;
            case PARCELABLE:
                statement += (isActivity ? ("getParcelableExtra($S)") : ("getParcelable($S)"));
                break;
            case OBJECT:
                statement = "serializationUtils.json2Object(instance." + (isActivity ? "getIntent()." : "getArguments().") + (isActivity ? "getStringExtra($S)" : "getString($S)") + ", new " + Constant.TYPE_WRAPPER + "<$T>(){}.getType())";
                break;
        }

        return statement;
    }

    private String buildCastCode(Element element) {
        if (typeUtils.typeExchange(element) == TypeKind.SERIALIZABLE.ordinal()) {
            return CodeBlock.builder().add("($T) ", ClassName.get(element.asType())).build().toString();
        }
        return "";
    }


    private void print(String s){
        messager.printMessage(Diagnostic.Kind.NOTE, s);
    }
}
