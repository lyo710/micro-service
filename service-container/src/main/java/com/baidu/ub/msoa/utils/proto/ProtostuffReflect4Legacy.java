package com.baidu.ub.msoa.utils.proto;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pippo on 15/6/24.
 */
public class ProtostuffReflect4Legacy {

    /**
     * cached schemas
     */
    private static ConcurrentHashMap<String, Schema> cache = new ConcurrentHashMap<>();

    /**
     * init runtime properties
     */
    static {
        System.setProperty("protostuff.runtime.collection_schema_on_repeated_fields", "true");
        System.setProperty("protostuff.runtime.morph_collection_interfaces", "true");
        System.setProperty("protostuff.runtime.morph_map_interfaces", "true");
    }

    public static <T> T decode(Class<T> clazz, byte[] bytes) {
        try {
            Schema schema = getSchema(clazz);
            T instance = clazz.newInstance();
            ProtobufIOUtil.mergeFrom(bytes, instance, schema);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> byte[] encode(T object) {
        LinkedBuffer buffer = LinkedBuffer.allocate(512);
        Schema schema = getSchema(object.getClass());
        return ProtobufIOUtil.toByteArray(object, schema, buffer);
    }

    private static Schema getSchema(Class<?> clazz) {
        Schema schema = cache.get(clazz.getName());
        if (schema == null) {
            schema = RuntimeSchema.getSchema(clazz);
            cache.putIfAbsent(clazz.getName(), schema);
        }
        return schema;
    }

}
