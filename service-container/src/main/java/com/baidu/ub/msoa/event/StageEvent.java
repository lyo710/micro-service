package com.baidu.ub.msoa.event;

/**
 * 可阶段处理的事件
 * Created by pippo on 15/9/5.
 */
public interface StageEvent<T> extends Event<T> {

    String getStage();

}
