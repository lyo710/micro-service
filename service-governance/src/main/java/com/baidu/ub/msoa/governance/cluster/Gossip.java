package com.baidu.ub.msoa.governance.cluster;

import org.jgroups.PhysicalAddress;
import org.jgroups.protocols.TP;
import org.jgroups.stack.GossipRouter;
import org.jgroups.util.AsciiString;

/**
 * Created by pippo on 15/7/18.
 */
public class Gossip extends TP {

    private GossipRouter router;

    @Override
    public boolean supportsMulticasting() {
        return false;
    }

    @Override
    public void sendMulticast(AsciiString clusterName, byte[] data, int offset, int length)
            throws Exception {

    }

    @Override
    public void sendUnicast(PhysicalAddress dest, byte[] data, int offset, int length) throws Exception {

    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    protected PhysicalAddress getPhysicalAddress() {
        return null;
    }
}
