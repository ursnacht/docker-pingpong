
package ch.glue.ping.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;

/**
 *
 * @author airhacks.com
 */
@Stateless
public class ZnueniService {

	@Inject
	@ConfigProperty(name = "message", defaultValue = "not configured")
	String message;

	@Inject
	WurstMetric metric;

	@Fallback(fallbackMethod = "veggy")
	@Retry(maxRetries = 2)
	public String message() {
		System.out.println(".");
		this.metric.nextWurst();
		throw new NoWurstException("today is 21");
		// return "42";
	}

	public String veggy() {
		return "green stuff " + this.message;
	}

	public String call(String host) {
		WebTarget target = ClientBuilder.newClient().target(host);
		return target.path("resource/ping/{call}").resolveTemplate("call", "meine url").request().get(String.class);
	}

}
