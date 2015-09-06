package com.baidu.ub.msoa.event;

/**
 * Created by pippo on 15/7/19.
 */
public class StageEventEngine extends EventEngine {

    @Override
    public void process(Event event) {
        EventHandlerContext context = new StageEventHandlerContext(stack);

        try {
            context.passThrough(event);
        } catch (Throwable throwable) {
            exceptionListener.onException(context, event, throwable);
        } finally {
            context.destroy();
        }
    }

}
