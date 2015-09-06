package com.baidu.ub.msoa.container.test;

import com.baidu.ub.msoa.container.BundleContainer;
import com.baidu.ub.msoa.container.support.bundle.BundleInitializer;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.SpringBaseBundleContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

import javax.servlet.ServletContext;

/**
 * Created by pippo on 15/7/3.
 */
public class BundleDependencyInjectionTestExecutionListener extends DependencyInjectionTestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        ServletContext sc = new MockServlet3Context();

        GenericWebApplicationContext context = new GenericWebApplicationContext(sc);
        context.setParent(testContext.getApplicationContext());
        context.refresh();

        sc.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);

        BundleIntegrationTest meta = testContext.getTestClass().getAnnotation(BundleIntegrationTest.class);
        if (meta == null) {
            return;
        }
        new BundleInitializer().onStartup(null, sc);
    }

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        //        System.out.println(testContext.getApplicationContext());

        super.prepareTestInstance(testContext);
    }

    @Override
    protected void injectDependencies(TestContext testContext) throws Exception {
        BundleIntegrationTest meta = testContext.getTestClass().getAnnotation(BundleIntegrationTest.class);

        if (meta != null) {
            Object bean = testContext.getTestInstance();
            SpringBaseBundleContext context = (SpringBaseBundleContext) BundleContainer.get().getBundle(meta.bundle());
            AutowireCapableBeanFactory beanFactory = context.getAutowireCapableBeanFactory();
            beanFactory.autowireBeanProperties(bean, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
            beanFactory.initializeBean(bean, testContext.getTestClass().getName());
            testContext.removeAttribute(REINJECT_DEPENDENCIES_ATTRIBUTE);
        }

    }
}
