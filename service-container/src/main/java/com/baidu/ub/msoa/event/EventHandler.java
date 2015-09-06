package com.baidu.ub.msoa.event;

/**
 * Created by pippo on 15/7/19.
 */
public interface EventHandler<E extends Event<?>> {

    /**
     * @return event name
     */
    String getName();

    boolean accept(Event<?> event);

    /**
     * downstream process
     * invoke context.passThough will fire next handler.downstream,else fire upstream stack from this handler
     *
     * @param context
     * @param event
     * @throws Throwable
     */
    void downstream(EventHandlerContext context, E event) throws Throwable;

    /**
     * upstream process
     *
     * @param context
     * @param event
     * @throws Throwable
     */
    void upstream(EventHandlerContext context, E event) throws Throwable;

    /**
     * process on exception
     *
     * @param context
     * @param event
     */
    void onException(EventHandlerContext context, E event, Throwable throwable);

}
