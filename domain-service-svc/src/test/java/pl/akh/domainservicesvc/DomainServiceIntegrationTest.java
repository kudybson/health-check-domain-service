package pl.akh.domainservicesvc;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = {Application.class,
        DataSourceConfig.class})
@ActiveProfiles("test")
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.SERIALIZABLE)
@ExtendWith(MockitoExtension.class)
public class DomainServiceIntegrationTest {
}
