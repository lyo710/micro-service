package com.baidu.ub.msoa.container.support.bundle;

import java.util.regex.Pattern;

/**
 * Created by pippo on 15/7/14.
 */
public interface BundleConstants {

    /**
     * component conf file
     */
    String BUNDLE_CONTEXT_FILE = "bundle.context.xml";

    /**
     * component conf directory
     */
    String BUNDLE_CONTEXT_ROOT = "classpath*:META-INF";

    /**
     * component scan pattern
     */
    String BUNDLE_CONTEXT_PATTERN = BUNDLE_CONTEXT_ROOT + "/**/" + BUNDLE_CONTEXT_FILE;

    /**
     * component path scan pattern
     */
    Pattern BUNDLE_CONTEXT_PATH_PATTERN = Pattern.compile("(.*)/META-INF/(.*)/" + BUNDLE_CONTEXT_FILE);
}
