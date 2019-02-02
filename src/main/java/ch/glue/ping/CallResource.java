package ch.glue.ping;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author airhacks.com
 */
@Path("call")
public class CallResource {

	@Inject
	RelayService relayService;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response call() {
		String msg = relayService.buildMessage();
		return Response.status(200).entity(msg).build();
	}

	@GET
	@Path("{uri}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response call(@PathParam("uri") String uri) {
		return relayService.relay(uri);
	}
}
