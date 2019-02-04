
package ch.glue.ping;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.time.StopWatch;

@Stateless
public class RelayService {

	@Context
	private HttpServletRequest httpServletRequest;

	public Response relay(String address) {
		String msg = buildMessage();
		System.out.println("Calling: " + address);
		StopWatch sw = new StopWatch();
		sw.start();
		String result = doCall(address);
		sw.stop();
		return Response.status(200).entity(msg + "\nCalling: " + address + "\nResult: " + result + "\n" + sw + "\n")
				.build();
	}

	public String buildMessage() {
		String msg = String.format("Incoming request from %s --> %s\n", //
				httpServletRequest.getRemoteAddr(), //
				httpServletRequest.getLocalAddr());
		System.out.println(msg);
		return msg;
	}

	private String doCall(String address) {
		WebTarget target = ClientBuilder.newClient().target(address);
		return target.request().get(String.class);
	}
}
