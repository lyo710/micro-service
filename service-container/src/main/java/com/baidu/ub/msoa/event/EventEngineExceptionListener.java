package com.baidu.ub.msoa.event;

/**
 * Created by pippo on 15/7/19.
 */
public interface EventEngineExceptionListener {

    /**
     * listen event process exception
     *
     * @param context
     * @param event
     * @param throwable
     */
    void onException(EventHandlerContext context, Event event, Throwable throwable);

}
