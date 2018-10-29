import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;

public class Service {
    private Timer timer = new Timer(true);

    public void scheduleTask(String taskString) {
        String[] parts = taskString.trim().split(";");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Bad number of arguments, expected 5");
        }
        String externalId = parts[0];
        String message = parts[1];

        LocalTime time = LocalTime.parse(parts[2]);

        Task.NotificationTaskType notificationType = Task.NotificationTaskType.valueOf(parts[3]);
        String extraParams = parts[4];

        Task task = new Task(externalId, notificationType, message, time, extraParams);
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), time);
        timer.schedule(task, Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }
}