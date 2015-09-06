package org.springframework.beans.factory.support;

import com.baidu.ub.msoa.container.support.bundle.BundleContext;
import com.baidu.ub.msoa.container.support.bundle.BundleContextPostProcessor;
import com.baidu.ub.msoa.container.support.bundle.BundleInboundServletRegister;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Created by pippo on 15/7/27.
 */
public class SpringBaseBundleContext extends XmlWebApplicationContext implements BundleContext {

    @Override
    public void init(ServletContext servletContext) throws ServletException {
        // init component context
        addBeanFactoryPostProcessor(new BundleContextPostProcessor());
        setConfigLocation(String.format(BUNDLE_CONTEXT_FILE_FORMAT, getName()));
        setServletContext(servletContext);
        registerShutdownHook();
        refresh();

        // init spring mvc dispatcher
        BundleInboundServletRegister.register(getName(), String.format(BUNDLE_CONTEXT_PATH_FORMAT, getName()), this);
    }

    protected String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setClassLoader(ClassLoader classLoader) {
        super.setClassLoader(classLoader);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
