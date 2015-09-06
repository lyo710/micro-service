package com.baidu.ub.msoa.container.support.governance.conf;

import com.baidu.ub.msoa.container.support.governance.HttpClient;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by pippo on 15/8/22.
 */
@Configuration
public class ServiceTopologyAddressesFactoryBean {

    @Resource(name = "msoa.governance.addresses")
    private Addresses governanceAddresses;

    @Bean(name = "msoa.governance.addresses.service.topology.subscribe")
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Addresses serviceTopologySubscribe() {
        Set<String> addresses = new HashSet<>();
        for (String governanceAddress : governanceAddresses.urls) {
            String address = governanceAddress.replace("http://", "ws://") + "/service-governance/topology/subscribe";
            addresses.add(address);
        }

        return new Addresses(addresses.toArray(new String[addresses.size()]));
    }

    @Bean(name = "msoa.governance.addresses.service.topology.fetch")
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Addresses serviceTopologyFetch() {
        Set<String> addresses = new HashSet<>();
        for (String governanceAddress : governanceAddresses.urls) {
            addresses.add(String.format("%s/service-governance/topology/fetch", governanceAddress));
        }

        return new Addresses(addresses.toArray(new String[addresses.size()]));
    }

    @Bean(name = "msoa.governance.client.service.topology.fetch")
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public HttpClient client() {
        return new HttpClient(serviceTopologyFetch().urls);
    }
}
