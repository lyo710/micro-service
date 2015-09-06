package com.baidu.ub.msoa.utils.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/**
 * jdk的Executors.newWorkStealingPool要1.8才有,但其实调用的是1.7已有代码
 * Created by pippo on 15/9/5.
 */
public class WorkStealingPool {

    public static ExecutorService create() {
        return new ForkJoinPool(Runtime.getRuntime().availableProcessors(),
                ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                null,
                true);
    }

    public static ExecutorService create(int parallelism) {
        return new ForkJoinPool(parallelism, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
    }
}
