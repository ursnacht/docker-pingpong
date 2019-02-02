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
@Path("ping")
public class PingResource {

	@Inject
	RelayService relayService;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response ping() {
		String msg = relayService.buildMessage();
		return Response.status(200).entity(msg).build();
	}

	@GET
	@Path("{hostAndPort}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response ping(@PathParam("hostAndPort") String hostAndPort) {
		String uri = "http://" + hostAndPort + "/znueni/manage/ping";
		return relayService.relay(uri);
	}
}
