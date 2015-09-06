package com.baidu.ub.msoa.container.support.governance.contact.proto;

import com.google.common.primitives.Primitives;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type;
import com.squareup.protoparser.DataType;
import com.squareup.protoparser.DataType.NamedType;
import com.squareup.protoparser.DataType.ScalarType;

/**
 * Created by pippo on 15/6/15.
 */
public enum ProtoType {

    FLOAT(Type.TYPE_FLOAT, ScalarType.FLOAT, Float.class),

    DOUBLE(Type.TYPE_DOUBLE, ScalarType.DOUBLE, Double.class),

    INT32(Type.TYPE_INT32, ScalarType.INT32, Integer.class),

    INT64(Type.TYPE_INT64, ScalarType.INT64, Long.class),

    UINT32(Type.TYPE_UINT32, ScalarType.UINT32, Integer.class),

    UINT64(Type.TYPE_UINT64, ScalarType.UINT64, Long.class),

    SINT32(Type.TYPE_SINT32, ScalarType.SINT32, Integer.class),

    SINT64(Type.TYPE_SFIXED64, ScalarType.SINT64, Long.class),

    FIXED32(Type.TYPE_FIXED32, ScalarType.FIXED32, Integer.class),

    FIXED64(Type.TYPE_FIXED64, ScalarType.FIXED64, Long.class),

    SFIXED32(Type.TYPE_SFIXED32, ScalarType.SFIXED32, Integer.class),

    SFIXED64(Type.TYPE_SFIXED64, ScalarType.SFIXED64, Long.class),

    BOOL(Type.TYPE_BOOL, ScalarType.BOOL, Boolean.class),

    STRING(Type.TYPE_STRING, ScalarType.STRING, String.class),

    BYTES(Type.TYPE_BYTES, ScalarType.BYTES, String.class);

    ProtoType(Type protoType, ScalarType type, Class<?> jvmType) {
        this.protoType = protoType;
        this.displayType = type;
        this.jvmType = jvmType;
    }

    public final Type protoType;
    public final ScalarType displayType;
    public final Class<?> jvmType;

    public static Class<?> getPrimitiveType(DataType displayType) {
        for (ProtoType protoType : ProtoType.values()) {
            if (protoType.displayType == displayType) {
                return protoType.jvmType;
            }
        }

        return null;
    }

    public static DataType getDisplayType(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            clazz = Primitives.wrap(clazz);
        }

        for (ProtoType protoType : ProtoType.values()) {
            if (protoType.jvmType == clazz) {
                return protoType.displayType;
            }
        }

        if (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }

        String name = clazz.getPackage().getName() + "." + clazz.getSimpleName();
        return NamedType.create(name);
    }

}
