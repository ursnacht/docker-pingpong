
package ch.glue.ping.boundary;

import java.util.concurrent.atomic.LongAdder;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.metrics.annotation.Gauge;

/**
 *
 * @author airhacks.com
 */
@ApplicationScoped
public class WurstMetric {

    private LongAdder wurstCounter;

    @PostConstruct
    public void init() {
        this.wurstCounter = new LongAdder();
    }

    public void nextWurst() {
        this.wurstCounter.increment();
    }

    @Gauge(unit = "count")
    public int getWurstCount() {
        return this.wurstCounter.intValue();
    }


}
