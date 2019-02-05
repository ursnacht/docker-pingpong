package ch.glue.sendmail;

import java.util.Properties;

import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.glue.ping.RelayService;

@Path("sendmail")
public class SendmailResource {

	private String smtpHost = "172.17.0.1";
	private String smtpPort = "80";

	@Inject
	RelayService relayService;

	@GET
	@Path("{to}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response sendmail(@PathParam("to") String to) {
		String msg = relayService.buildMessage();
		StringBuilder result = new StringBuilder();
		result.append(msg);
		result.append("\n");
		result.append(sendMail(to, to, msg));
		result.append("\n");
		return Response.status(200).entity(result.toString()).build();
	}

	@GET
	@Path("{to}/{from}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response sendmail(@PathParam("to") String to, @PathParam("from") String from) {
		String msg = relayService.buildMessage();
		StringBuilder result = new StringBuilder();
		result.append(msg);
		result.append("\n");
		result.append(sendMail(to, from, msg));
		result.append("\n");
		return Response.status(200).entity(result.toString()).build();
	}

	private String sendMail(String to, String from, String msg) {
		String subject = "PingPong Test";

		Properties props = new Properties();
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", smtpPort);

		Session session = Session.getInstance(props);

		try {
			MimeMessage message = new MimeMessage(session);
			InternetAddress[] sender = new InternetAddress[1];
			sender[0] = new InternetAddress(from);
			message.addFrom(sender);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			message.setHeader("Content-Type", "text/plain");
			message.setSubject(subject, "UTF-8");

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(msg, "text/plain");
			MimeMultipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);
			Transport.send(message);
			return "Mail sent successfully to " + to;
		} catch (Exception e) {
			return "Could not send message: " + e.getMessage();
		}
	}

}
