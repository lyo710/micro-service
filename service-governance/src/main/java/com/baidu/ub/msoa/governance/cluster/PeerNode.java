package com.baidu.ub.msoa.governance.cluster;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.protocols.TCP;
import org.jgroups.protocols.TCPGOSSIP;
import org.jgroups.stack.GossipRouter;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.LogManager;

/**
 * Created by pippo on 15/7/16.
 */
public class PeerNode {

    static ExecutorService executorService = Executors.newCachedThreadPool();

    static {
        // Jersey uses java.util.logging - bridge to slf4
        java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            rootLogger.removeHandler(handlers[i]);
        }
        SLF4JBridgeHandler.install();
    }

    public PeerNode() {
        //            UDP udp = new UDP();
        //            //udp.setBindAddress(InetAddress.getByName("127.0.0.1"));
        //            udp.setBindToAllInterfaces(true);
        //
        //            channel = new JChannel(false);
        //            channel.setName(name);
        //            channel.setProtocolStack(new ProtocolStack());
        //
        //
        //
        //            channel.getProtocolStack().addProtocol(udp).addProtocol(new PING())
        //                    .addProtocol(new MERGE3())
        //                    .addProtocol(new FD_SOCK())
        //                    .addProtocol(new FD_ALL().setValue("timeout", 12000)
        //                            .setValue("interval", 3000))
        //                    .addProtocol(new VERIFY_SUSPECT())
        //                    .addProtocol(new BARRIER())
        //                    .addProtocol(new NAKACK2())
        //                    .addProtocol(new UNICAST3())
        //                    .addProtocol(new STABLE())
        //                    .addProtocol(new GMS())
        //                    .addProtocol(new UFC())
        //                    .addProtocol(new MFC())
        //                    .addProtocol(new FRAG2());
        //            channel.getProtocolStack().init();
    }

    public PeerNode(String name) {
        this.name = name;
    }

    private String name;
    private JChannel channel;

    public void start() throws Exception {
        if (channel != null) {
            return;
        }

        channel = new JChannel(new File(PeerNode.class.getResource("/META-INF/tcp.xml").getFile()));
        channel.setName(name);
        channel.setReceiver(new ReceiverAdapter() {

            @Override
            public void receive(Message msg) {
                //                    System.out.println("receive#" + name + "#" + msg.toStringAsObject());
            }
        });

        channel.connect("default");
        System.out.println("start node:" + name + "@@" + channel.getState());
    }

    public void stop() {
        if (channel == null) {
            return;
        }

        System.out.println("stop node:" + name);

        try {
            channel.close();
        } finally {
            channel = null;
        }
    }

    public boolean isRuning() {
        return channel != null;
    }

    public static void main(String[] args) throws Exception {

        final GossipRouter gossipRouter1 = new GossipRouter(12001);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    gossipRouter1.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).get();

        final GossipRouter gossipRouter2 = new GossipRouter(12002);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    gossipRouter2.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).get();

        List<PeerNode> nodes = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String name = "peer-node-" + i;
            PeerNode node = new PeerNode(name);
            node.start();

            nodes.add(node);
            // executorService.submit(node).getDefault();
        }

        Thread.sleep(1000 * 10);

        for (PeerNode node : nodes) {
            System.out.println(node.name + "#" + node.channel.getView());
        }

        System.out.println("gossip1.address.mapping#" + gossipRouter1.dumpAddresssMappings());
        System.out.println("gossip1.address.routing.table.detail#" + gossipRouter1.dumpRoutingTableDetailed());

        System.out.println("gossip2.address.mapping#" + gossipRouter2.dumpAddresssMappings());
        System.out.println("gossip2.address.routing.table.detail#" + gossipRouter2.dumpRoutingTableDetailed());

        while (true) {

            System.out.println("@@@@@@@@@@@@@@@@@@@@");
            int shutdown = new Random().nextInt(nodes.size());
            int index = 0;
            for (PeerNode node : nodes) {

                if (!node.isRuning()) {
                    node.start();
                }

                node.channel.send(null, String.format("from:[%s]", node.name));

                TCP tcp = (TCP) node.channel.getProtocolStack().findProtocol(TCP.class);
                if (tcp != null) {
                    System.out.println("####" + node.name + "###" + tcp.printConnections());
                }

                TCPGOSSIP tcpgossip = (TCPGOSSIP) node.channel.getProtocolStack().findProtocol(TCPGOSSIP.class);
                if (tcpgossip != null) {
                    //                    System.out.println(tcpgossip.dumpStats());
                    //                List<RouterStub> stubs = tcpgossip.getStubManager().getStubs();
                    //                for (RouterStub stub : stubs) {
                    //                    System.out.println("   " + node.name + "@" + stub);
                    //                }
                }

                System.out.println(node.channel.getView());

                if (index == shutdown) {
                    node.stop();
                }

                index++;

            }

            Thread.sleep(1000 * 10);

        }
    }
}
