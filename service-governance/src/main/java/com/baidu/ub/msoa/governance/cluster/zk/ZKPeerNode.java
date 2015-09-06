package com.baidu.ub.msoa.governance.cluster.zk;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by pippo on 15/8/3.
 */
@Component
public class ZKPeerNode extends QuorumPeerMain {

    private static Logger logger = LoggerFactory.getLogger(ZKPeerNode.class);
    private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    @Resource
    private QuorumPeerConfig config;

    @PostConstruct
    public void start() {
        logger.info("start the zk server node");
        executor.schedule(new Daemon(), 5, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void stop() {
        logger.info("stop the zk server node");

        if (quorumPeer != null) {
            quorumPeer.shutdown();
        }
    }

    private class Daemon implements Runnable {

        @Override
        public void run() {

            try {

                if (config.getServers().isEmpty()) {
                    ZooKeeperServerMain main = new ZooKeeperServerMain();
                    ServerConfig serverConfig = new ServerConfig();
                    serverConfig.readFrom(config);
                    try {
                        main.runFromConfig(serverConfig);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    runFromConfig(config);
                }

            } catch (Throwable e) {
                logger.error("zk node due to error,the node would be stop", e);
                stop();
            }

            logger.info("try to restart zk node");
            start();
        }
    }

}
