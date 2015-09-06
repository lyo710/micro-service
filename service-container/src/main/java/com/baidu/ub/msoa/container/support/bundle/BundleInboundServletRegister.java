/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */
package com.baidu.ub.msoa.container.support.bundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletRegistration;

/**
 * User: pippo
 * Date: 13-11-21-12:23
 */
public class BundleInboundServletRegister {

    private static final Logger logger = LoggerFactory.getLogger(BundleInboundServletRegister.class);

    public static ServletRegistration register(String name, String mapping, WebApplicationContext context) {
        // Validate.isTrue(!isMappingExists(mapping), "invalid mapping:[%s]", mapping);
        String servletName = getServletName(name);

        ServletRegistration registration = context.getServletContext().getServletRegistration(servletName);
        if (registration == null) {
            DispatcherServlet dispatcher = new DispatcherServlet(context);
            ServletRegistration.Dynamic _registration = context.getServletContext().addServlet(servletName, dispatcher);
            _registration.addMapping(mapping);
            _registration.setAsyncSupported(true);

            registration = _registration;
            logger.info("register new spring mvc dispatcher:[{}] for mapping:[{}]", dispatcher, mapping);
        } else {
            logger.warn("the dispatcher with service:[{}] exists, ignore register!", servletName);
        }

        return registration;
    }

    protected static String getServletName(String name) {
        return "dispatcher_" + name;
    }

    // private static final Set<String> mappings = new HashSet<String>();

    // private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    //    public static boolean isMappingExists(String mapping) {
    //        for (String _mapping : mappings) {
    //            if (pathMatcher.matchStart(mapping, _mapping)) {
    //                logger.warn("the register mapping:[{}] covered by:[{}]", mapping, _mapping);
    //                return true;
    //            }
    //
    //            if (pathMatcher.matchStart(_mapping, mapping)) {
    //                logger.warn("the exists mapping:[{}] conflict with:[{}]", _mapping, mapping);
    //                return true;
    //            }
    //        }
    //
    //        return false;
    //    }

}
