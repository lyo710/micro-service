package com.baidu.ub.msoa.governance.service.topology.impl;

import com.baidu.ub.msoa.container.support.governance.contact.proto.annotaion.CollectionElementType;
import com.baidu.ub.msoa.container.support.governance.discover.annotation.BundleService;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.ServiceTopologyView;
import com.baidu.ub.msoa.governance.domain.repository.ServiceTopologyRepository;
import com.baidu.ub.msoa.governance.service.topology.ServiceTopologyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by pippo on 15/8/24.
 */
@BundleService(name = "serviceTopologyService", version = 1)
@Service
public class ServiceTopologyServiceImpl implements ServiceTopologyService {

    @Resource
    private ServiceTopologyRepository topologyRepository;

    @Override
    public ServiceTopologyView getView(int provider, String service, int version, String method) {
        return topologyRepository.getView(provider, service, version, method);
    }

    @CollectionElementType(String.class)
    @Override
    public List<Integer> getAllProvider() {
        return topologyRepository.getAllProvider();
    }

    @CollectionElementType(ServiceTopologyView.class)
    @Override
    public List<ServiceTopologyView> getAllServiceByProvider(int provider) {
        return topologyRepository.getAllServiceByProvider(provider);
    }
}
