package pl.akh.domainservicesvc.domain.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

public class DateUtils {
    public static LocalDateTime getDayOfCurrentWeek(DayOfWeek dayOfWeek) {
        return LocalDate.now().with(ChronoField.DAY_OF_WEEK, dayOfWeek.getValue()).atTime(0, 0);
    }
}
