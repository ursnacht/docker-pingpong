package ch.glue.ping.boundary;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.time.StopWatch;

/**
 *
 * @author airhacks.com
 */
@Path("ping")
public class PingResource {

	@Context
	private HttpServletRequest httpServletRequest;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response ping() {
		String msg = String.format("Incoming ping from %s --> %s", httpServletRequest.getRemoteAddr(),
				httpServletRequest.getLocalAddr());
		System.out.println(msg);
		return Response.status(200).entity(msg).build();
	}

	@GET
	@Path("{uri}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response ping(@PathParam("uri") String uri) {
		String msg = String.format("Incoming ping from %s --> %s", httpServletRequest.getRemoteAddr(),
				httpServletRequest.getLocalAddr());
		System.out.println(msg);
		System.out.println("Calling " + uri);
		StopWatch sw = new StopWatch();
		sw.start();
		String result = doCall(uri);
		sw.stop();
		return Response.status(200).entity(msg + "\nCalling: " + uri + "\nResult: " + result + "\n" + sw).build();
	}

	public String doCall(String uri) {
		WebTarget target = ClientBuilder.newClient().target("http://" + uri);
		return target.path("/znueni/manage/ping").request().get(String.class);
	}

}
