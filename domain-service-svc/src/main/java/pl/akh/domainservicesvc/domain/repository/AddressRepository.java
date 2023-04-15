package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.akh.domainservicesvc.domain.model.entities.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
