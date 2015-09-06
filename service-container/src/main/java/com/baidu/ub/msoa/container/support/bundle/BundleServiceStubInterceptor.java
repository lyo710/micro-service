package com.baidu.ub.msoa.container.support.bundle;

import com.baidu.ub.msoa.container.support.governance.discover.annotation.BundleService;
import com.baidu.ub.msoa.container.support.governance.domain.BundleServiceNameSpace;
import com.baidu.ub.msoa.container.support.governance.domain.model.BundleServiceMetaInfo;
import com.baidu.ub.msoa.container.support.router.conf.EventEngineFactory;
import com.baidu.ub.msoa.container.support.router.event.RouteEvent;
import com.baidu.ub.msoa.container.support.router.event.RouteMessage;
import com.google.common.base.Throwables;
import com.google.common.base.Verify;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pippo on 15/6/25.
 */
public class BundleServiceStubInterceptor implements MethodInterceptor, BundleServiceNameSpace {

    private static Map<String, BundleServiceMetaInfo> _cache = new HashMap<>();

    //    public BundleServiceStubInterceptor(ConfigurableListableBeanFactory beanFactory, BundleService serviceMeta) {
    //        this.beanFactory = beanFactory;
    //        this.serviceMeta = serviceMeta;
    //    }

    //    protected ConfigurableListableBeanFactory beanFactory;
    //    protected BundleService serviceMeta;

    public BundleServiceStubInterceptor(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    private Class<?> targetClass;

    private BundleServiceMetaInfo getInvokerMetaInfo(Method method) {
        BundleService annotation = method.getAnnotation(BundleService.class);
        if (annotation == null) {
            return null;
        }

        String key = String.format("%s#%s@%s",
                annotation.interfaceType().getName(),
                method.getName(),
                method.getName().hashCode());

        BundleServiceMetaInfo metaInfo = _cache.get(key);
        if (metaInfo == null) {
            metaInfo = new BundleServiceMetaInfo(annotation.provider(),
                    annotation.name(),
                    annotation.version(),
                    annotation.method(),
                    annotation.remoteStrategy());
            _cache.put(key, metaInfo);
        }

        return metaInfo;
    }

    //    private BundleServiceMetaInfo createMetaInfo(Method method, String key) {
    //        BundleServiceMetaInfo metaInfo;
    //
    //        BundleService invoker = method.getAnnotation(BundleService.class);
    //        if (invoker != null) {
    //            metaInfo = new BundleServiceMetaInfo(invoker.provider(),
    //                    invoker.name(),
    //                    invoker.version(),
    //                    invoker.method(),
    //                    invoker.remoteStrategy());
    //        } else {
    //            metaInfo = new BundleServiceMetaInfo(serviceMeta.provider(),
    //                    serviceMeta.name(),
    //                    serviceMeta.version(),
    //                    method.getName(),
    //                    serviceMeta.remoteStrategy());
    //        }
    //
    //        _cache.put(key, metaInfo);
    //        return metaInfo;
    //    }

    //    protected MethodExecutor getLocalMethodExporter(String name) {
    //        return ServiceContainer.get().getBean(name);
    //    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        BundleServiceMetaInfo metaInfo = getInvokerMetaInfo(method);
        Verify.verifyNotNull(metaInfo,
                String.format("can not execute method:[%s], because can not find @BundleService", method.getName()));

        RouteMessage message = new RouteMessage(metaInfo.provider,
                metaInfo.service,
                metaInfo.version,
                metaInfo.method,
                metaInfo.routeStrategy);

        message.argumentTypes = invocation.getMethod().getParameterTypes();
        message.arguments = invocation.getArguments();
        message.resultType = method.getReturnType();

        try {
            EventEngineFactory.getRouteEngineByProvider(metaInfo.getProvider()).process(new RouteEvent(message));
            return message.result;
        } catch (Exception e) {
            LoggerFactory.getLogger(targetClass)
                    .error("execute method:[{}#{}] due error:[{}]",
                            targetClass.getName(),
                            method.getName(),
                            Throwables.getStackTraceAsString(e));
            throw e;
        }
    }
}