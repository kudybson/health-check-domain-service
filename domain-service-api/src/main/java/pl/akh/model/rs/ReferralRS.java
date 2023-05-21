package pl.akh.model.rs;

import lombok.Builder;
import lombok.Data;
import pl.akh.model.common.TestType;

import java.sql.Timestamp;

@Data
@Builder
public class ReferralRS {
    private Long id;
    private TreatmentRS treatment;
    private TestType testType;
    private Timestamp expirationDate;
}
