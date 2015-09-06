package com.baidu.ub.msoa.container.support.bundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Created by pippo on 15/6/23.
 */
public interface BundleContext extends BundleConstants {

    String BUNDLE_CONTEXT_FILE_FORMAT = BUNDLE_CONTEXT_ROOT + "/%s/" + BUNDLE_CONTEXT_FILE;
    String BUNDLE_CONTEXT_PATH_FORMAT = "/%s/*";

    /**
     * init context
     *
     * @param servletContext
     * @throws ServletException
     */
    void init(ServletContext servletContext) throws ServletException;

    /**
     * @return bundle name
     */
    String getName();

    /**
     * set bundle name
     *
     * @param name
     */
    void setName(String name);

    void setClassLoader(ClassLoader classLoader);

}
