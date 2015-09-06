package com.baidu.ub.msoa.utils.jetty;

import com.baidu.ub.msoa.utils.slf4j.UtilLoggerBridge;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.LowResourceMonitor;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pippo on 14-10-21.
 */
public class EmbedJettyServer {

    private static final Logger logger = LoggerFactory.getLogger(EmbedJettyServer.class);

    protected static Server server = null;

    public static Server getServer() {
        return server;
    }

    static {
        UtilLoggerBridge.bridge();
    }

    /**
     * 启动server
     */
    public void start() {
        if (server != null) {
            logger.warn("the jetty server:[{}] is exists, ignore start operation");
            return;
        }

        try {
            createServer();
            // createResourcesMonitor();
            createJmxSupport();
            createStatistics();

            if (server != null) {
                server.start();
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     * 停止server
     */
    public void stop() {
        if (server == null) {
            logger.debug("the jetty server not exists, ignore stop operation");
            return;
        }

        try {
            server.stop();
            server = null;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     * @return 是否已经启动
     */
    public boolean isOpen() {
        return server != null && server.isRunning();
    }

    protected void createServer() throws Exception {
        server = new Server(createThreadPool());
        server.setDumpAfterStart(false);
        server.setDumpBeforeStop(false);
        server.setStopAtShutdown(true);
        server.addConnector(createConnector());
        server.setHandler(createWebAppHandler());

         /*添加servlet3.0支持*/
        Configuration.ClassList
                .setServerDefault(server)
                .add(0, AnnotationConfiguration.class.getName());

        logger.debug("create jetty server");
    }

    protected QueuedThreadPool createThreadPool() {
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setName("jetty inbound thread pool");
        threadPool.setIdleTimeout(1000 * 60);

        if (maxThreads < minThreads) {
            maxThreads = minThreads;
        }

        threadPool.setMinThreads(minThreads);
        threadPool.setMaxThreads(maxThreads);
        logger.debug("create jetty thread pool:[{}]", threadPool);
        return threadPool;
    }

    protected Connector createConnector() {
        ServerConnector connector = new ServerConnector(server);
        connector.setHost(this.host);
        connector.setPort(this.port);

        logger.debug("create jetty connector:[{}]", connector);
        return connector;
    }

    protected HandlerList createWebAppHandler() {
        final HandlerList handlers = new HandlerList();

        defaultWebAppContext = createDefaultWebAppContext();
        if (defaultWebAppContext != null) {
            handlers.addHandler(defaultWebAppContext);
        }

        return handlers;
    }

    protected WebAppContext defaultWebAppContext = null;

    protected WebAppContext createDefaultWebAppContext() {
        defaultWebAppContext = createWebAppContext(defaultWebAppDir,
                defaultWebAppContextPath,
                new String[] { "index.html" });
        List<Resource> classesResources = new ArrayList<>();
        List<Resource> webInfJarResources = new ArrayList<Resource>();

        try {
            classesResources.add(Resource.newResource(EmbedJettyServer.class.getResource("/")));

            /*如果运行在嵌入式环境,依赖的lib会在webapp的classloader之前加载,那么不会作为当前webapp的资源被扫描*/
            /*所以此处主动扫描,并加入到当前webapp的WEB-INF/lib下*/
            Enumeration<URL> urls = ClassLoader.getSystemResources("META-INF/web-fragment.xml");
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                Resource resource = Resource.newResource(
                        url.getFile().replace("/META-INF/web-fragment.xml", "").replace("!", ""));

                if (resource.isDirectory()) {
                    classesResources.add(resource);
                } else {
                    webInfJarResources.add(resource);
                }
            }

            //            for (Resource resource : containerResources) {
            //                context.getMetaData().addContainerResource(resource);
            //                logger.debug("add resource:[{}] as container resource", resource);
            //            }

            defaultWebAppContext.getMetaData().setWebInfClassesDirs(classesResources);
            logger.debug("set resource:[{}] as classes resource", classesResources);

            for (Resource resource : webInfJarResources) {
                defaultWebAppContext.getMetaData().addWebInfJar(resource);
                logger.debug("add resource:[{}] as web info jar", resource);
            }

        } catch (IOException e) {
            logger.warn("retrieve servlet3 resource due to error", e);
        } finally {
            classesResources.clear();
            webInfJarResources.clear();
        }

        return defaultWebAppContext;
    }

    protected Map<String, WebAppContext> existContextPath = new HashMap<String, WebAppContext>();

    protected WebAppContext createWebAppContext(String dir, String contextPath, String[] welcomeFiles) {
        File webApp;
        if (EmbedJettyServer.class.getResource(dir) != null) {
            webApp = new File(EmbedJettyServer.class.getResource(dir).getFile());
        } else {
            webApp = new File(dir);
        }

        if (!webApp.exists()) {
            logger.warn("invalid webapp dir:[{}], ignore it", webApp);
            return null;
        }

        if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }

        if (existContextPath.containsKey(contextPath)) {
            logger.warn("conflict contextPath:[{}] mapping:[{}] and [{}], ignore mapping:[{}]",
                    contextPath,
                    existContextPath.get(contextPath),
                    webApp,
                    webApp);

            return existContextPath.get(contextPath);
        }

        WebAppContext context = new WebAppContext(webApp.getAbsolutePath(), contextPath);
        context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "true");
        context.setWelcomeFiles(welcomeFiles);

        if (!useSession) {
            context.setSessionHandler(new SessionHandler(new EmptySessionManager()));
        }

        existContextPath.put(contextPath, context);
        logger.info("create webapp:[{}] with contextPath:[{}]", webApp, contextPath);
        return context;
    }

    protected void createResourcesMonitor() {
        LowResourceMonitor lowResourcesMonitor = new LowResourceMonitor(server);
        lowResourcesMonitor.setPeriod(1000);
        lowResourcesMonitor.setLowResourcesIdleTimeout(200);
        lowResourcesMonitor.setMonitorThreads(true);
        lowResourcesMonitor.setMaxConnections(10240);
        lowResourcesMonitor.setMaxMemory((long) (Runtime.getRuntime().totalMemory() * 0.8));
        lowResourcesMonitor.setMaxLowResourcesTime(5000);
        server.addBean(lowResourcesMonitor);
    }

    protected void createJmxSupport() {
        MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());

        if (StringUtils.isBlank(jmxDomain)) {
            jmxDomain = String.format("jetty-%s", System.currentTimeMillis());
        }

        mbContainer.setDomain(jmxDomain);
        server.addBean(mbContainer);
    }

    protected void createStatistics() {
        StatisticsHandler stats = new StatisticsHandler();
        stats.setHandler(server.getHandler());
        server.setHandler(stats);
    }

    protected int minThreads = Math.max(4, Runtime.getRuntime().availableProcessors());
    protected int maxThreads = minThreads * 40 + 1;
    protected String host;
    protected int port;
    protected String jmxDomain;
    protected boolean useSession = false;
    protected String defaultWebAppDir = "/";
    protected String defaultWebAppContextPath = "/";
    protected String webAppScanPackage = "com.sirius";

    /**
     * @param minThreads
     */
    public void setMinThreads(int minThreads) {
        if (minThreads > 4) {
            this.minThreads = minThreads;
        }
    }

    /**
     * @param maxThreads
     */
    public void setMaxThreads(int maxThreads) {
        if (maxThreads > 4) {
            this.maxThreads = maxThreads;
        }
    }

    /**
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @param jmxDomain
     */
    public void setJmxDomain(String jmxDomain) {
        this.jmxDomain = jmxDomain;
    }

    /**
     * @param useSession
     */
    public void setUseSession(boolean useSession) {
        this.useSession = useSession;
    }

    /**
     * @param defaultWebAppDir
     */
    public void setDefaultWebAppDir(String defaultWebAppDir) {
        this.defaultWebAppDir = defaultWebAppDir;
    }

    /**
     * @param defaultWebAppContextPath
     */
    public void setDefaultWebAppContextPath(String defaultWebAppContextPath) {
        this.defaultWebAppContextPath = defaultWebAppContextPath;
    }

    /**
     * @param webAppScanPackage
     */
    public void setWebAppScanPackage(String webAppScanPackage) {
        this.webAppScanPackage = webAppScanPackage;
    }

    public static void main(String[] args) {
        EmbedJettyServer embedJettyServer = new EmbedJettyServer();
        embedJettyServer.setHost("127.0.0.1");
        embedJettyServer.setPort(8080);
        embedJettyServer.start();
    }
}
