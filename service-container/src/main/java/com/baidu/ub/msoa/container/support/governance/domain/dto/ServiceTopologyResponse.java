package com.baidu.ub.msoa.container.support.governance.domain.dto;

import com.baidu.ub.msoa.container.support.governance.domain.model.topology.ServiceTopologyView;

/**
 * Created by pippo on 15/8/23.
 */
public class ServiceTopologyResponse extends BaseResponse {

    private ServiceTopologyView topologyView;

    public ServiceTopologyView getTopologyView() {
        return topologyView;
    }

    public void setTopologyView(ServiceTopologyView topologyView) {
        this.topologyView = topologyView;
    }

    @Override
    public String toString() {
        return String.format("ServiceTopologyResponse{'super'=%s, 'topologyView'=%s}", super.toString(), topologyView);
    }
}
