package com.baidu.ub.msoa.container.support.governance.conf;

import com.baidu.ub.msoa.container.support.bundle.annotation.ProductProfile;
import com.baidu.ub.msoa.container.support.bundle.annotation.TestProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Created by pippo on 15/8/22.
 */
@Configuration
public class GovernanceAddressesFactoryBean {

    private static Logger logger = LoggerFactory.getLogger(GovernanceAddressesFactoryBean.class);

    @Resource(name = "msoa.container.properties")
    private Properties properties;

    /**
     * 测试环境使用配置文件
     *
     * @return governance address list
     */
    @Bean(name = "msoa.governance.addresses")
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @TestProfile
    public Addresses addresses4conf() {
        Set<String> addresses = new HashSet<>();

        for (Object key : properties.keySet()) {
            if (key.toString().startsWith("msoa.governance.addresses")) {
                addresses.add((String) properties.get(key));
            }
        }

        return new Addresses(addresses.toArray(new String[addresses.size()]));
    }

    /**
     * 生成环境应该从BNS中获取
     *
     * @return governance address list
     */
    @Bean(name = "msoa.governance.addresses")
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @ProductProfile
    public Addresses addresses4bns() {
        // TODO
        logger.error("@@@@@@@" + properties);
        Set<String> addresses = new HashSet<>();

        for (Object key : properties.keySet()) {
            if (key.toString().startsWith("msoa.governance.addresses")) {
                addresses.add((String) properties.get(key));
            }
        }

        return new Addresses(addresses.toArray(new String[addresses.size()]));
    }

}
