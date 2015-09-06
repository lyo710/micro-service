package com.baidu.ub.msoa.container.support.governance.conf;

import com.baidu.ub.msoa.container.support.governance.HttpClient;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by pippo on 15/8/22.
 */
@Configuration
@Order(100)
public class ServiceRegisterAddressesFactoryBean {

    @Resource(name = "msoa.governance.addresses")
    private Addresses addresses;

    @Bean(name = "msoa.governance.client.service.register")
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public HttpClient client() {
        return new HttpClient(addresses());
    }

    public String[] addresses() {
        Set<String> output = new HashSet<>();
        for (String governanceAddress : addresses.urls) {
            output.add(String.format("%s/service-governance/register", governanceAddress));
        }

        return output.toArray(new String[output.size()]);
    }
}
