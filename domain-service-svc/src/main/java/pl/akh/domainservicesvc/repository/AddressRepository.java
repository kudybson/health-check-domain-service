package pl.akh.domainservicesvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.akh.domainservicesvc.model.entities.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
