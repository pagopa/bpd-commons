package it.gov.pagopa.bpd.common.connector;

import eu.sia.meda.DummyConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = "classpath:config/rest-client.properties")
@ContextConfiguration(classes = {DummyConfiguration.class, FeignAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class})
public abstract class BaseFeignRestClientTest {

}
