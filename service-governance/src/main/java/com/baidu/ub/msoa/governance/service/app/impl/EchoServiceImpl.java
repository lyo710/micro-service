package com.baidu.ub.msoa.governance.service.app.impl;

import com.baidu.ub.msoa.governance.service.app.EchoService;
import org.springframework.stereotype.Service;

/**
 * Created by pippo on 15/9/5.
 */
@Service
public class EchoServiceImpl implements EchoService {

    @Override
    public String echo(String echo) {
        return echo;
    }

}
