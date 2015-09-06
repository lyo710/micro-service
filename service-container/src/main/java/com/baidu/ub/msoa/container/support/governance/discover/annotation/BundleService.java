package com.baidu.ub.msoa.container.support.governance.discover.annotation;

import com.baidu.ub.msoa.container.support.router.RouteStrategy;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by pippo on 15/6/23.
 */
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface BundleService {

    /**
     * @return service provider
     */
    int provider() default -1;

    /**
     * @return service name
     */
    String name() default "";

    /**
     * @return service version
     */
    int version() default 1;

    /**
     * @return rpc method name
     */
    String method() default "";

    /**
     * @return method scan interface
     */
    Class<?> interfaceType() default Object.class;

    /**
     * @return service remote strategy
     */
    RouteStrategy remoteStrategy() default RouteStrategy.switchover;
}
