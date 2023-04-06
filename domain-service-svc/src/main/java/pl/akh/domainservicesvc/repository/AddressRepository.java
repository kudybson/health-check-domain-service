package pl.akh.domainservicesvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.model.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
