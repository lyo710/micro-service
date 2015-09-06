/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : lms-job
 * user created : yangy
 * date created : 2013-7-11 - 上午9:56:12
 */
package com.baidu.ub.msoa.utils.freemarker;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.Map;

/**
 * FreemarkerUtils
 *
 * @author songsp
 * @since 2013-7-11
 */
public class FreemarkerUtils {

    private static final Logger logger = LoggerFactory.getLogger(FreemarkerUtils.class);

    private static Configuration configuration = null;

    static {
    }

    /**
     * merge template and data
     *
     * @param templateName
     * @param context
     * @return rendered result
     */
    public static String toString(String templateName, Map<String, Object> context) {

        try {
            if (configuration == null) {
                configuration = new Configuration();
                configuration.setTemplateLoader(new ClassTemplateLoader(FreemarkerUtils.class, "/META-INF"));
            }

            Template template = configuration.getTemplate(templateName);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, context);
        } catch (Exception e) {
            logger.error("process template:[{}] with context:[{}] due to error:[{}]", templateName,
                    context,
                    ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

}
