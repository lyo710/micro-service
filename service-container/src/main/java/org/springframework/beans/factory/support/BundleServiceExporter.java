package org.springframework.beans.factory.support;

import com.baidu.ub.msoa.container.BundleContainer;
import com.baidu.ub.msoa.container.support.governance.contact.proto.ServiceStubFileGenerator;
import com.baidu.ub.msoa.container.support.governance.discover.annotation.BundleService;
import com.baidu.ub.msoa.container.support.governance.domain.BundleServiceNameSpace;
import com.baidu.ub.msoa.container.support.governance.domain.dto.ContactType;
import com.baidu.ub.msoa.container.support.governance.domain.dto.ServiceRegisterRequest;
import com.baidu.ub.msoa.container.support.governance.registry.ServiceRegisterStub;
import com.baidu.ub.msoa.utils.AnnotationUtil.AnnotationBindInfo;
import com.squareup.protoparser.ProtoFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Properties;

/**
 * Created by pippo on 15/6/23.
 */
public class BundleServiceExporter implements BundleServiceNameSpace {

    private static Logger logger = LoggerFactory.getLogger(BundleServiceExporter.class);

    /**
     * construct exporter with beanFactory
     *
     * @param beanFactory
     */
    public BundleServiceExporter(ConfigurableListableBeanFactory beanFactory) {
        Properties properties = beanFactory.getBean("msoa.container.properties", Properties.class);
        this.containerId = Integer.parseInt(properties.getProperty("msoa.container.id"));
        this.autoRegister = Boolean.parseBoolean(properties.getProperty("msoa.service.auto-register"));
    }

    private int containerId;
    private boolean autoRegister;

    /**
     * export bean as service
     *
     * @param meta
     * @param bean
     */
    public void export(AnnotationBindInfo<BundleService> meta, Object bean) {

        Class<?> interfaceType = meta.annotation.interfaceType();
        if (interfaceType == Object.class) {
            interfaceType = meta.bind;
        }

        Method[] methods = interfaceType.getDeclaredMethods();
        for (Method method : methods) {
            if (Modifier.isPublic(method.getModifiers())) {
                createDelegate(meta.annotation, bean, method);
            }
        }

        if (autoRegister) {
            register(bean.getClass());
        }
    }

    private void createDelegate(BundleService meta, Object bean, Method method) {
        BeanDefinition beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(MethodExecutorImpl.class)
                .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_NO)
                .setLazyInit(false)
                .setScope(BeanDefinition.SCOPE_SINGLETON)
                .setInitMethodName("init")
                .addPropertyValue("target", bean)
                .addPropertyValue("method", method)
                .getBeanDefinition();

        String name = NameSpace.serviceIdentity(containerId, meta.name(), meta.version(), method.getName());
        BundleContainer.get().registerBean(name, beanDefinition);
        logger.info("create method executor:[{}]", name);
    }

    private void register(Class<?> interfaceType) {

        ServiceStubFileGenerator generator = new ServiceStubFileGenerator(containerId, interfaceType);
        ProtoFile protoFile = generator.generate();
        ServiceRegisterRequest request = new ServiceRegisterRequest(ContactType.proto, protoFile.toSchema());

        ServiceRegisterStub registerClient = BundleContainer.get().getBean("serviceRegisterStub");
        registerClient.register(request);
    }

}
