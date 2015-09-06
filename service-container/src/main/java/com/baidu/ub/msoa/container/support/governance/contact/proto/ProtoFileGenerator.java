package com.baidu.ub.msoa.container.support.governance.contact.proto;

import com.baidu.ub.msoa.container.support.governance.contact.proto.annotaion.Optional;
import com.baidu.ub.msoa.container.support.governance.contact.proto.annotaion.Tag;
import com.squareup.protoparser.DataType;
import com.squareup.protoparser.EnumConstantElement;
import com.squareup.protoparser.EnumElement;
import com.squareup.protoparser.FieldElement;
import com.squareup.protoparser.FieldElement.Label;
import com.squareup.protoparser.MessageElement;
import com.squareup.protoparser.ProtoFile;
import com.squareup.protoparser.ProtoFile.Builder;
import com.squareup.protoparser.TypeElement;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pippo on 15/6/24.
 */
public class ProtoFileGenerator extends ProtoBase {

    public ProtoFileGenerator(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    protected Class<?> targetClass;
    protected Builder builder;
    protected Map<String, TypeElement> existNameTypes = new HashMap<>();

    public ProtoFile generate() {
        createBuilder();
        create(targetClass);
        return builder.build();
    }

    protected Builder createBuilder() {
        builder = ProtoFile.builder(targetClass.getSimpleName());
        return builder;
    }

    protected TypeElement create(Class<?> clazz) {

        if (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }

        if (!isCustomType(clazz)) {
            return null;
        }

        String fullName = getTypeName(clazz).toString();

        // todo 父子关系的对象生成会导致堆栈溢出,需要特殊的判断逻辑
        if (existNameTypes.containsKey(fullName)) {
            return existNameTypes.get(fullName);
        }

        TypeElement element = clazz.isEnum() ? createEnumType(clazz, fullName) : createMessageType(clazz, fullName);

        existNameTypes.put(fullName, element);
        return element;
    }

    protected DataType getTypeName(Class<?> clazz) {
        return ProtoType.getDisplayType(clazz);
    }

    protected EnumElement createEnumType(Class<?> clazz, String name) {
        try {
            Method nameM = clazz.getMethod("name");
            Method ordinalM = clazz.getMethod("ordinal");

            EnumElement.Builder elementBuilder = EnumElement.builder().name(name);
            Object[] values = clazz.getEnumConstants();
            for (Object value : values) {
                elementBuilder.addConstant(EnumConstantElement.builder()
                        .name((String) nameM.invoke(value))
                        .tag(((Integer) ordinalM.invoke(value)) + 1)
                        .build());
            }

            EnumElement element = elementBuilder.build();
            builder.addType(element);
            return element;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected TypeElement createMessageType(Class<?> clazz, String name) {
        MessageElement.Builder elementBuilder = MessageElement.builder().name(name);
        Field[] fields = FieldUtils.getAllFields(clazz);
        //        Field[] fields = clazz.getDeclaredFields();

        int tagIndex = 0;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Class<?> fieldClazz = field.getType();

            if (Modifier.isNative(field.getModifiers())
                    || Modifier.isStatic(field.getModifiers())
                    || Modifier.isVolatile(field.getModifiers())
                    || Modifier.isTransient(field.getModifiers())) {
                continue;
            }

            /* 判断自定义类型 */
            if (isCustomType(fieldClazz)) {
                create(fieldClazz);
            }

            if (createCollectionFieldElement(elementBuilder, field, tagIndex)) {
                tagIndex++;
            } else if (createFieldElement(elementBuilder, field, tagIndex)) {
                tagIndex++;
            }
        }

        TypeElement element = elementBuilder.build();
        builder.addType(element);
        return element;
    }

    protected boolean createFieldElement(MessageElement.Builder elementBuilder, Field field, int tagIndex) {
        if (field.getType().isArray() || Collection.class.isAssignableFrom(field.getType())) {
            return false;
        }

        Optional optional = field.getAnnotation(Optional.class);
        Tag tag = field.getAnnotation(Tag.class);

        FieldElement fieldElement = FieldElement.builder()
                .name(field.getName())
                .label(optional != null ? Label.OPTIONAL : Label.REQUIRED)
                .tag((tag != null && tag.value() > tagIndex) ? tag.value() : tagIndex + 1)
                .type(getTypeName(field.getType()))
                .build();
        elementBuilder.addField(fieldElement);

        return true;
    }

    protected boolean createCollectionFieldElement(MessageElement.Builder elementBuilder, Field field, int index) {
        Class<?> type = field.getType();

        if (!(type.isArray() || Collection.class.isAssignableFrom(type))) {
            return false;
        }

        Class<?> elementType;
        elementType = type.isArray() ? type.getComponentType() : getGenericType(field, 0);

        create(elementType);
        FieldElement fieldElement = FieldElement.builder()
                .name(field.getName())
                .label(Label.REPEATED)
                .tag(index + 1)
                .type(getTypeName(elementType))
                .build();
        elementBuilder.addField(fieldElement);
        return true;
    }

    protected Class<?> getGenericType(Field field, int index) {
        return getGenericType(field.getGenericType(), index);
    }

    protected Class<?> getGenericType(Class<?> clazz, int index) {
        return getGenericType(clazz.getGenericSuperclass(), index);
    }

    protected Class<?> getGenericType(Type type, int index) {
        try {
            type = ((ParameterizedType) type).getActualTypeArguments()[index];
            if (type instanceof GenericArrayType) {
                int dimensions = 1;
                Type componentType = ((GenericArrayType) type).getGenericComponentType();
                while (componentType instanceof GenericArrayType) {
                    dimensions++;
                    componentType = ((GenericArrayType) componentType).getGenericComponentType();
                }

                // TODO is there a more efficient way (reflection) to obtain an
                // array class?

                if (dimensions == 1) {
                    return Array.newInstance((Class<?>) componentType, 0).getClass();
                }

                final int[] arg = new int[dimensions];
                arg[0] = 0;
                return Array.newInstance((Class<?>) componentType, arg).getClass();
            }

            if (type instanceof ParameterizedType) {
                // TODO in the future, we can opt to do recursive type
                // inspection which can avoid including type metadata even with
                // very complex nested generic types (E.g a
                // List<List<List<String>>>).

                // special handling when generic type is either Class<?> or
                // Enum<?>
                Object rawType = ((ParameterizedType) type).getRawType();
                if (Class.class == rawType) {
                    return Class.class;
                }

                if (Enum.class == rawType) {
                    return Enum.class;
                }

                return null;
            }

            return (Class<?>) type;
        } catch (Exception e) {
            return null;
        }
    }

}
