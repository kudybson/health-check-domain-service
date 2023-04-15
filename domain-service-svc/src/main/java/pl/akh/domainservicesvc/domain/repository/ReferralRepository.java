package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.domain.model.entities.Referral;

public interface ReferralRepository extends JpaRepository<Referral, Long> {
}
