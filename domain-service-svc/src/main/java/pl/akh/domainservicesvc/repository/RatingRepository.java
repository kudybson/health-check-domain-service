package pl.akh.domainservicesvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.model.entities.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {
}
