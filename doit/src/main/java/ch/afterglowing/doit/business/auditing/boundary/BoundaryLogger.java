package ch.afterglowing.doit.business.auditing.boundary;

import ch.afterglowing.doit.business.monitoring.entity.CallEvent;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Created by ben on 27.09.15.
 */
public class BoundaryLogger {

    @Inject
    LogSink logger;

    @Inject
    Event<CallEvent> monitoring;

    @AroundInvoke
    public Object logCall(InvocationContext context) throws Exception {
        long start = System.currentTimeMillis();
        try {
            return context.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;
            monitoring.fire(new CallEvent(context.getMethod().getName(), duration));
        }
    }
}
