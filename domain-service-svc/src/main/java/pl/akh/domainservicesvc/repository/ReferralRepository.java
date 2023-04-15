package pl.akh.domainservicesvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.model.entities.Referral;

public interface ReferralRepository extends JpaRepository<Referral, Long> {
}
