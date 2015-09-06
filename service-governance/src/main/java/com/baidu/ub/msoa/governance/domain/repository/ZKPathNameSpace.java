package com.baidu.ub.msoa.governance.domain.repository;

/**
 * Created by pippo on 15/8/24.
 */
public interface ZKPathNameSpace {

    class NameSpace {

        public static final String SERVER_INFO_ROOT = "/server_info";
        public static final String SERVICE_INFO_ROOT = "/service_info";
        public static final String SERVICE_TOPOLOGY_ROOT = "/service_topology";

        public static String serverInfo(String host) {
            return String.format("%s/%s", SERVER_INFO_ROOT, host);
        }

        public static String serverInfo(String ip, int port) {
            return String.format("%s/%s#%s", SERVER_INFO_ROOT, ip, port);
        }

        public static String serviceInfo(int provider, String serviceIdentity) {
            return String.format("%s/%s/%s", SERVICE_INFO_ROOT, provider, serviceIdentity);
        }

        public static String providerTopology(int provider) {
            return String.format("%s/%s", SERVICE_TOPOLOGY_ROOT, provider);
        }

        public static String serviceTopology(int provider, String serviceIdentity) {
            return String.format("%s/%s/%s", SERVICE_TOPOLOGY_ROOT, provider, serviceIdentity);
        }

        public static String serviceTopology(String provider, String serviceIdentity) {
            return String.format("%s/%s/%s", SERVICE_TOPOLOGY_ROOT, provider, serviceIdentity);
        }

        public static String serviceEndpoints(int provider, String serviceIdentity) {
            return String.format("%s/endpoints", serviceTopology(provider, serviceIdentity));
        }

        public static String serviceEndpoint(int provider, String serviceIdentity, String host) {
            return String.format("%s/endpoints/%s", serviceTopology(provider, serviceIdentity), host);
        }

        public static String serviceEndpoint(String provider, String serviceIdentity, String host) {
            return String.format("%s/endpoints/%s", serviceTopology(provider, serviceIdentity), host);
        }

        public static String serviceEndpoint(int provider, String serviceIdentity, String ip, int port) {
            return String.format("%s/endpoints/%s#%s", serviceTopology(provider, serviceIdentity), ip, port);
        }

        public static String serviceEndpointStatus(int provider, String serviceIdentity, String host) {
            return String.format("%s/status", serviceEndpoint(provider, serviceIdentity, host));
        }

        public static String serviceEndpointStatus(String provider, String serviceIdentity, String host) {
            return String.format("%s/status", serviceEndpoint(provider, serviceIdentity, host));
        }

        public static String serviceEndpointStatus(int provider, String serviceIdentity, String ip, int port) {
            return String.format("%s/status", serviceEndpoint(provider, serviceIdentity, ip, port));
        }

        public static String serviceEndpointStatistics(int provider, String serviceIdentity, String ip, int port) {
            return String.format("%s/statistics", serviceEndpoint(provider, serviceIdentity, ip, port));
        }
    }

}
