package org.springframework.beans.factory.support;

import com.baidu.ub.msoa.container.support.bundle.BundleServiceStub;
import com.baidu.ub.msoa.container.support.bundle.IBundleService;
import com.baidu.ub.msoa.container.support.governance.discover.annotation.BundleService;
import com.baidu.ub.msoa.container.support.governance.domain.BundleServiceNameSpace;
import com.baidu.ub.msoa.container.support.governance.domain.model.BundleServiceMetaInfo;
import com.baidu.ub.msoa.container.support.rpc.RPCException;
import com.baidu.ub.msoa.container.support.rpc.RPCStatus;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.ErrorInfo;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCArguments;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCDestination;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCRequest;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCResponse;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCResult;
import com.baidu.ub.msoa.container.support.rpc.domain.model.EventType;
import com.baidu.ub.msoa.container.support.rpc.domain.model.RPCEvent;
import com.baidu.ub.msoa.container.support.rpc.outbound.event.OutboundEventEngine;
import com.baidu.ub.msoa.utils.JavassistUtil;
import com.google.common.base.Throwables;
import com.google.common.base.Verify;
import javassist.ClassPool;
import javassist.CtClass;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pippo on 15/6/23.
 */
public class BundleServiceInjector implements BundleServiceNameSpace {

    private static Logger logger = LoggerFactory.getLogger(BundleServiceInjector.class);

    public BundleServiceInjector(ConfigurableListableBeanFactory beanFactory) {
        this.eventEngine = beanFactory.getBean(OutboundEventEngine.class);
        this.stubInterceptor = new StubInterceptor();
    }

    private OutboundEventEngine eventEngine;
    private StubInterceptor stubInterceptor;

    public void inject(Object target, Field field) {
        try {
            Object proxy = createProxy(field);
            field.setAccessible(true);
            field.set(target, proxy);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T createProxy(Field field) throws Exception {

        Class<?> targetClazz = field.getType();
        Object invoker = createProxyClass(targetClazz).newInstance();

        ProxyFactory proxyFactory = new ProxyFactory(invoker);
        proxyFactory.setOptimize(true);
        proxyFactory.setProxyTargetClass(true);
        // proxyFactory.addAdvice(new BundleServiceStubInterceptor(targetClazz));
        proxyFactory.addAdvice(stubInterceptor);

        if (targetClazz.isInterface()) {
            proxyFactory.setInterfaces(targetClazz);
        } else {
            proxyFactory.setTargetClass(targetClazz);
        }

        return (T) proxyFactory.getProxy();
    }

    private Class<?> createProxyClass(Class<?> targetClazz) throws Exception {
        ClassPool pool = JavassistUtil.getDefault();
        String proxyClassName = String.format("%sProxy", targetClazz.getName());
        logger.info("create bundle service proxy:[{}]", proxyClassName);

        CtClass classDeclaring = pool.getOrNull(proxyClassName);
        if (classDeclaring != null) {
            return Class.forName(proxyClassName);
        }

        classDeclaring = pool.getAndRename(BundleServiceStub.class.getName(), proxyClassName);
        if (targetClazz.isInterface()) {
            classDeclaring.setSuperclass(pool.get(BundleServiceStub.class.getName()));
            classDeclaring.addInterface(pool.get(targetClazz.getName()));
        } else {
            classDeclaring.setSuperclass(pool.get(targetClazz.getName()));
            classDeclaring.addInterface(pool.get(IBundleService.class.getName()));
        }

        return classDeclaring.toClass();
    }

    private static Map<String, BundleServiceMetaInfo> _cache = new HashMap<>();

    public class StubInterceptor implements MethodInterceptor, BundleServiceNameSpace {

        private BundleServiceMetaInfo getInvokerMetaInfo(Method method) {
            BundleService annotation = method.getAnnotation(BundleService.class);
            System.out.println(annotation);
            if (annotation == null) {
                return null;
            }

            BundleServiceMetaInfo metaInfo = _cache.get(annotation.toString());
            if (metaInfo == null) {
                metaInfo = new BundleServiceMetaInfo(annotation.provider(),
                        annotation.name(),
                        annotation.version(),
                        annotation.method(),
                        annotation.remoteStrategy());
                _cache.put(annotation.toString(), metaInfo);
            }

            return metaInfo;
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            Method method = invocation.getMethod();
            BundleServiceMetaInfo metaInfo = getInvokerMetaInfo(method);
            Verify.verifyNotNull(metaInfo,
                    String.format("can not execute method:[%s], because can not find @BundleService",
                            method.getName()));

            try {
                RPCEvent event = buildEvent(invocation, metaInfo);
                eventEngine.process(event);
                RPCResponse response = event.payload.response;
                RPCResult result = event.payload.response.result;

                switch (RPCStatus.valueOf(response.status)) {
                    case SUCCESS:
                        return result.bytes2value(invocation.getMethod().getReturnType());
                    case INNER_ERROR:
                        ErrorInfo info = result.bytes2value(ErrorInfo.class);
                        try {
                            Class<?> clazz = Class.forName(info.exceptionType);
                            if (Throwable.class.isAssignableFrom(clazz)) {
                                throw (Throwable) clazz.getConstructor(String.class).newInstance(info.stackTrace);
                            } else {
                                throw new RPCException(info.stackTrace);
                            }
                        } catch (Throwable throwable) {
                            throw new RPCException(info.stackTrace);
                        }

                    default:
                        throw new RPCException(new String(result.payload));
                }

            } catch (Throwable e) {
                LoggerFactory.getLogger(invocation.getThis().getClass())
                        .error("execute method:[{}] due error:[{}]",
                                method.getName(),
                                Throwables.getStackTraceAsString(e));
                throw e;
            }
        }

        private RPCEvent buildEvent(MethodInvocation invocation, BundleServiceMetaInfo metaInfo) {
            RPCRequest request = new RPCRequest();
            request.header.destination = new RPCDestination(metaInfo.provider,
                    metaInfo.service,
                    metaInfo.version,
                    metaInfo.method,
                    metaInfo.routeStrategy);
            request.arguments = new RPCArguments(invocation.getArguments());
            return new RPCEvent(EventType.outbound, request);
        }
    }

}
