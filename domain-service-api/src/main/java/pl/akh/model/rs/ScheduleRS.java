package pl.akh.model.rs;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ScheduleRS {
    LocalDateTime startDateTime;
    LocalDateTime endDateTime;
}
