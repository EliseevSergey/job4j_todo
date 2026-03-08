package ru.job4j.utility;

import ru.job4j.model.Task;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

public class TimeUtility {

    public static List<TimeZone> getAllTimeZones() {
        var zones = new ArrayList<TimeZone>();
        for (String timeId : TimeZone.getAvailableIDs()) {
            zones.add(TimeZone.getTimeZone(timeId));
        }
        return zones;
    }

    public static TimeZone getDefaultTimeZone() {
        return TimeZone.getDefault();
    }

    public static LocalDateTime convertToUserTimeZone(LocalDateTime taskTime, String userTimezone) {
        return taskTime
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of(userTimezone))
                .toLocalDateTime();
    }

    public static Task convertTaskToUserTimeZone(Task task, String userTimezone) {
        task.setCreated(convertToUserTimeZone(task.getCreated(), userTimezone));
        return task;
    }

    public static Collection<Task> convertTasksToUserTimeZone(Collection<Task> tasks, String userTimezone) {
        if (tasks == null || tasks.isEmpty()) {
            return tasks;
        }
        for (Task task : tasks) {
            if (task.getCreated() != null) {
                task.setCreated(convertToUserTimeZone(task.getCreated(), userTimezone));
            }
        }
        return tasks;
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
