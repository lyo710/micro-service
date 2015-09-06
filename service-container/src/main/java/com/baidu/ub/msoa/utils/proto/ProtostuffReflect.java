package com.baidu.ub.msoa.utils.proto;

import com.google.common.primitives.Primitives;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pippo on 15/6/24.
 */
public class ProtostuffReflect {

    private static byte[] empty = new byte[0];
    private static Map<String, Schema<?>> cache = new HashMap<>();

    /**
     * encode object to byte[]
     *
     * @param target
     * @return byte[]
     */
    @SuppressWarnings("unchecked")
    public static byte[] encode(Object target) {
        if (target == null) {
            return empty;
        }

        Schema schema;
        List list = null;
        if (target instanceof Collection) {
            list = collection2List((Collection) target);
            if (list.isEmpty()) {
                return empty;
            } else {
                schema = getSchema(list.get(0).getClass());
            }
        } else if (target.getClass().isArray()) {
            schema = getSchema(target.getClass().getComponentType());
            list = array2List((Object[]) target);
        } else {
            schema = getSchema(target.getClass());
        }

        try {
            byte[] result;

            if (list != null) {
                result = list2ByteArray(schema, list);
            } else {
                result = ProtobufIOUtil.toByteArray(target, schema, LinkedBuffer.allocate(512));
            }

            return result;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static List collection2List(Collection collection) {
        List ll = new ArrayList();
        for (Object o : collection) {
            if (o != null) {
                ll.add(o);
            }
        }
        return ll;
    }

    private static List array2List(Object[] array) {
        List ll = new ArrayList();
        for (Object o : array) {
            if (o != null) {
                ll.add(o);
            }
        }
        return ll;
    }

    private static byte[] list2ByteArray(Schema schema, List list) throws java.io.IOException {
        byte[] result;
        ByteArrayOutputStream out = new ByteArrayOutputStream(512);
        ProtobufIOUtil.writeListTo(out, list, schema, LinkedBuffer.allocate(512));
        result = out.toByteArray();
        return result;
    }

    /**
     * decode byte[] to object
     *
     * @param data
     * @param clazz
     * @return result
     */
    @SuppressWarnings("unchecked")
    public static <T> T decode(byte[] data, Class<T> clazz) {
        if (data == null || data.length == 0) {
            return null;
        }

        if (clazz.isPrimitive()) {
            clazz = Primitives.wrap(clazz);
        }

        try {
            Schema schema = getSchema(clazz.isArray() ? clazz.getComponentType() : clazz);

            T instance;

            if (clazz.isArray()) {
                List l = ProtobufIOUtil.parseListFrom(new ByteArrayInputStream(data), schema);
                instance = (T) l.toArray((Object[]) Array.newInstance(clazz.getComponentType(), 0));
            } else {
                instance = (T) schema.newMessage();
                ProtobufIOUtil.mergeFrom(data, instance, schema);
            }

            return instance;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T transform(Class<T> expectType, Object origin) {
        return decode(encode(origin), expectType);
    }

    //    @SuppressWarnings("unchecked")
    //    private static <T> T instance(Class<T> clazz) throws Exception {
    //
    //
    //
    //        if (clazz == Short.class) {
    //            return (T) Short.valueOf((short) -1);
    //        } else if (clazz == Integer.class) {
    //            return (T) Integer.valueOf(-1);
    //        } else if (clazz == Long.class) {
    //            return (T) Long.valueOf(-1);
    //        } else if (clazz == Float.class) {
    //            return (T) Float.valueOf(-1);
    //        } else if (clazz == Double.class) {
    //            return (T) Double.valueOf(-1);
    //        } else if (clazz == Boolean.class) {
    //            return (T) Boolean.FALSE;
    //        } else {
    //            return clazz.newInstance();
    //        }
    //    }

    private static <T> Schema<T> getSchema(Class<T> clazz) {
        Schema schema = cache.get(clazz.getName());
        if (schema == null) {
            schema = RuntimeSchema.createFrom(clazz);
            cache.put(clazz.getName(), schema);
        }
        return schema;
    }

}
