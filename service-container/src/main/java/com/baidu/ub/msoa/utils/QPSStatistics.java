package com.baidu.ub.msoa.utils;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by pippo on 15/7/20.
 */
public class QPSStatistics implements RangeStatistics {

    private CalcTask calcTask = new CalcTask();
    private Deque<Long> deque = new LinkedList<>();
    private double qps = 0;

    public void query() {
        CALC_EXECUTOR.submit(calcTask);
    }

    @Override
    public double getStatistics() {
        return qps;
    }

    @Override
    public String toString() {
        return getStatistics() + "";
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
