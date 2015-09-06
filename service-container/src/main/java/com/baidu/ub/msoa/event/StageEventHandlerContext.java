package com.baidu.ub.msoa.event;

import com.baidu.ub.msoa.utils.thread.WorkStealingPool;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by pippo on 15/7/19.
 */
public class StageEventHandlerContext extends DefaultEventHandlerContext {

    private static StageStatusLoader statusLoader = new StageStatusLoader();
    private static ExecutorLoader executorLoader = new ExecutorLoader();

    public StageEventHandlerContext(EventHandlerStack stack) {
        super(stack);
    }

    @Override
    protected void doPassThrough(final Event event) {
        if (!(event instanceof StageEvent)) {
            super.doPassThrough(event);
            return;
        }

        String key = stageKey((StageEvent) event);
        if (Strings.isNullOrEmpty(key)) {
            super.doPassThrough(event);
            return;
        }

        StageStatus status = stageStatus(key);
        if (status.isolation()) {

            executorService(key).execute(new Runnable() {

                @Override
                public void run() {
                    StageEventHandlerContext.super.passThrough(event);
                }

            });

        } else {
            super.doPassThrough(event);
        }
    }

    private String stageKey(StageEvent event) {
        return String.format("%s.%s", event.getType(), event.getStage());
    }

    private StageStatus stageStatus(String key) {

        try {
            return stageStatus.get(key, statusLoader);
        } catch (ExecutionException e) {
            StageStatus status = new StageStatus();
            stageStatus.put(key, status);
            return status;
        }
    }

    private ExecutorService executorService(String key) {
        try {
            return stageExecutors.get(key, executorLoader);
        } catch (ExecutionException e) {
            ExecutorService executor = WorkStealingPool.create();
            stageExecutors.put(key, executor);
            return executor;
        }
    }

    private Cache<String, StageStatus> stageStatus = CacheBuilder.<String, StageStatus>newBuilder()
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .expireAfterWrite(3, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    private Cache<String, ExecutorService> stageExecutors = CacheBuilder.<String, ExecutorService>newBuilder()
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .expireAfterAccess(3, TimeUnit.MINUTES)
            .maximumSize(100)
            .removalListener(new RemovalListener<String, ExecutorService>() {

                @Override
                public void onRemoval(RemovalNotification<String, ExecutorService> notification) {
                    ExecutorService executor = notification.getValue();
                    if (executor != null) {
                        executor.shutdown();
                    }
                }
            })
            .build();

    private static class StageStatus {

        int cost = 0;
        int errorCount = 0;

        public boolean isolation() {
            // return errorCount > 5 || cost > 30000;
            return true;
        }
    }

    private static class StageStatusLoader implements Callable<StageStatus> {

        @Override
        public StageStatus call() throws Exception {
            return new StageStatus();
        }

    }

    private static class ExecutorLoader implements Callable<ExecutorService> {

        @Override
        public ExecutorService call() throws Exception {
            return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        }

    }

}
