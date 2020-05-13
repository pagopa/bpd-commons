package it.gov.pagopa.bpd.common.connector;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import eu.sia.meda.DummyConfiguration;
import lombok.SneakyThrows;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.Map;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
//@AutoConfigureWireMock
@ContextConfiguration(initializers = BaseFeignRestClientTest.RandomPortInitializer.class)
@Import({DummyConfiguration.class})
public abstract class BaseFeignRestClientTest {

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(
            wireMockConfig().dynamicPort()
    );
    protected static String SERIVICE_PORT_ENV_VAR_NAME;

    private static void injectEnvironmentVariable(String key, String value)
            throws Exception {

        Class<?> processEnvironment = Class.forName("java.lang.ProcessEnvironment");

        Field unmodifiableMapField = getAccessibleField(processEnvironment, "theUnmodifiableEnvironment");
        Object unmodifiableMap = unmodifiableMapField.get(null);
        injectIntoUnmodifiableMap(key, value, unmodifiableMap);

        Field mapField = getAccessibleField(processEnvironment, "theEnvironment");
        Map<String, String> map = (Map<String, String>) mapField.get(null);
        map.put(key, value);

        mapField = getAccessibleField(processEnvironment, "theCaseInsensitiveEnvironment");
        map = (Map<String, String>) mapField.get(null);
        map.put(key, value);
    }

    private static Field getAccessibleField(Class<?> clazz, String fieldName)
            throws NoSuchFieldException {

        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    private static void injectIntoUnmodifiableMap(String key, String value, Object map)
            throws ReflectiveOperationException {

        Class unmodifiableMap = Class.forName("java.util.Collections$UnmodifiableMap");
        Field field = getAccessibleField(unmodifiableMap, "m");
        Object obj = field.get(map);
        ((Map<String, String>) obj).put(key, value);
    }

    @Configuration
    @ImportAutoConfiguration(FeignAutoConfiguration.class)
    static class ContextConfiguration {
    }

    public static class RandomPortInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @SneakyThrows
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            injectEnvironmentVariable(SERIVICE_PORT_ENV_VAR_NAME, String.valueOf(wireMockRule.port()));
//            TestPropertySourceUtilsF
//                    .addInlinedPropertiesToEnvironment(applicationContext,
//                            "rest-client.citizen.base-url=" + "http://localhost:" + wireMockRule.port() + "/bpd/citizens"
//                    );
        }
    }

}
