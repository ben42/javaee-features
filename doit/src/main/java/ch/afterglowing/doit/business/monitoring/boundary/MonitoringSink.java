package ch.afterglowing.doit.business.monitoring.boundary;

import ch.afterglowing.doit.business.auditing.boundary.LogSink;
import ch.afterglowing.doit.business.monitoring.entity.CallEvent;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created by ben on 27.09.15.
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class MonitoringSink {

    @Inject
    LogSink log;

    // Using this kind of list because the access may be multithreaded
    CopyOnWriteArrayList<CallEvent> recentEvents;

    @PostConstruct
    public void init() {
        recentEvents = new CopyOnWriteArrayList<>();
    }

    public void onCallEvent(@Observes CallEvent event) {
        recentEvents.add(event);
        log.log(event.toString());
    }

    public List<CallEvent> getRecentEvents() {
        return recentEvents;
    }

    public LongSummaryStatistics getStatistics() {
        return recentEvents.stream().collect(Collectors.summarizingLong(CallEvent::getDuration));
    }
}
