package com.baidu.ub.msoa.governance.inbound.controller;

import com.baidu.ub.msoa.container.support.governance.contact.proto.MethodStubFileExtractorMain;
import com.baidu.ub.msoa.container.support.governance.contact.proto.ProtoBase;
import com.baidu.ub.msoa.container.support.governance.domain.dto.ServiceRegisterRequest;
import com.baidu.ub.msoa.container.support.governance.domain.model.registry.ProtoContact;
import com.baidu.ub.msoa.container.support.governance.domain.model.registry.ServiceInfo;
import com.baidu.ub.msoa.container.support.governance.domain.model.registry.ServiceSLA;
import com.baidu.ub.msoa.container.support.router.RouteStrategy;
import com.squareup.protoparser.ProtoFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by pippo on 15/8/7.
 */
@Component
public class ServiceInfoAssembler extends ProtoBase {

    private static Logger logger = LoggerFactory.getLogger(ServiceInfoAssembler.class);

    public ServiceInfo[] assemble(ServiceRegisterRequest request) throws Exception {
        logger.debug("assemble service info from request:[{}]", request);

        ProtoFile[] files = MethodStubFileExtractorMain.extract(request.getContact());

        Collection<ServiceInfo> infos = new ArrayList<>();
        for (ProtoFile file : files) {
            infos.add(assemble(request, file));
        }

        return infos.toArray(new ServiceInfo[0]);
    }

    private ServiceInfo assemble(ServiceRegisterRequest request, ProtoFile protoFile) {
        int provider = Integer.parseInt(getOptionValue(protoFile, "provider"));
        String service = getOptionValue(protoFile, "service");
        int version = Integer.parseInt(getOptionValue(protoFile, "version"));
        String method = getOptionByPrefix(protoFile, "return_").get(0).name().replace("return_", "");

        ServiceInfo info = new ServiceInfo(provider, service, version, method, RouteStrategy.switchover);
        info.setAcceptConsumers(request.getAcceptConsumers());
        info.setContact(new ProtoContact(protoFile.toSchema()));
        info.setAvailable(false);
        info.setSla(new ServiceSLA());
        info.getSla().setCurrency(request.getCurrency());
        info.getSla().setLatency(request.getLatency());
        info.getSla().setQps(request.getQps());

        return info;
    }

}
