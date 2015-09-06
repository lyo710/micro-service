package com.baidu.ub.msoa.governance.inbound.controller.console;

import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.ServiceTopologyView;
import com.baidu.ub.msoa.governance.domain.dto.TopologyNode;
import com.baidu.ub.msoa.governance.domain.model.AppInfo;
import com.baidu.ub.msoa.governance.service.app.AppInfoService;
import com.baidu.ub.msoa.governance.service.topology.ServiceTopologyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by pippo on 15/8/30.
 */
@Controller
public class TopologyController {

    @Resource
    private ServiceTopologyService topologyService;

    @Resource
    private AppInfoService appInfoService;

    @RequestMapping("console/topology")
    public String topology() {
        return "topology";
    }

    @RequestMapping("console/topology/providers")
    @ResponseBody
    public TopologyNode providers() {
        List<TopologyNode> nodes = new ArrayList<>();

        List<Integer> providers = topologyService.getAllProvider();
        for (Integer provider : providers) {
            AppInfo appinfo = appInfoService.get(provider);

            TopologyNode node = new TopologyNode();
            node.setId(appinfo.getId() + "");
            node.setName(appinfo.getName());
            node.setData(appinfo);
            buildProviderChild(node);
            nodes.add(node);
        }

        TopologyNode root = new TopologyNode();
        root.setId("service-topology");
        root.setName("service-topology");
        root.setData(Collections.emptyMap());
        root.setChildren(nodes);
        return root;
    }

    private void buildProviderChild(TopologyNode node) {
        List<ServiceTopologyView> views = topologyService.getAllServiceByProvider(Integer.parseInt(node.getId()));

        Map<String, Map<Integer, List<ServiceTopologyView>>> viewByServiceName = new TreeMap<>();

        for (ServiceTopologyView view : views) {
            String service = view.getService();

            Map<Integer, List<ServiceTopologyView>> viewByVersion = viewByServiceName.get(service);
            if (viewByVersion == null) {
                viewByVersion = new TreeMap<>();
                viewByServiceName.put(service, viewByVersion);
            }

            int version = view.getVersion();
            List<ServiceTopologyView> serviceViews = viewByVersion.get(version);
            if (serviceViews == null) {
                serviceViews = new ArrayList<>();
                viewByVersion.put(version, serviceViews);
            }
            serviceViews.add(view);
        }

        for (String service : viewByServiceName.keySet()) {

            TopologyNode serviceNode = new TopologyNode();
            serviceNode.setId(service);
            serviceNode.setName(service);
            serviceNode.setData(Collections.emptyMap());
            node.getChildren().add(serviceNode);

            Map<Integer, List<ServiceTopologyView>> viewByVersion = viewByServiceName.get(service);
            for (Integer version : viewByVersion.keySet()) {

                TopologyNode versionNode = new TopologyNode();
                versionNode.setId("v_" + service + version);
                versionNode.setName("v_" + version);
                versionNode.setData(Collections.emptyMap());
                serviceNode.getChildren().add(versionNode);

                for (ServiceTopologyView view : viewByVersion.get(version)) {
                    TopologyNode methodNode = new TopologyNode();
                    methodNode.setId(view.method);
                    methodNode.setName(view.method);
                    methodNode.setData(view);
                    buildEndpointChild(methodNode);
                    versionNode.getChildren().add(methodNode);
                }
            }
        }
    }

    private void buildEndpointChild(TopologyNode node) {
        ServiceTopologyView view = (ServiceTopologyView) node.getData();
        for (Endpoint endpoint : view.getEndpoints()) {
            TopologyNode endpointNode = new TopologyNode();
            endpointNode.setId(endpoint.serviceIdentity + "#" + endpoint.getHost() + ":" + endpoint.getPort());
            endpointNode.setName(endpoint.getHost() + ":" + endpoint.getPort() + "#" + endpoint.getStatus());
            endpointNode.setData(endpoint);
            node.getChildren().add(endpointNode);
        }
    }
}
