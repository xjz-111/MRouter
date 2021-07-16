package com.leslie.mrouter_compiler;

import com.leslie.mrouter_annotation.TypeKind;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.leslie.mrouter_compiler.Constant.BOOLEAN;
import static com.leslie.mrouter_compiler.Constant.BYTE;
import static com.leslie.mrouter_compiler.Constant.CHAR;
import static com.leslie.mrouter_compiler.Constant.DOUBEL;
import static com.leslie.mrouter_compiler.Constant.FLOAT;
import static com.leslie.mrouter_compiler.Constant.INTEGER;
import static com.leslie.mrouter_compiler.Constant.LONG;
import static com.leslie.mrouter_compiler.Constant.PARCELABLE;
import static com.leslie.mrouter_compiler.Constant.SERIALIZABLE;
import static com.leslie.mrouter_compiler.Constant.SHORT;
import static com.leslie.mrouter_compiler.Constant.STRING;

/**
 * 作者：xjzhao
 * 时间：2021-07-16 00:01
 */
 class TypeUtils {
    private Types types;
    private TypeMirror parcelableType;
    private TypeMirror serializableType;

     TypeUtils(Types types, Elements elements) {
        this.types = types;

        parcelableType = elements.getTypeElement(PARCELABLE).asType();
        serializableType = elements.getTypeElement(SERIALIZABLE).asType();
    }

    /**
     * Diagnostics out the true java type
     *
     * @param element Raw type
     * @return Type class of java
     */
     int typeExchange(Element element) {
        TypeMirror typeMirror = element.asType();

        // Primitive
        if (typeMirror.getKind().isPrimitive()) {
            return element.asType().getKind().ordinal();
        }

        switch (typeMirror.toString()) {
            case BYTE:
                return TypeKind.BYTE.ordinal();
            case SHORT:
                return TypeKind.SHORT.ordinal();
            case INTEGER:
                return TypeKind.INT.ordinal();
            case LONG:
                return TypeKind.LONG.ordinal();
            case FLOAT:
                return TypeKind.FLOAT.ordinal();
            case DOUBEL:
                return TypeKind.DOUBLE.ordinal();
            case BOOLEAN:
                return TypeKind.BOOLEAN.ordinal();
            case CHAR:
                return TypeKind.CHAR.ordinal();
            case STRING:
                return TypeKind.STRING.ordinal();
            default:
                // Other side, maybe the PARCELABLE or SERIALIZABLE or OBJECT.
                if (types.isSubtype(typeMirror, parcelableType)) {
                    // PARCELABLE
                    return TypeKind.PARCELABLE.ordinal();
                } else if (types.isSubtype(typeMirror, serializableType)) {
                    // SERIALIZABLE
                    return TypeKind.SERIALIZABLE.ordinal();
                } else {
                    return TypeKind.OBJECT.ordinal();
                }
        }
    }

     boolean isEmpty(String s){
        return null == s || "".equals(s);
    }
}
