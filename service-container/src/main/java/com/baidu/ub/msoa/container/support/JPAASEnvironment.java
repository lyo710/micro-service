package com.baidu.ub.msoa.container.support;

import com.baidu.ub.msoa.utils.NetUtils;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by pippo on 15/8/23.
 */
@Component("jpaasEnvironment")
public class JPAASEnvironment {

    /**
     * System properties of server host for JPaaS2.0.
     * Usually defined in shell.
     */
    public static final String SYSTEM_PROPERTY_HOST_JPAAS2 = "JPAAS_HOST";

    /**
     * System properties of server port for JPaaS1.0.
     * Get from -D property
     */
    public static final String JVM_PROPERTY_HTTP_PORT_JPAAS1 = "port.http.nonssl";

    /**
     * System properties of server port for JPaaS1.0.
     * Usually defined in shell.
     */
    public static final String SYSTEM_PROPERTY_HTTP_PROT_JPAAS1 = "VCAP_APP_PORT";

    /**
     * System properties of server port for JPaaS2.0.
     * Usually defined in shell.
     */
    public static final String SYSTEM_PROPERTY_HTTP_PORT_JPAAS2 = "JPAAS_HTTP_PORT";

    @Resource
    private Environment environment;

    @Value("${msoa.container.default.port}")
    private int defaultPort;

    private String hostIP = null;
    private String hostName = null;
    private int httpPort = -1;

    public String getHostIP() {
        if (Strings.isNullOrEmpty(hostIP)) {
            hostIP = environment.getProperty(SYSTEM_PROPERTY_HOST_JPAAS2, NetUtils.getLocalHostIP());
        }

        return hostIP;
    }

    public String getHostName() {
        if (Strings.isNullOrEmpty(hostName)) {
            hostName = environment.getProperty(SYSTEM_PROPERTY_HOST_JPAAS2, NetUtils.getHostName());
        }

        return hostName;
    }

    public int getHostHttpPort() {
        if (httpPort > 0) {
            return httpPort;
        }

        String port = environment.getProperty(JVM_PROPERTY_HTTP_PORT_JPAAS1);

        if (Strings.isNullOrEmpty(port)) {
            port = environment.getProperty(SYSTEM_PROPERTY_HTTP_PROT_JPAAS1);
        }

        if (Strings.isNullOrEmpty(port)) {
            port = environment.getProperty(SYSTEM_PROPERTY_HTTP_PORT_JPAAS2);
        }

        httpPort = port != null ? Integer.parseInt(port) : defaultPort;
        return httpPort;
    }

}
