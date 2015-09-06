package com.baidu.ub.msoa.container.support.governance.domain;

/**
 * Created by pippo on 15/6/25.
 */
public interface BundleServiceNameSpace {

    class NameSpace {

        public static final String BUNDLE_SERVICE_ID_FORMAT = "%s.%s_%s.%s";

        public static String serviceIdentity(int provider, String service, int version, String method) {
            return String.format(BUNDLE_SERVICE_ID_FORMAT, provider, service, version, method);
        }

    }

}
