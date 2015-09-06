package com.baidu.ub.msoa.container.support.bundle.annotation;

import com.baidu.ub.msoa.container.test.BundleTestConstants;
import org.springframework.context.annotation.Profile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by pippo on 15/8/22.
 */
@Profile("!" + BundleTestConstants.PROFILE)
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface ProductProfile {
}
