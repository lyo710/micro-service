package com.baidu.ub.msoa.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by pippo on 15/7/20.
 */
public class MBeanHelper {

    private static Logger logger = LoggerFactory.getLogger(MBeanHelper.class);

    public static <T> void registerMBean(String name, T bean, Class<T> interfaceType) {
        try {
            MBeanServer mBeanServer = findMBeanServer();
            ObjectName objectName = getObjectName(name);
            mBeanServer.registerMBean(new StandardMBean(bean, interfaceType, true), objectName);
            logger.info("register new getStatistics mbean:[{}]", objectName);
        } catch (Exception e) {
            logger.warn(String.format("register mbean:[%s] due to error", name), e);
        }
    }

    private static AtomicInteger index = new AtomicInteger(0);

    private static ObjectName getObjectName(String name) throws MalformedObjectNameException {
        return new ObjectName(String.format("micro-container:monitor=%s-%s", name, index.incrementAndGet()));
    }

    private static MBeanServer findMBeanServer() {
        ArrayList<MBeanServer> servers = MBeanServerFactory.findMBeanServer(null);
        if (servers.isEmpty()) {
            return ManagementFactory.getPlatformMBeanServer();
        }

        return servers.get(0);
    }


}
