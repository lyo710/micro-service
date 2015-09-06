package com.baidu.ub.msoa.container.support.governance.contact.proto;

import com.baidu.ub.msoa.container.support.governance.domain.model.BundleServiceMetaInfo;
import com.squareup.protoparser.OptionElement;
import com.squareup.protoparser.OptionElement.Kind;
import com.squareup.protoparser.ProtoFile;
import com.squareup.protoparser.TypeElement;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by pippo on 15/8/6.
 */
public class MethodStubFileExtractor extends ServiceStubClassGenerator {

    public MethodStubFileExtractor(String schema) {
        super(schema);
    }

    public MethodStubFileExtractor(String schema, boolean writeClazz) {
        super(schema, writeClazz);
    }

    @Override
    public Set<Class<?>> generate() throws Exception {
        return super.generate();
    }

    public ProtoFile[] protoFiles() throws Exception {
        List<ProtoFile> protoFiles = new ArrayList<>();

        for (BundleServiceMetaInfo metaInfo : methodStubClazz.keySet()) {

            Class<?> methodStub = methodStubClazz.get(metaInfo);
            loadClass(methodStub.getName());
            ProtoFile file = new Generator(methodStub, metaInfo).generate();
            protoFiles.add(file);
        }

        return protoFiles.toArray(new ProtoFile[0]);
    }

    public static class Generator extends ServiceStubFileGenerator {

        public Generator(Class<?> targetClass, BundleServiceMetaInfo metaInfo) {
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

}
