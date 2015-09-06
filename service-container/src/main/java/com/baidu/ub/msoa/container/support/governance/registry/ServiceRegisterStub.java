package com.baidu.ub.msoa.container.support.governance.registry;

import com.baidu.ub.msoa.container.support.governance.HttpClient;
import com.baidu.ub.msoa.container.support.governance.domain.dto.ServiceRegisterRequest;
import com.baidu.ub.msoa.container.support.governance.domain.dto.ServiceRegisterResponse;
import com.baidu.ub.msoa.container.support.governance.domain.dto.spring.ServiceRegisterAcceptEvent;
import com.baidu.ub.msoa.container.support.governance.domain.model.registry.RegisterInfo;
import com.google.common.base.Throwables;
import com.google.common.base.Verify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by pippo on 15/8/18.
 */
@Service("serviceRegisterStub")
public class ServiceRegisterStub {

    private static Logger logger = LoggerFactory.getLogger(ServiceRegisterStub.class);
    private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private Set<ServiceRegisterRequest> failRequest = new HashSet<>();
    private Set<RegisterInfo> accept = new HashSet<>();
    private Set<RegisterInfo> reject = new HashSet<>();

    @Resource(name = "msoa.governance.client.service.register")
    private HttpClient client;

    @Resource
    private ApplicationEventPublisher eventPublisher;

    @PostConstruct
    public void init() {
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //                System.out.println("start@@@@@");
                Set<ServiceRegisterRequest> success = new HashSet<>();

                try {
                    for (ServiceRegisterRequest request : failRequest) {

                        ServiceRegisterResponse response = doRegister(request);
                        if (response.isSuccess()) {
                            success.add(request);
                        }
                    }

                    for (ServiceRegisterRequest request : success) {
                        failRequest.remove(request);
                    }

                    if (failRequest.isEmpty()) {
                        executorService.shutdown();
                    }
                } catch (Throwable e) {
                    logger.warn("register service info due to error", e);
                }

                //                System.out.println("finish@@@@@");
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    /**
     * 向服务治理中心注册服务信息
     *
     * @param request
     */
    public void register(final ServiceRegisterRequest request) {
        try {
            doRegister(request);
        } catch (Exception e) {
            logger.error("register service info:[{}] due to error:[{}]", request, Throwables.getStackTraceAsString(e));
        }
    }

    private ServiceRegisterResponse doRegister(ServiceRegisterRequest request) {
        logger.info("try register request:[{}]", request);
        ServiceRegisterResponse response;

        try {
            response = client.post(request, ServiceRegisterResponse.class);
        } catch (Exception e) {
            response = new ServiceRegisterResponse();
            response.setSuccess(false);
            response.setError(e.getMessage());
        }

        if (!response.isSuccess()) {
            failRequest.add(request);
            logger.error("register request due to error:[{}], will try later", response.getError());
            return response;
        }

        for (RegisterInfo info : response.getInfos()) {
            Verify.verifyNotNull(info.getServiceInfo(), "service info can not be null");

            if (info.isAccept()) {
                accept.add(info);
                reject.remove(info);
                logger.info("register service info is accept:[{}]", info.getServiceInfo().serviceIdentity);
                eventPublisher.publishEvent(new ServiceRegisterAcceptEvent(info.getServiceInfo()));
            } else {
                accept.remove(info);
                reject.add(info);
                logger.info("register service info is reject:[{}] the error is:[{}]",
                        info.getServiceInfo().serviceIdentity,
                        info.getError());
            }
        }

        return response;
    }

}
