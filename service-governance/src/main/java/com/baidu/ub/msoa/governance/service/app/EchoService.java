package com.baidu.ub.msoa.governance.service.app;

import com.baidu.ub.msoa.container.support.governance.discover.annotation.BundleService;

/**
 * Created by pippo on 15/9/5.
 */
@BundleService(name = "echoService", version = 1, interfaceType = EchoService.class)
public interface EchoService {

    String echo(String echo);

}
