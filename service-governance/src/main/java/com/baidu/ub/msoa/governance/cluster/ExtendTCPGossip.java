package com.baidu.ub.msoa.governance.cluster;

import org.jgroups.protocols.TCPGOSSIP;

/**
 * Created by pippo on 15/7/17.
 */
public class ExtendTCPGossip extends TCPGOSSIP {

    @Override public void destroy() {
        super.destroy();
    }
}
