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

/**
 *
 * @author airhacks.com
 */
@Path("call")
public class CallResource {

	@Context
	private HttpServletRequest httpServletRequest;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response call() {
		String msg = String.format("Incoming call from %s --> %s", httpServletRequest.getRemoteAddr(),
				httpServletRequest.getLocalAddr());
		System.out.println(msg);
		return Response.status(200).entity(msg).build();
	}

	@GET
	@Path("{uri}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response call(@PathParam("uri") String uri) {
		String msg = String.format("Incoming call from %s --> %s", httpServletRequest.getRemoteAddr(),
				httpServletRequest.getLocalAddr());
		System.out.println(msg);
		System.out.println("Calling " + uri);
		String result = doCall(uri);
		return Response.status(200).entity(msg + "\nCalling: " + uri + "\nResult: " + result).build();
	}

	public String doCall(String uri) {
		WebTarget target = ClientBuilder.newClient().target(uri);
		return target.request().get(String.class);
	}

}
