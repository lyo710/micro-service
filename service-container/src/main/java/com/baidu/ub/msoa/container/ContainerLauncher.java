package com.baidu.ub.msoa.container;

import com.baidu.ub.msoa.container.support.JPAASEnvironment;
import com.baidu.ub.msoa.utils.jetty.EmbedJettyServer;

/**
 * Created by pippo on 15/8/20.
 */
public class ContainerLauncher {

    public static void main(String[] args) {
        String host = args != null && args.length >= 1 ? args[0] : "0.0.0.0";
        int port = args != null && args.length >= 2 ? Integer.parseInt(args[1]) : 8080;

        System.setProperty(JPAASEnvironment.SYSTEM_PROPERTY_HTTP_PORT_JPAAS2, port + "");

        final EmbedJettyServer embedJettyServer = new EmbedJettyServer();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            @Override
            public void run() {
                embedJettyServer.stop();
            }
        }));

        embedJettyServer.setHost(host);
        embedJettyServer.setPort(port);
        embedJettyServer.start();
    }

}
