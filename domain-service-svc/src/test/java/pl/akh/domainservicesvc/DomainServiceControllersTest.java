package pl.akh.domainservicesvc;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class DomainServiceControllersTest {
    @Value(value = "${local.server.port}")
    private int port;
    @Autowired
    protected TestRestTemplate restTemplate;
    private final String host = "localhost";
    private final String protocol = "http";
    private final String contextPath = "/api/domain-service";

    protected String getUrl() {
        return protocol + "://" + host + ":" + port + contextPath;
    }

    //TODO add oauth mock
}
