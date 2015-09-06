package com.baidu.ub.msoa.governance.cluster.zk;

import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Properties;

/**
 * Created by pippo on 15/8/3.
 */
@Configuration
public class QuorumPeerConfigFactoryBean {

    private QuorumPeerConfig config = new QuorumPeerConfig();

    @Bean(name = "zk.peer.config")
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public QuorumPeerConfig peerConfig() throws Exception {
        // TODO load form disconf

        Properties properties = new Properties();
        properties.load(QuorumPeerConfigFactoryBean.class.getResourceAsStream("/zoo.cfg"));
        config.parseProperties(properties);
        return config;
    }

}
