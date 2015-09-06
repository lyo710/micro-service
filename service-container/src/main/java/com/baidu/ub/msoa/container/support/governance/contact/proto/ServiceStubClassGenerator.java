package com.baidu.ub.msoa.container.support.governance.contact.proto;

import com.baidu.ub.msoa.container.support.governance.discover.annotation.BundleService;
import com.baidu.ub.msoa.container.support.governance.domain.model.BundleServiceMetaInfo;
import com.baidu.ub.msoa.container.support.router.RouteStrategy;
import com.google.common.base.Verify;
import com.squareup.protoparser.OptionElement;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.IntegerMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by pippo on 15/8/6.
 */
public class ServiceStubClassGenerator extends ProtoClassGenerator {

    private static Logger logger = LoggerFactory.getLogger(ServiceStubClassGenerator.class);

    public ServiceStubClassGenerator(String schema) {
        super(schema);
    }

    public ServiceStubClassGenerator(String schema, boolean writeClazz) {
        super(schema, writeClazz);
    }

    @Override
    public Set<Class<?>> generate() throws Exception {
        super.generate();
        this.generateServiceStub();
        return generatedClazz;
    }

    protected int provider;
    protected String service;
    protected int version;
    protected Class<?> serviceStubClazz;
    protected Map<BundleServiceMetaInfo, Class<?>> methodStubClazz = new HashMap<>();

    public int getProvider() {
        return provider;
    }

    public String getService() {
        return service;
    }

    public int getVersion() {
        return version;
    }

    public Class<?> getServiceStubClazz() {
        return serviceStubClazz;
    }

    public Map<BundleServiceMetaInfo, Class<?>> getMethodStubClazz() {
        return methodStubClazz;
    }

    public void generateServiceStub() throws Exception {
        provider = Integer.parseInt(getOptionValue(protoFile, "provider"));
        service = getOptionValue(protoFile, "service");
        version = Integer.parseInt(getOptionValue(protoFile, "version"));
        Verify.verify(provider > 0);
        Verify.verifyNotNull(service);
        Verify.verifyNotNull(version);

        createMethodStubs();
        createServiceStub();

        if (writeClazz) {

            CtClass ctClass;
            for (Class<?> clazz : methodStubClazz.values()) {
                ctClass = pool.get(clazz.getName());
                ctClass.writeFile("generated/" + timestamp);
                ctClass.defrost();
            }

            ctClass = pool.get(serviceStubClazz.getName());
            ctClass.writeFile("generated/" + timestamp);
            ctClass.defrost();
        }

    }

    private void createMethodStubs() throws CannotCompileException {
        List<OptionElement> returnTypes = getOptionByPrefix(protoFile, "return_");
        for (OptionElement type : returnTypes) {
            BundleServiceMetaInfo metaInfo = new BundleServiceMetaInfo(provider,
                    service,
                    version,
                    type.name().replace("return_", ""),
                    RouteStrategy.switchover);

            createMethodStub(metaInfo, (String) type.value());
        }
    }

    private void createMethodStub(BundleServiceMetaInfo metaInfo, String returnType) throws CannotCompileException {

        String interfaceType =
                String.format("%s.%s%s", protoFile.packageName(), upperFirst(service), upperFirst(metaInfo.method));

        // Class<?> clazz = loadClass(interfaceType);

        // if (clazz == null) {
        CtClass m_interface = pool.getOrNull(interfaceType);
        if (m_interface != null) {
            m_interface.defrost();
        }

        m_interface = pool.makeInterface(interfaceType);

        List<String> argumentTypes = getOptionValueByPrefix(protoFile, "argument_" + metaInfo.method);
        StringBuilder sb = new StringBuilder(returnType).append(" ").append(metaInfo.method).append("(");
        for (int i = 0; i < argumentTypes.size(); i++) {
            sb.append(argumentTypes.get(i)).append(" arg").append(i);
            if (i != argumentTypes.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(");");

        CtMethod ctMethod = CtMethod.make(sb.toString(), m_interface);
        m_interface.addMethod(ctMethod);
        addInvokerAnnotation(provider, service, version, m_interface, ctMethod);

        Class<?> clazz = m_interface.toClass();
        logger.debug("created method stub:[{}]", clazz);
        //        } else {
        //            logger.debug("load method stub:[{}]", clazz);
        //        }

        methodStubClazz.put(metaInfo, clazz);
        generatedClazz.add(clazz);
    }

    private void createServiceStub() throws NotFoundException, CannotCompileException {
        String interfaceType = String.format("%s.%s", protoFile.packageName(), upperFirst(service));
        //        serviceStubClazz = loadClass(interfaceType);

        //        if (serviceStubClazz == null) {
        CtClass service_stub = pool.getOrNull(interfaceType);
        if (service_stub != null) {
            service_stub.defrost();
        }

        service_stub = pool.makeInterface(interfaceType);

        for (Class<?> methodStub : methodStubClazz.values()) {
            service_stub.addInterface(pool.get(methodStub.getName()));
        }

        serviceStubClazz = service_stub.toClass();
        logger.debug("created service stub:[{}]", serviceStubClazz);
        //        } else {
        //            logger.debug("load service stub:[{}]", serviceStubClazz);
        //
        //        }

        generatedClazz.add(serviceStubClazz);
    }

    private void addInvokerAnnotation(int provider,
            String service,
            int version,
            CtClass m_interface,
            CtMethod ctMethod) {

        ConstPool constPool = m_interface.getClassFile().getConstPool();
        Annotation annotation = new Annotation(BundleService.class.getName(), constPool);
        annotation.addMemberValue("provider", new IntegerMemberValue(constPool, provider));
        annotation.addMemberValue("name", new StringMemberValue(service, constPool));
        annotation.addMemberValue("version", new IntegerMemberValue(constPool, version));
        annotation.addMemberValue("method", new StringMemberValue(ctMethod.getName(), constPool));

        AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        attr.addAnnotation(annotation);
        ctMethod.getMethodInfo().addAttribute(attr);
    }

}
