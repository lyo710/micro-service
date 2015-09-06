package com.baidu.ub.msoa.container.support.router;

import com.baidu.ub.msoa.container.support.router.event.EventType;
import com.baidu.ub.msoa.container.support.router.event.RouteEvent;
import com.baidu.ub.msoa.event.Event;
import com.baidu.ub.msoa.event.EventHandlerAdapter;
import com.baidu.ub.msoa.event.EventHandlerContext;
import com.baidu.ub.msoa.utils.MBeanHelper;
import com.baidu.ub.msoa.utils.QPSStatistics;
import com.baidu.ub.msoa.utils.TimeCostStatistics;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pippo on 15/7/1.
 */
@Component
public class RouteStatistics extends EventHandlerAdapter<RouteEvent> {

    private static String STATISTICS_SERVICE_START = "statistics.service.start";
    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    private static Map<String, QPSStatistics> qps_m = new ConcurrentHashMap<>();
    private static Map<String, TimeCostStatistics> time_cost_m = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        MBeanHelper.registerMBean(RouteStatistics.class.getName(), new MBean(), MBeanInterface.class);
    }

    @Override
    public boolean accept(Event<?> event) {
        return event.getType() == EventType.ROUTE.ordinal();
    }

    @Override
    public void downstream(final EventHandlerContext context, final RouteEvent event) throws Throwable {
        context.setAttribute(STATISTICS_SERVICE_START, System.nanoTime());
        executor.submit(new Runnable() {
            @Override
            public void run() {
                getQPSStatistics(event.message.meta.serviceIdentity).query();
            }
        });
        context.passThrough(event);
    }

    @Override
    public void upstream(final EventHandlerContext context, final RouteEvent event) throws Throwable {
        final long end = System.nanoTime();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                long start = context.getAttribute(STATISTICS_SERVICE_START);
                double cost = ((double) (end - start)) / 1000000;
                getTimeCostStatistics(event.message.meta.serviceIdentity).cost(cost);
            }
        });
    }

    /**
     * getDefault QPSStatistics with serviceIdentity
     *
     * @param rpcIdentity
     * @return QPSStatistics
     */
    public static QPSStatistics getQPSStatistics(String rpcIdentity) {
        QPSStatistics statistics = qps_m.get(rpcIdentity);
        if (statistics == null) {
            statistics = new QPSStatistics();
            qps_m.put(rpcIdentity, statistics);
        }
        return statistics;
    }

    /**
     * getDefault TimeCostStatistics with serviceIdentity
     *
     * @param rpcIdentity
     * @return TimeCostStatistics
     */
    public static TimeCostStatistics getTimeCostStatistics(String rpcIdentity) {
        TimeCostStatistics statistics = time_cost_m.get(rpcIdentity);
        if (statistics == null) {
            statistics = new TimeCostStatistics();
            time_cost_m.put(rpcIdentity, statistics);
        }
        return statistics;
    }

    public interface MBeanInterface {

        /**
         * @return all rpc qps statistics
         */
        List<String> getQPS();

        /**
         * @return all rpc cost statistics
         */
        List<String> getCost();

    }

    public class MBean implements MBeanInterface {

        @Override
        public List<String> getQPS() {
            List<String> lines = new ArrayList<>();
            for (String name : qps_m.keySet()) {
                lines.add(String.format("%s=%s", name, qps_m.get(name)));
            }
            return lines;
        }

        @Override
        public List<String> getCost() {
            List<String> lines = new ArrayList<>();
            for (String name : time_cost_m.keySet()) {
                lines.add(String.format("%s=%s", name, time_cost_m.get(name)));
            }
            return lines;
        }
    }

}
