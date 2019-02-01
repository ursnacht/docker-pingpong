
package ch.glue.ping.boundary;

import javax.ejb.ApplicationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 * @author airhacks.com
 */
@ApplicationException(rollback = false)
public class NoWurstException extends WebApplicationException {

    NoWurstException(String message) {
        super(Response.status(204).header("reason", "wurst is out").build());
    }

}
