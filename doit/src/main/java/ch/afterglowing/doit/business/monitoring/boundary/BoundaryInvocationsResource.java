package ch.afterglowing.doit.business.monitoring.boundary;

import ch.afterglowing.doit.business.monitoring.entity.CallEvent;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

/**
 * Created by ben on 27.09.15.
 */
@Stateless
@Path("boundary-invocations")
public class BoundaryInvocationsResource {

    @Inject
    MonitoringSink monitoringSink;

    @GET
    public List<CallEvent> expose() {
        return monitoringSink.getRecentEvents();
    }
}
