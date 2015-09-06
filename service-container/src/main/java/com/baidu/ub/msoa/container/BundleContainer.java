package com.baidu.ub.msoa.container;

import com.baidu.ub.msoa.container.support.bundle.BundleContext;
import com.google.common.base.Verify;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.SpringBaseBundleContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletContext;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pippo on 15/6/23.
 */
public class BundleContainer implements BundleConstants {

    private static Logger logger = LoggerFactory.getLogger(BundleContainer.class);
    private static BundleContainer container = new BundleContainer();
    private static XmlWebApplicationContext iocContainer;
    private static Map<String, SpringBaseBundleContext> component_name_mapping = new HashMap<>();

    public static void init(ServletContext ctx) {
        if (iocContainer != null) {
            return;
        }

        iocContainer = new XmlWebApplicationContext();
        iocContainer.registerShutdownHook();

        WebApplicationContext parent = WebApplicationContextUtils.getWebApplicationContext(ctx);
        if (parent != null) {
            iocContainer.setClassLoader(parent.getClassLoader());
            iocContainer.setParent(parent);
        }

        String profile = System.getProperty(MSOA_VM_OPTIONS_PROFILE);
        if (profile != null) {
            iocContainer.getEnvironment().addActiveProfile(profile);
        }

        iocContainer.setConfigLocation("classpath*:META-INF/bundles.container.xml");
        iocContainer.setServletContext(ctx);
        iocContainer.refresh();
    }

    //    public static void initBundle(BundleContext bundle, ServletContext ctx) {
    //        init(ctx);
    //        initBundle(bundle);
    //    }

    public static void create(BundleContext bundle) {
        Verify.verify(iocContainer != null, "the container not init, please invoke init(ServletContext ctx) first");

        SpringBaseBundleContext context = (SpringBaseBundleContext) bundle;

        try {
            logger.info("##########begin init bundle:[{}]##########", context.getName());
            context.setClassLoader(iocContainer.getClassLoader());
            context.setParent(iocContainer);
            context.init(iocContainer.getServletContext());
            logger.info("##########finish init bundle:[{}]##########", context.getName());
            component_name_mapping.put(context.getName(), context);
        } catch (Exception e) {
            logger.error("init bundle:[{}] due to error:[{}]", context, ExceptionUtils.getStackTrace(e));
        }
    }

    public static void create(URL url) {
        // TODO 可以在运行时通过url直接加载并初始化一个组件(服务)

        // 1 jar下载到本地
        // 2 扫描jar,查找component.context.xml
        // 3 比对组件是否已经存在
        // 4 加载jar
        // 5 初始化context
        //        URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        //        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        //        method.setAccessible(true);
        //        method.invoke(classLoader,
        //                new URL("file:///Users/pippo/Documents/net-storage/everbox/maven-repository/com/sirius/component-support/example/component-2/20150622/component-2-20150622.jar"));
        //
        //        Thread.currentThread().setContextClassLoader(classLoader);
    }

    /**
     * getDefault component container
     *
     * @return ComponentContainer
     */
    public static BundleContainer get() {
        return container;
    }

    /**
     * getDefault all components
     *
     * @return ComponentContext[]
     */
    public BundleContext[] getBundles() {
        return component_name_mapping.values().toArray(new BundleContext[0]);
    }

    /**
     * getDefault component with name
     *
     * @param name
     * @return ComponentContext
     */
    public BundleContext getBundle(String name) {
        return component_name_mapping.get(name);
    }

    /**
     * register bean with name
     *
     * @param name
     * @param beanDefinition
     */
    public void registerBean(String name, BeanDefinition beanDefinition) {
        ((DefaultListableBeanFactory) iocContainer.getBeanFactory()).registerBeanDefinition(name, beanDefinition);
    }

    /**
     * getDefault bean with name
     *
     * @param name
     * @param <T>
     * @return bean
     */
    public <T> T getBean(String name) {
        if (iocContainer.containsBean(name)) {
            return (T) iocContainer.getBean(name);
        }

        for (SpringBaseBundleContext componentContext : component_name_mapping.values()) {
            if (componentContext.containsBean(name)) {
                return (T) componentContext.getBean(name);
            }
        }

        return null;
    }

    /**
     * getDefault  ServletContext
     *
     * @return ServletContext
     */
    public ServletContext getServletContext() {
        return iocContainer.getServletContext();
    }

}
