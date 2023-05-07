package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.akh.domainservicesvc.domain.model.entities.Rating;

import java.util.List;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("select r from Rating r where r.doctor.id = :doctorId")
    List<Rating> getRatingsByDoctorId(UUID doctorId);
}
