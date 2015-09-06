package com.baidu.ub.msoa.container.test;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by pippo on 15/7/3.
 */
@ContextConfiguration({ "classpath*:META-INF/bundles.test.xml" })
@TestExecutionListeners({ ServletTestExecutionListener.class,
        BundleDependencyInjectionTestExecutionListener.class,
        // DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        SqlScriptsTestExecutionListener.class })
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BundleIntegrationTest {

    /**
     * @return 当前组件的名称
     */
    String bundle();

    //    /**
    //     * @return 在那些包下面扫描是否存在组件
    //     */
    //    String[] basePackages() default { "com.sirius", "com.baidu" };

    //    MavenDependency[] mvnComponents() default {};
    //
    //    URLDependency[] externalComponents() default {};
    //
    //    @interface MavenDependency {
    //
    //        String groupId();
    //
    //        String artifactId();
    //
    //        String version();
    //
    //        String repoURL() default "http://repo2.maven.org/maven2";
    //
    //    }
    //
    //    @interface URLDependency {
    //
    //        String value();
    //
    //    }

}


