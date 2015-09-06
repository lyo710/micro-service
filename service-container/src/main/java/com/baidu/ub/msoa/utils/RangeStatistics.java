package com.baidu.ub.msoa.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pippo on 15/7/20.
 */
public interface RangeStatistics {

    ExecutorService CALC_EXECUTOR = Executors.newSingleThreadExecutor();

    double getStatistics();

}
