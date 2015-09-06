package com.baidu.ub.msoa.example.test.disover;

import com.baidu.ub.msoa.container.support.governance.discover.RemoteServiceEndpointDiscover;
import com.baidu.ub.msoa.container.support.governance.domain.model.BundleServiceMetaInfo;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint;
import com.baidu.ub.msoa.container.support.router.RouteStrategy;
import com.baidu.ub.msoa.container.test.BundleIntegrationTest;
import com.baidu.ub.msoa.container.test.BundleTestConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by pippo on 15/8/24.
 */
@ActiveProfiles(BundleTestConstants.PROFILE)
@BundleIntegrationTest(bundle = "room-service")
@RunWith(SpringJUnit4ClassRunner.class)
public class RemoteEndpointDiscoverTest {

    @Resource
    private RemoteServiceEndpointDiscover endpointDiscover;

    @Test
    public void discover() {
        BundleServiceMetaInfo metaInfo =
                new BundleServiceMetaInfo(1, "roomService", 1, "create", RouteStrategy.switchover);
        Endpoint endpoint = endpointDiscover.select(metaInfo);
        Assert.assertNotNull(endpoint);
    }

}
