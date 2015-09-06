package com.baidu.ub.msoa.governance.inbound.controller;

import com.baidu.ub.msoa.container.support.governance.domain.dto.ServiceRegisterRequest;
import com.baidu.ub.msoa.container.support.governance.domain.dto.ServiceRegisterResponse;
import com.baidu.ub.msoa.container.support.governance.domain.model.registry.RegisterInfo;
import com.baidu.ub.msoa.container.support.governance.domain.model.registry.ServiceInfo;
import com.baidu.ub.msoa.governance.service.register.ServiceRegistry;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by pippo on 15/8/4.
 */
@Controller
public class ServiceRegisterController {

    private static Logger logger = LoggerFactory.getLogger(ServiceRegisterController.class);

    @RequestMapping("register")
    @ResponseBody
    public ServiceRegisterResponse register(@RequestBody ServiceRegisterRequest request) {

        ServiceRegisterResponse response = new ServiceRegisterResponse();
        int provider = -1;
        String service = null;
        int version = -1;

        try {
            switch (request.getContactType()) {
                case proto:

                    ServiceInfo[] infos = serviceInfoAssembler.assemble(request);
                    logger.debug("to be register service info is:[{}]", infos);
                    response.setInfos(serviceRegistry.register(infos));

                    boolean flag = true;
                    /* 只要有一个方法有冲突,那么就认为注册失败 */
                    for (RegisterInfo info : response.getInfos()) {
                        ServiceInfo serviceInfo = info.getServiceInfo();
                        if (serviceInfo == null) {
                            flag = false;
                            break;
                        }

                        provider = serviceInfo.provider;
                        service = serviceInfo.service;
                        version = serviceInfo.version;

                        if (!info.isAccept()) {
                            flag = false;
                            break;
                        }
                    }
                    response.setSuccess(flag);

                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            logger.warn("do service register due to error", e);
            response.setError(Throwables.getStackTraceAsString(e));
            response.setSuccess(false);
        }

        if (response.isSuccess() && provider > 0 && service != null && version > 0) {
            serviceRegistry.saveSchema(provider, service, version, request.getContact());
        }

        return response;
    }

    @Resource
    private ServiceInfoAssembler serviceInfoAssembler;

    @Resource
    private ServiceRegistry serviceRegistry;
}
