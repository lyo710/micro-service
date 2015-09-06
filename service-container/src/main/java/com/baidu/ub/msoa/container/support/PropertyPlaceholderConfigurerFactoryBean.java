package com.baidu.ub.msoa.container.support;

import com.google.common.base.Verify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by pippo on 15/8/22.
 */
@Configuration
public class PropertyPlaceholderConfigurerFactoryBean implements BeanFactoryAware {

    private static Logger logger = LoggerFactory.getLogger(PropertyPlaceholderConfigurerFactoryBean.class);
    private static String propertyFileFormat = "classpath:msoa.container.%s.properties";

    @Bean(name = "msoa.container.propertyPlaceholder")
    public PropertyPlaceholderConfigurer placeholder() {
        PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
        configurer.setLocations(build());
        return configurer;
    }

    @Bean(name = "msoa.container.properties")
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Properties properties() throws IOException {
        PropertiesFactoryBean factoryBean = new PropertiesFactoryBean();
        factoryBean.setLocations(build());
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    private Resource[] build() {
        Environment environment = beanFactory.getBean(Environment.class);
        // configurer.setIgnoreResourceNotFound(true);
        // configurer.setIgnoreUnresolvablePlaceholders(true);

        List<Resource> resources = new ArrayList<>();
        for (String activeProfile : environment.getActiveProfiles()) {
            String file = String.format(propertyFileFormat, activeProfile);
            build(resources, file);
        }

        if (resources.isEmpty()) {
            build(resources, "classpath:msoa.container.properties");
        }

        return resources.toArray(new Resource[resources.size()]);
    }

    private void build(List<Resource> resources, String file) {
        logger.info("add property placeholder:[{}]", file);
        Resource resource = new DefaultResourceLoader().getResource(file);

        Verify.verify(resource.exists(), String.format("expect properties file:[%s] not exists", file));
        resources.add(resource);
    }

    private static BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
