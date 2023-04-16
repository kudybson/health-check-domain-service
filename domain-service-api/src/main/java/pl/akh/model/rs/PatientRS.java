package pl.akh.model.rs;

import lombok.Builder;
import lombok.Data;
import pl.akh.model.common.Gender;
import pl.akh.model.rq.AddressRQ;

import java.util.UUID;

@Data
@Builder
public class PatientRS {
    private UUID patientUUID;
    private String firstName;
    private String lastName;
    private Gender gender;

    private AddressRQ addressRQ;
}
