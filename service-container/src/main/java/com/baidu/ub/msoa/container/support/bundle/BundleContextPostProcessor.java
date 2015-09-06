package com.baidu.ub.msoa.container.support.bundle;

import com.baidu.ub.msoa.container.support.governance.discover.annotation.BundleService;
import com.baidu.ub.msoa.utils.AnnotationUtil;
import com.baidu.ub.msoa.utils.AnnotationUtil.AnnotationBindInfo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BundleServiceExporter;
import org.springframework.beans.factory.support.BundleServiceInjector;

import java.lang.reflect.Field;

/**
 * Created by pippo on 15/6/23.
 */
public class BundleContextPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.addBeanPostProcessor(new ModuleServicePostProcessor(beanFactory));
    }

    public static class ModuleServicePostProcessor implements BeanPostProcessor {

        public ModuleServicePostProcessor(ConfigurableListableBeanFactory beanFactory) {
            this.moduleServiceExporter = new BundleServiceExporter(beanFactory);
            this.moduleServiceInjector = new BundleServiceInjector(beanFactory);
        }

        private BundleServiceExporter moduleServiceExporter;
        private BundleServiceInjector moduleServiceInjector;

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            AnnotationBindInfo<BundleService> meta = AnnotationUtil.find(bean.getClass(), BundleService.class);

            if (meta != null) {
                moduleServiceExporter.export(meta, bean);
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                BundleService annotation = field.getAnnotation(BundleService.class);
                if (annotation == null) {
                    continue;
                }

                moduleServiceInjector.inject(bean, field);
            }

            return bean;
        }

    }
}
