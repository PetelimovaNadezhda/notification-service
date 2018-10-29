import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class MailSender {
    private final String login;
    private final Session session;

    public MailSender(String login, String password) {
        this.login = login;

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(login, password);
            }
        };
        session = Session.getInstance(props, auth);
    }

    public void sendEmailNotification(Task task) throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = new MimeMessage(session);
        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");

        msg.setFrom(new InternetAddress(login, login));
        msg.setReplyTo(InternetAddress.parse(task.getExtraParams(), false));
        msg.setSubject("Notification", "UTF-8");
        msg.setText(task.getMessage(), "UTF-8");
        msg.setSentDate(new Date());

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(task.getMessage(), false));
        Transport.send(msg);
    }
}
