package pl.akh.domainservicesvc;


import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = {Application.class,
        DataSourceConfig.class})
@ActiveProfiles("test")
@Transactional
public class DomainServiceIntegrationTest {
}
