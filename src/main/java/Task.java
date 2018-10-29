import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TimerTask;

public class Task extends TimerTask {
    private final String message;
    private final String extraParams;
    private LocalTime time;
    private MailSender mailSender = new MailSender("", "");
    private String externalId;
    private NotificationTaskType notificationType;
    Task(String externalId, NotificationTaskType notificationType, String message, LocalTime time, String extraParams) {
        this.externalId = externalId;
        this.notificationType = notificationType;
        this.message = message;
        this.time = time;
        this.extraParams = extraParams;
    }

    public String getMessage() {
        return message;
    }

    public String getExtraParams() {
        return extraParams;
    }

    public String getExternalId() {
        return externalId;
    }

    @Override
    public void run() {
        System.out.println("Running task " + externalId + " '" + message + "' via" + notificationType +
                " at " + time.format(DateTimeFormatter.ISO_TIME));
        switch (notificationType) {
            case URL:
                try {
                    mailSender.sendEmailNotification(this);
                } catch (MessagingException | UnsupportedEncodingException e) {
                    System.err.println("Error sending email notification, reason: " + e.getMessage());
                }
                break;
            case MAIL:
                try {
                    UrlService.sendGetNotification(this);
                } catch (IOException e) {
                    System.err.println("Error sending GEY notification, reason: " + e.getMessage());
                }
                break;
        }
    }

    enum NotificationTaskType {
        MAIL("email"),
        URL("http");

        private final String fieldDescription;

        NotificationTaskType(String fieldDescription) {
            this.fieldDescription = fieldDescription;
        }

        public String getFieldDescription() {
            return fieldDescription;
        }
    }
}


