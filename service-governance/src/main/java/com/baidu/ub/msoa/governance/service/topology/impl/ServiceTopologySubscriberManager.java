package com.baidu.ub.msoa.governance.service.topology.impl;

import com.baidu.ub.msoa.governance.domain.repository.ZKPathNameSpace;
import com.baidu.ub.msoa.pubusb.SubscriberManager;
import org.springframework.stereotype.Service;

/**
 * Created by pippo on 15/8/24.
 */
@Service("serviceTopologySubscriberManager")
public class ServiceTopologySubscriberManager extends SubscriberManager implements ZKPathNameSpace {

}
