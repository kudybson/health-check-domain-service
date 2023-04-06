package pl.akh.domainservicesvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.model.entities.Test;

public interface TestRepository extends JpaRepository<Test, Long> {
}
