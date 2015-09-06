package com.baidu.ub.msoa.container.support.governance.contact.proto;

import com.baidu.ub.msoa.container.support.governance.domain.model.BundleServiceMetaInfo;
import com.squareup.protoparser.OptionElement;
import com.squareup.protoparser.OptionElement.Kind;
import com.squareup.protoparser.ProtoFile;
import com.squareup.protoparser.TypeElement;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by pippo on 15/8/18.
 */
public class MethodStubFileGenerator extends ServiceStubFileGenerator {

    public MethodStubFileGenerator(Class<?> targetClass, BundleServiceMetaInfo metaInfo) {
        super(metaInfo.provider, targetClass);
        this.metaInfo = metaInfo;
    }

    private BundleServiceMetaInfo metaInfo;

    @Override
    public ProtoFile generate() {
        builder = ProtoFile.builder(targetClass.getSimpleName())
                .packageName(targetClass.getPackage().getName())
                .addOption(OptionElement.create("provider", Kind.STRING, metaInfo.provider + ""))
                .addOption(OptionElement.create("service", Kind.STRING, metaInfo.service))
                .addOption(OptionElement.create("version", Kind.STRING, metaInfo.version + ""));

        Method[] methods = targetClass.getDeclaredMethods();

        for (Method method : methods) {
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }

            generate(method);
        }

        return builder.build();
    }

    protected ProtoFile generate(Method method) {
        TypeElement element = create(method.getReturnType());
        builder.addOption(OptionElement.create("return_" + method.getName(),
                Kind.STRING,
                element != null ? element.name() : method.getReturnType().getName()));

        for (int i = 0; i < method.getParameterTypes().length; i++) {
            Class<?> type = method.getParameterTypes()[i];

            element = create(type);
            builder.addOption(OptionElement.create("argument_" + method.getName() + "_" + i,
                    Kind.STRING,
                    element != null ? element.name() : type.getName()));
        }

        return builder.build();
    }

}
