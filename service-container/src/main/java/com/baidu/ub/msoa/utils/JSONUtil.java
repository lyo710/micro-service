package com.baidu.ub.msoa.utils;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by pippo on 15/8/5.
 */
public class JSONUtil {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        // OBJECT_MAPPER.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);
        // OBJECT_MAPPER.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        // OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
    }

    public static byte[] toBytes(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObject(byte[] result, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(result, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObject(InputStream result, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(result, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObject(byte[] result, Assembler<T> assembler) {
        try {
            return assembler.assemble(OBJECT_MAPPER.readTree(result));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public interface Assembler<T> {

        T assemble(JsonNode jsonNode);

    }
}
