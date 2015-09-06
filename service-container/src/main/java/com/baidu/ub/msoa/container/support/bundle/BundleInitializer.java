package com.baidu.ub.msoa.container.support.bundle;

import com.baidu.ub.msoa.container.BundleContainer;
import com.baidu.ub.msoa.utils.slf4j.UtilLoggerBridge;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.SpringBaseBundleContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * Created by pippo on 15/7/2.
 */
// @HandlesTypes(ComponentContext.class)
public class BundleInitializer implements ServletContainerInitializer, BundleConstants {

    static {
        UtilLoggerBridge.bridge();
    }

    private static Logger logger = LoggerFactory.getLogger(BundleInitializer.class);
    private static PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    @Override
    public void onStartup(Set<Class<?>> classList, ServletContext ctx) throws ServletException {
        BundleContainer.init(ctx);

        for (Resource resource : scanBundleResources()) {
            try {
                Matcher matcher = BUNDLE_CONTEXT_PATH_PATTERN.matcher(resource.getURL().getPath());

                if (matcher.find()) {
                    String name = matcher.group(2);
                    logger.debug("find bundle:[{}] context file:[{}]", name, resource.getURL().getPath());

                    BundleContext bundle = new SpringBaseBundleContext();
                    bundle.setName(name);
                    BundleContainer.create(bundle);
                }
            } catch (Exception e) {
                logger.warn("initBundle component:[{}] due to error:[{}]", resource, ExceptionUtils.getStackTrace(e));
            }
        }

    }

    private Resource[] scanBundleResources() {
        Resource[] contextResources;

        try {
            contextResources = resourceResolver.getResources(BUNDLE_CONTEXT_PATTERN);
        } catch (IOException e) {
            throw new BundleInitException(e);
        }

        return contextResources;
    }

}
