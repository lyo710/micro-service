package com.baidu.ub.msoa.container.support.governance.contact.proto;

import com.baidu.ub.msoa.container.support.governance.contact.proto.annotaion.CollectionElementType;
import com.baidu.ub.msoa.container.support.governance.discover.annotation.BundleService;
import com.baidu.ub.msoa.utils.AnnotationUtil;
import com.baidu.ub.msoa.utils.AnnotationUtil.AnnotationBindInfo;
import com.google.common.primitives.Primitives;
import com.squareup.protoparser.DataType;
import com.squareup.protoparser.DataType.NamedType;
import com.squareup.protoparser.OptionElement;
import com.squareup.protoparser.OptionElement.Kind;
import com.squareup.protoparser.ProtoFile;
import com.squareup.protoparser.TypeElement;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

/**
 * Created by pippo on 15/8/6.
 */
public class ServiceStubFileGenerator extends ProtoFileGenerator {

    public ServiceStubFileGenerator(int provider, Class<?> targetClass) {
        super(targetClass);
        this.provider = provider;
    }

    protected int provider;

    @Override
    public ProtoFile generate() {
        AnnotationBindInfo<BundleService> meta = AnnotationUtil.find(targetClass, BundleService.class);
        if (meta == null) {
            return super.generate();
        }

        BundleService bundleService = meta.annotation;

        /* 如果定义了provider且大于0,那么使用annotation上定义的,否则使用配置文件中定义的msoa.container.id */
        int serviceProvider = bundleService.provider();
        if (serviceProvider <= 0) {
            serviceProvider = provider;
        }

        targetClass = meta.bind;
        String packageStr = targetClass.getPackage().getName();
        if (!packageStr.endsWith("v_" + bundleService.version())) {
            packageStr += (".p_" + provider);
            packageStr += (".v_" + bundleService.version());
        }

        builder = ProtoFile.builder(targetClass.getSimpleName())
                .packageName(packageStr)
                .addOption(OptionElement.create("provider", Kind.STRING, serviceProvider + ""))
                .addOption(OptionElement.create("service", Kind.STRING, bundleService.name()))
                .addOption(OptionElement.create("version", Kind.STRING, bundleService.version() + ""));

        for (Method method : targetClass.getDeclaredMethods()) {

            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }

            generate(method);
        }

        return builder.build();
    }

    protected ProtoFile generate(Method method) {
        Class<?> returnType = method.getReturnType();
        Class<?> usedType = returnType;

        if (returnType.isArray()) {
            usedType = returnType.getComponentType();
        }

        CollectionElementType collectionElementType = method.getAnnotation(CollectionElementType.class);
        if (collectionElementType != null) {
            usedType = collectionElementType.value();
            //            element = create(type);
            //            builder.addOption(OptionElement.create("element_" + method.getName(),
            //                    Kind.STRING,
            //                    element != null ? element.name() : type.getName()));
        }

        TypeElement typeElement = create(usedType);
        String returnTypeOption = typeElement != null ? typeElement.name() : usedType.getName();
        if (returnType.isArray() || Collection.class.isAssignableFrom(returnType)) {
            returnTypeOption += "[]";
        }

        builder.addOption(OptionElement.create("return_" + method.getName(), Kind.STRING, returnTypeOption));

        for (int i = 0; i < method.getParameterTypes().length; i++) {
            Class<?> type = method.getParameterTypes()[i];

            typeElement = create(type);
            builder.addOption(OptionElement.create("argument_" + method.getName() + "_" + i,
                    Kind.STRING,
                    typeElement != null ? typeElement.name() : type.getName()));
        }

        return builder.build();
    }

    @Override
    protected DataType getTypeName(Class<?> clazz) {
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

        String name;
        if (clazz.getPackage().getName().endsWith("p_" + provider)) {
            name = clazz.getPackage().getName() + "." + clazz.getSimpleName();
        } else {
            name = clazz.getPackage().getName() + ".p_" + provider + "." + clazz.getSimpleName();
        }

        return NamedType.create(name);
    }
}
