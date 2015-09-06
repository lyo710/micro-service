package com.baidu.ub.msoa.container.support.governance.discover;

import asia.stampy.server.message.message.MessageMessage;
import com.baidu.ub.msoa.container.support.governance.HttpClient;
import com.baidu.ub.msoa.container.support.governance.conf.Addresses;
import com.baidu.ub.msoa.container.support.governance.domain.BundleServiceNameSpace;
import com.baidu.ub.msoa.container.support.governance.domain.dto.ServiceTopologyRequest;
import com.baidu.ub.msoa.container.support.governance.domain.dto.ServiceTopologyResponse;
import com.baidu.ub.msoa.container.support.governance.domain.dto.stomp.BodyCodec;
import com.baidu.ub.msoa.container.support.governance.domain.model.BundleServiceMetaInfo;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.ServiceTopologyView;
import com.baidu.ub.msoa.pubusb.MessageProcessor;
import com.baidu.ub.msoa.pubusb.websocket.SubscribeClientEndpoint;
import com.baidu.ub.msoa.pubusb.websocket.WebSocketSubscriber;
import com.baidu.ub.msoa.utils.http.HttpRuntimeException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by pippo on 15/8/22.
 */
@Service("serviceEndpointDiscover")
public class RemoteServiceEndpointDiscover extends WebSocketSubscriber
        implements BundleServiceNameSpace, ServiceEndpointDiscover {

    protected static Logger logger = LoggerFactory.getLogger(RemoteServiceEndpointDiscover.class);
    protected static SubscribeClientEndpoint clientEndpoint = new SubscribeClientEndpoint();
    protected Cache<String, ServiceTopologyView> viewCache = CacheBuilder.<String, ServiceTopologyView>newBuilder()
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    @Resource(name = "msoa.governance.addresses.service.topology.subscribe")
    private Addresses topologySubscribeAddresses;

    //    @Resource
    //    private ServiceTopologyViewMemTable topologyViewTable;

    @Resource(name = "msoa.governance.client.service.topology.fetch")
    private HttpClient client;

    private ServiceDiscoverMessageProcessor processor = new ServiceDiscoverMessageProcessor();

    @PostConstruct
    @Override
    public void init() {
        super.addresses = topologySubscribeAddresses.urls;
        super.endpoint = clientEndpoint;
        super.init();
    }

    @Override
    public Endpoint select(BundleServiceMetaInfo metaInfo) {
        return select(metaInfo, SelectStrategy.random);
    }

    @Override
    public Endpoint select(BundleServiceMetaInfo metaInfo, SelectStrategy strategy) {

        ServiceTopologyView view = get(metaInfo);

        //        ServiceTopologyView view = topologyViewTable.get(metaInfo.provider, metaInfo.serviceIdentity);
        //
        //        if (view == null) {
        //            view = fetch(metaInfo);
        //        }

        if (view == null) {
            return null;
        } else {
            // topologyViewTable.save(metaInfo.provider, metaInfo.serviceIdentity, view);
            subscribe(metaInfo.serviceIdentity, processor);
        }

        return view.select(strategy);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Endpoint> list(BundleServiceMetaInfo metaInfo) {
        ServiceTopologyView view = get(metaInfo);

        if (view == null) {
            view = fetch(metaInfo);
        }

        return (Collection<Endpoint>) (view != null ? view.getEndpoints() : Collections.emptySet());
    }

    private ServiceTopologyView get(final BundleServiceMetaInfo metaInfo) {
        try {
            return viewCache.get(metaInfo.serviceIdentity, new Callable<ServiceTopologyView>() {

                @Override
                public ServiceTopologyView call() throws Exception {
                    return fetch(metaInfo);
                }

            });
        } catch (Exception e) {
            return null;
        }
    }

    private ServiceTopologyView fetch(BundleServiceMetaInfo metaInfo) {
        ServiceTopologyRequest request =
                new ServiceTopologyRequest(metaInfo.provider, metaInfo.service, metaInfo.version, metaInfo.method);

        ServiceTopologyResponse response = null;
        try {
            response = client.post(request, ServiceTopologyResponse.class);
        } catch (HttpRuntimeException e) {
            logger.warn("fetch service topology view due to error:[{}]", e.getMessage());
            return null;
        }

        if (!response.isSuccess()) {
            logger.warn("fetch topology:[{}] fail:[{}]", request, response.getError());
        }

        return response.isSuccess() ? response.getTopologyView() : null;
    }

    private class ServiceDiscoverMessageProcessor implements MessageProcessor {

        @Override
        public void process(String channel, MessageMessage message) {

            if ("topology".equals(message.getHeader().getDestination())) {
                ServiceTopologyView view =
                        BodyCodec.decodeBase64((String) message.getBody(), ServiceTopologyView.class);
                process(view);
            }

        }

        private void process(ServiceTopologyView view) {
            logger.info("#####:[{}]", view);
            //            topologyViewTable.save(view.provider, view.serviceIdentity, view);
            viewCache.put(view.serviceIdentity, view);
        }
    }

}
