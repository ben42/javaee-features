package ch.afterglowing.doit.business.monitoring.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.LongSummaryStatistics;

/**
 * Created by ben on 27.09.15.
 */
@Stateless
@Path("boundary-statistics")
public class BoundaryStatisticsResource {

    @Inject
    MonitoringSink monitoringSink;

    @GET
    public JsonObject getStatistics() {
        LongSummaryStatistics statistics = monitoringSink.getStatistics();
        return Json.createObjectBuilder()
                .add("invocation-count", statistics.getCount())
                .add("average-duration", statistics.getAverage())
                .add("min-duration", statistics.getMin())
                .add("max-duration", statistics.getMax()).build();
    }
}
