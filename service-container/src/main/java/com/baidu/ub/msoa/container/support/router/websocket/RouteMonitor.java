package com.baidu.ub.msoa.container.support.router.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by pippo on 15/7/21.
 */
public class RouteMonitor {

    private static Map<String, RouteFuture> futures = new ConcurrentHashMap<>();

    /**
     * create RouteFuture
     *
     * @param routeId
     * @return RouteFuture
     */
    public static RouteFuture createRouteFuture(String routeId) {
        RouteFuture future = new RouteFuture();
        futures.put(routeId, future);
        return future;
    }

    /**
     * getDefault RouteFuture
     *
     * @param routeId
     * @return RouteFuture
     */
    public static RouteFuture getRouteFuture(String routeId) {
        RouteFuture future = futures.get(routeId);

        //            long start = System.currentTimeMillis();
        //            while (future == null && (System.currentTimeMillis() - start < 10)) {
        //                future = endpoint.context.getDefault(eventId);
        //            }

        return future;
    }

    /**
     * future for async route endpoint
     */
    public static class RouteFuture implements Future<Packet> {

        private Semaphore semaphore = new Semaphore(0);

        public Packet packet;
        public Throwable throwable;

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            semaphore.release();
            return true;
        }

        @Override
        public boolean isCancelled() {
            return semaphore.availablePermits() == 0;
        }

        @Override
        public boolean isDone() {
            return semaphore.availablePermits() == 0;
        }

        @Override
        public Packet get() throws InterruptedException, ExecutionException {
            semaphore.tryAcquire();
            return packet;
        }

        @Override
        public Packet get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException {
            semaphore.tryAcquire(timeout, unit);
            return packet;
        }
    }

}
