package com.baidu.ub.msoa.container.support.rpc.outbound.event;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pippo on 15/7/20.
 */
public class ServiceQPSStatistics {

    private static ExecutorService CALC_EXECUTOR = Executors.newSingleThreadExecutor();

    public double qps = 0;
    private CalcTask calcTask = new CalcTask();
    private Deque<Long> deque = new LinkedList<>();

    public void count() {
        CALC_EXECUTOR.submit(calcTask);
    }

    public double get() {
        return qps;
    }

    @Override
    public String toString() {
        return Double.toString(qps);
    }

    private class CalcTask implements Runnable {

        @Override
        public void run() {
            deque.push(System.currentTimeMillis());

            if (deque.size() < 2) {
                return;
            }

            qps = (((double) deque.size()) / ((double) deque.getFirst() - (double) deque.getLast())) * 1000;

            while (deque.size() > 1000) {
                deque.removeLast();
            }
        }
    }

}
