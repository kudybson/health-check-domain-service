package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.Rating;
import pl.akh.model.rs.RatingRS;

public class RatingMapper {
    public static RatingRS mapToDto(Rating rating) {
        return RatingRS.builder()
                .id(rating.getId())
                .doctorUUID(rating.getDoctor().getId())
                .patientUUID(rating.getPatient().getId())
                .description(rating.getDescription())
                .grade(rating.getGrade())
                .build();
    }
}
