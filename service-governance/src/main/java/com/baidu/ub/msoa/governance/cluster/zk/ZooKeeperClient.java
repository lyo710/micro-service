package com.baidu.ub.msoa.governance.cluster.zk;

import com.baidu.ub.msoa.utils.JSONUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.quorum.QuorumPeer.QuorumServer;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by pippo on 15/8/10.
 */
@Component("zooKeeperClient")
public class ZooKeeperClient {

    private static Logger logger = LoggerFactory.getLogger(ZooKeeperClient.class);
    private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private ZooKeeper zooKeeper;
    private Set<Watcher> watchers = new HashSet<>();

    @Resource
    private QuorumPeerConfig config;

    @PostConstruct
    public void start() {
        logger.info("start the zk client");

        StringBuilder connectString = new StringBuilder();

        if (config.getServers().isEmpty()) {
            /* stand along */
            connectString.append(config.getClientPortAddress().getHostString())
                    .append(":")
                    .append(config.getClientPortAddress().getPort());
        } else {
            /* cluster */
            for (QuorumServer server : config.getServers().values()) {
                connectString.append(server.addr.getHostString())
                        .append(":")
                        .append(config.getClientPortAddress().getPort())
                        .append(",");
            }
        }

        executor.schedule(new Daemon(connectString.toString()), 5, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void stop() {
        logger.info("stop the zk client");

        if (zooKeeper != null) {

            try {
                zooKeeper.close();
            } catch (InterruptedException e) {
                // remove quietness
            }

            zooKeeper = null;
        }
    }

    public ZooKeeper get() {
        long start = System.currentTimeMillis();

        while (zooKeeper == null) {
            if (System.currentTimeMillis() - start > 5000) {
                throw new RuntimeException("can not connect to the zk server");
            }
        }

        return zooKeeper;
    }

    public void create(String path, Object data) {
        get();

        try {
            if (zooKeeper.exists(path, true) != null) {
                return;
            }

            create(path.split("/"), 1, data != null ? JSONUtil.toBytes(data) : null);
        } catch (Exception e) {
            throw new RuntimeException("create zk path due to error", e);
        }
    }

    protected void create(String[] path, int index, byte[] data) {
        try {
            if (index > path.length) {
                return;
            }

            index++;
            String expectPath = Joiner.on("/").skipNulls().join(Arrays.copyOfRange(path, 0, index));

            if (zooKeeper.exists(expectPath, true) == null) {
                logger.trace("try create zk path:[{}]", expectPath);
                zooKeeper.create(expectPath,
                        index == path.length ? data : null,
                        Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            }

            create(path, index, data);
        } catch (Exception e) {
            throw new RuntimeException("create zk path due to error", e);
        }
    }

    public void setData(String path, Object data) {
        get();

        try {
            Stat stat = zooKeeper.exists(path, true);
            if (stat == null) {
                create(path, data);
                return;
            }

            zooKeeper.setData(path, data != null ? JSONUtil.toBytes(data) : null, stat.getVersion());
            zooKeeper.exists(path, true);
        } catch (Exception e) {
            throw new RuntimeException("set zk data due to error", e);
        }
    }

    public <T> T getData(String path, Class<T> clazz) {
        get();
        try {
            Stat stat = zooKeeper.exists(path, true);
            if (stat == null) {
                return null;
            }

            byte[] bb = zooKeeper.getData(path, true, stat);
            return bb != null ? JSONUtil.toObject(bb, clazz) : null;
        } catch (Exception e) {
            throw new RuntimeException("get zk data due to error", e);
        }
    }

    public void delete(String path) {
        get();
        try {
            Stat stat = zooKeeper.exists(path, true);
            if (stat == null) {
                return;
            }

            List<String> children = zooKeeper.getChildren(path, true);
            for (String child : children) {
                delete(path + "/" + child);
            }

            zooKeeper.delete(path, stat.getVersion());
        } catch (Exception e) {
            throw new RuntimeException("delete zk path due to error", e);
        }
    }

    public List<String> getChildren(String parent) {
        get();
        try {
            Stat stat = zooKeeper.exists(parent, true);
            if (stat == null) {
                return Collections.emptyList();
            }

            return zooKeeper.getChildren(parent, true);
        } catch (Exception e) {
            throw new RuntimeException("list zk path children due to error", e);
        }
    }

    public List<String> getChildrenPath(String parent) {
        List<String> childrenPath = new ArrayList<>();

        List<String> children = getChildren(parent);
        for (String child : children) {
            childrenPath.add(parent + "/" + child);
        }

        return childrenPath;
    }

    public void addWathcer(Watcher watcher) {
        this.watchers.add(watcher);
    }

    public void remove(Watcher watcher) {
        this.watchers.remove(watcher);
    }

    private class Daemon implements Runnable {

        private Daemon(String connectString) {
            this.connectString = connectString;
        }

        private String connectString;

        @Override
        public void run() {

            try {

                if (zooKeeper != null && zooKeeper.getState().isAlive()) {
                    return;
                }

                zooKeeper = new ZooKeeper(connectString, 1000 * 60 * 5, new Watcher() {

                    @Override
                    public void process(WatchedEvent event) {
                        if (event.getState() == KeeperState.Expired) {
                            stop();
                        }

                        for (Watcher watcher : watchers) {
                            try {
                                watcher.process(event);
                            } catch (Exception e) {
                                logger.warn("watcher:[{}] pro cess event:[{}] due to error:[{}]",
                                        watcher,
                                        event,
                                        Throwables.getStackTraceAsString(e));
                            }
                        }
                    }
                });
            } catch (Throwable e) {
                logger.error("zk client due to error,the client would be stop", e);
                stop();
            }

            logger.info("try to restart zk client");
            start();
        }
    }

}
