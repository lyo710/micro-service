package com.baidu.ub.msoa.event;

import com.baidu.ub.msoa.utils.thread.WorkStealingPool;

import java.util.concurrent.ExecutorService;

/**
 * Created by pippo on 15/7/19.
 */
public class EventEngine {

    // private static Logger logger = LoggerFactory.getLogger(EventEngine.class);
    protected static ExecutorService executor = WorkStealingPool.create();

    /**
     * sync process event
     *
     * @param event
     */
    public void process(Event event) {
        EventHandlerContext context = new DefaultEventHandlerContext(stack);

        try {
            context.passThrough(event);
        } catch (Throwable throwable) {
            exceptionListener.onException(context, event, throwable);
        } finally {
            context.destroy();
        }
    }

    /**
     * async process event
     *
     * @param event
     * @param callback
     */
    @SuppressWarnings("unchecked")
    public void process(final Event event, final EventCallback callback) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    callback.preInvoke(event);
                    process(event);
                    callback.onSuccess(event);
                } catch (Exception e) {
                    callback.onFail(event, e);
                }
            }
        });

    }

    protected EventHandlerStack stack = new EventHandlerStack();
    protected EventEngineExceptionListener exceptionListener = new DefaultEventEngineExceptionListener();

    /**
     * @return event process handler stack
     */
    public EventHandlerStack getStack() {
        return stack;
    }

    /**
     * set event process handler stack
     *
     * @param stack
     */
    public void setStack(EventHandlerStack stack) {
        this.stack = stack;
    }

    /**
     * @return event process exception listener
     */
    public EventEngineExceptionListener getExceptionListener() {
        return exceptionListener;
    }

    /**
     * set event process exception listener
     *
     * @param exceptionListener
     */
    public void setExceptionListener(EventEngineExceptionListener exceptionListener) {
        this.exceptionListener = exceptionListener;
    }

    public static class DefaultEventEngineExceptionListener implements EventEngineExceptionListener {

        @Override
        public void onException(EventHandlerContext context, Event event, Throwable t) {
            //            logger.error("handler:[{}] due to error:[{}],the event is:[{}]",
            //                    context.getInterruptHandler().getName(),
            //                    Throwables.getStackTraceAsString(t),
            //                    event);
            throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
        }
    }

}
