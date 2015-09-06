package com.baidu.ub.msoa.event;

/**
 * Created by pippo on 15/7/20.
 */
public interface EventCallback<E extends Event> {

    /**
     * event process before invocation callback
     *
     * @param event
     */
    void preInvoke(E event);

    /**
     * event process success callback
     *
     * @param event
     */
    void onSuccess(E event);

    /**
     * event process fail callback
     *
     * @param event
     * @param throwable
     */
    void onFail(E event, Throwable throwable);

}
