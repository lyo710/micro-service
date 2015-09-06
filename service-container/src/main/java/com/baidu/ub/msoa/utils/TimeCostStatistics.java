package com.baidu.ub.msoa.utils;

/**
 * Created by pippo on 15/7/20.
 */
public class TimeCostStatistics implements RangeStatistics {

    private long count = 0;
    private double total = 0;
    private double cost = 0;

    public void cost(final double input) {
        CALC_EXECUTOR.submit(new Runnable() {

            @Override
            public void run() {
                count++;
                total += input;
                cost = total / (double) count;
            }
        });
    }

    @Override
    public double getStatistics() {
        return cost;
    }

    @Override
    public String toString() {
        return getStatistics() + "";
    }
}
