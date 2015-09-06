package com.baidu.ub.msoa.governance.service.register.impl;

import com.baidu.ub.msoa.container.support.governance.contact.ContactConflictException;
import com.baidu.ub.msoa.container.support.governance.domain.model.registry.RegisterInfo;
import com.baidu.ub.msoa.container.support.governance.domain.model.registry.ServiceInfo;
import com.baidu.ub.msoa.governance.domain.repository.ServiceInfoRepository;
import com.baidu.ub.msoa.governance.domain.repository.ServiceSchemaRepository;
import com.baidu.ub.msoa.governance.service.register.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pippo on 15/8/6.
 */
@Service
public class ServiceRegistryImpl implements ServiceRegistry {

    private static Logger logger = LoggerFactory.getLogger(ServiceRegistryImpl.class);

    @Override
    public void saveSchema(int provider, String service, int version, String schema) {
        serviceSchemaRepository.save(provider, service, version, schema);
    }

    @Override
    public String getSchema(int provider, String service, int version) {
        return serviceSchemaRepository.get(provider, service, version);
    }

    @Override
    public RegisterInfo[] register(ServiceInfo... infos) throws Exception {
        List<RegisterInfo> registerInfos = new ArrayList<>();

        for (ServiceInfo info : infos) {
            registerInfos.add(register(info));
        }

        return registerInfos.toArray(new RegisterInfo[0]);
    }

    protected RegisterInfo register(ServiceInfo submit) {
        logger.debug("register submit service info:[{}]", submit);

        ServiceInfo source = serviceInfoRepository.get(submit.getProvider(), submit.serviceIdentity);
        logger.debug("exist service info is:[{}]", source);

        if (source == null) {
            submit.setAvailable(true);
            serviceInfoRepository.save(submit);

            RegisterInfo registerInfo = new RegisterInfo(source, true, null);
            logger.info("accept first submit service info:[{}]", submit);
            return registerInfo;
        }

        RegisterInfo registerInfo;
        try {
            source.isConflict(submit);
            source.setAcceptConsumers(submit.getAcceptConsumers());
            source.setContact(submit.getContact());
            source.setAvailable(true);
            serviceInfoRepository.update(source);

            registerInfo = new RegisterInfo(source, true, null);
            logger.info("accept no conflict service info:[{}]", submit);
        } catch (ContactConflictException e) {
            submit.setAvailable(false);
            registerInfo = new RegisterInfo(submit, false, e.getMessage());
            logger.info("reject conflict:[{}] service info:[{}]", e.getMessage(), submit);
        }

        return registerInfo;
    }

    @Resource
    private ServiceSchemaRepository serviceSchemaRepository;

    @Resource
    private ServiceInfoRepository serviceInfoRepository;

}
