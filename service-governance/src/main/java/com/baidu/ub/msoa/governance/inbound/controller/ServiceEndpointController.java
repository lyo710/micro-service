package com.baidu.ub.msoa.governance.inbound.controller;

import com.baidu.ub.msoa.container.support.governance.domain.dto.ServiceTopologyRequest;
import com.baidu.ub.msoa.container.support.governance.domain.dto.ServiceTopologyResponse;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.ServiceTopologyView;
import com.baidu.ub.msoa.governance.service.topology.ServiceTopologyService;
import com.google.common.base.Throwables;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by pippo on 15/8/22.
 */
@Controller
public class ServiceEndpointController {

    @Resource
    private ServiceTopologyService topologyService;

    @RequestMapping("topology/fetch")
    @ResponseBody
    public ServiceTopologyResponse view(@RequestBody ServiceTopologyRequest request) {

        ServiceTopologyView view = null;
        String error = null;
        try {
            view = topologyService.getView(request.getProvider(),
                    request.getService(),
                    request.getVersion(),
                    request.getMethod());
        } catch (Exception e) {
            error = Throwables.getStackTraceAsString(e);
        }

        ServiceTopologyResponse response = new ServiceTopologyResponse();
        response.setSuccess(view != null);
        response.setError(error);
        response.setTopologyView(view);
        return response;
    }

}
