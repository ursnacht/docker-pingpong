package ch.glue.ping.boundary;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.eclipse.microprofile.metrics.annotation.Metered;

import ch.glue.ping.entity.Znueni;

/**
 *
 * @author airhacks.com
 */
@Path("ping")
public class PingResource {

    @Inject
    ZnueniService service;

    @GET
    @Metered
    public Znueni ping() {
        return new Znueni("wurst und brezel " + service.message(), 15);
    }

}
