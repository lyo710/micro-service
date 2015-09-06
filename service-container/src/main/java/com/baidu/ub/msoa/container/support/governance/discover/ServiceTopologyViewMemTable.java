package com.baidu.ub.msoa.container.support.governance.discover;

import com.baidu.ub.msoa.container.support.governance.domain.model.topology.ServiceTopologyView;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by pippo on 15/8/23.
 */
@Repository
public class ServiceTopologyViewMemTable {

    private ReentrantLock lock = new ReentrantLock(false);
    private Map<Integer, ProviderServiceTopologyViewTable> tables = new HashMap<>();

    public ServiceTopologyView get(int provider, String serviceIdentity) {
        return getTable(provider).get(serviceIdentity);
    }

    public void save(int provider, String serviceIdentity, ServiceTopologyView view) {
        getTable(provider).put(serviceIdentity, view);
    }

    private ProviderServiceTopologyViewTable getTable(int provider) {
        ProviderServiceTopologyViewTable table;
        lock.lock();
        try {
            table = tables.get(provider);
            if (table == null) {
                table = new ProviderServiceTopologyViewTable();
                tables.put(provider, table);
            }
        } finally {
            lock.unlock();
        }
        return table;
    }

    public static class ProviderServiceTopologyViewTable {

        private Map<String, ServiceTopologyView> views = new ConcurrentHashMap<>();

        public ServiceTopologyView get(String serviceIdentity) {
            return views.get(serviceIdentity);
        }

        public void put(String serviceIdentity, ServiceTopologyView view) {
            views.put(serviceIdentity, view);
        }

    }

}
