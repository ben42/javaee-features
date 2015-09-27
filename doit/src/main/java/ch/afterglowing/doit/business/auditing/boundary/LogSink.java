package ch.afterglowing.doit.business.auditing.boundary;

/**
 * Created by ben on 27.09.15.
 */
@FunctionalInterface
public interface LogSink {
    void log(String message);
}
