package pl.akh.model.rs.schedules;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ScheduleRS {
    private Long id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
